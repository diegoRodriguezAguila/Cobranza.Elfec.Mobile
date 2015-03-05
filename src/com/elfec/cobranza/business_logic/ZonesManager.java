package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.Zone;
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
	 * Asigna el valor de ya cargadas a las rutas requeridas
	 * @param zoneRoutes
	 */
	public static void setZoneRoutesLoaded(List<Route> zoneRoutes)
	{
		List<CoopReceipt> routeReceipts;
		for(Route route : zoneRoutes)
		{
			routeReceipts = CoopReceipt.findRouteReceipts(route.getRouteRemoteId());
			if(routeReceipts.size()>0)
			{
				route.setLoaded(true);
				route.save();
			}
		}
	}
}
