package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.Zone;
import com.elfec.cobranza.model.enums.DataExchangeStatus;
import com.elfec.cobranza.model.events.DataExportListener;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.model.results.ManagerProcessResult;
import com.elfec.cobranza.remote_data_access.RouteRDA;
import com.elfec.cobranza.remote_data_access.ZoneRDA;

/**
 * Se encarga de las operaciones de lógica de negocio de zonas 
 * @author drodriguez
 *
 */
public class ZonesManager {

	/**
	 * Importa las zonas asignadas al usuario y sus respectivas rutas
	 * @param user el usuario del cual se quieren obtener sus zonas asignadas
	 * @param password
	 * @return El resultado del acceso a datos, sus errores y el usuario con las zonas y rutas ya asignadas
	 */
	public static DataAccessResult<User> importUserZones(User user, String password)
	{
		DataAccessResult<User> result = new DataAccessResult<User>(true, user);
		ActiveAndroid.beginTransaction();
		try {
			List<Zone> userZones = ZoneRDA.requestUserZones(user, password);
			List<Route> zoneRoutes;
			for(Zone zone : userZones)
			{
				zoneRoutes = RouteRDA.requestZoneRoutes(zone, user.getUsername(), password);
				zone.save();
				for(Route route : zoneRoutes)
				{
					route.save();
				}
			}
			ActiveAndroid.setTransactionSuccessful();
		} catch (ConnectException e) {
			result.addError(e);
		} catch (SQLException e) {
			e.printStackTrace();
			result.addError(e);
		}
		finally{
			ActiveAndroid.endTransaction();
		}
		return result;
	}
	
	/**
	 * Asigna el valor de ya cargadas a las rutas requeridas remotamente.
	 * Se conecta remotamente al servidor Oracle para bloquear las rutas
	 * @param zoneRoutes
	 */
	public static DataAccessResult<Void> setRemoteZoneRoutesLocked(List<Route> zoneRoutes, String username, String password, String IMEI)
	{
		DataAccessResult<Void> result = new DataAccessResult<Void>(true);
		DataAccessResult<Void> res;
		for(Route route : zoneRoutes)
		{
			res = DataExchangeControlManager.lockRoute(username, password, IMEI, route);
			result.addErrors(res.getErrors());
			if(res.hasErrors())
				break;
		}
		return result;
	}
	/**
	 *  Asigna el valor de descargadas a las rutas requeridas remotamente.
	 *  Se conecta remotamente al servidor Oracle para desbloquear las rutas
	 * @param username
	 * @param password
	 * @param IMEI
	 * @param exportListener
	 * @param unlockType el tipo de desbloqueo si por eliminación o descarga, no se 
	 * puede enviar {@link DataExchangeStatus}.IMPORTED da excepción
	 * @return resultado de acceso a datos
	 */
	public static ManagerProcessResult setRemoteZoneRoutesUnlocked(String username, String password, 
			String IMEI, DataExchangeStatus unlockType, DataExportListener exportListener)
	{
		if(exportListener==null)
			exportListener = new DataExportListener() {//DUMMY Listener
			@Override
				public void onExporting(int exportCount, int totalElements) {}
				@Override
				public void onExportInitialized(int totalElements) {}				
				@Override
				public void onExportFinalized() {}
			};//DUMMY Listener
		List<Route> routes = Route.getAllLoadedRoutes();
		int size = routes.size(), count=0;
		exportListener.onExportInitialized(size);
		ManagerProcessResult result = new ManagerProcessResult();
		DataAccessResult<Void> res;
		for(Route route : routes)
		{
			res = DataExchangeControlManager.unlockRoute(username, password, IMEI, route, unlockType);
			count++;
			exportListener.onExporting(count, size);
			result.addErrors(res.getErrors());
			if(res.hasErrors())
				break;
		}
		exportListener.onExportFinalized();
		return result;
	}
	
	/**
	 * Asigna el valor de ya cargadas a las rutas requeridas
	 * @param zoneRoutes
	 */
	public static void setZoneRoutesLoaded(List<Route> zoneRoutes)
	{
		for(Route route : zoneRoutes)
		{
			route.setLoaded(true);
			route.save();
		}
	}
	
	/**
	 * Valida si es que la ruta tiene alguna factura y por ende debe marcarse como cargada y/o bloqueada
	 * @param route
	 * @return true si es que la ruta tiene alguna factura
	 */
	public static boolean hasRouteReceipts(Route route)
	{
		return CoopReceipt.findRouteReceipts(route.getRouteRemoteId()).size()>0;
	}
}
