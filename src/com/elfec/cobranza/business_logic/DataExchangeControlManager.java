package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.data_exchange.DataExchangeControl;
import com.elfec.cobranza.model.enums.DataExchangeStatus;
import com.elfec.cobranza.model.exceptions.UnableToChangeRouteStateException;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.DataExchangeControlRDA;

/**
 * Se encarga de las operaciones de negocio de <b>MOVILES.COBRA_RUTAS_ESTADOS</b>
 * @author drodriguez
 *
 */
public class DataExchangeControlManager {

	/**
	 * Filtra y obtiene las rutas que ya se encuentren bloqueadas, si es que no hay ninguna
	 * el resultado tiene una lista vacia de rutas.
	 * @param username
	 * @param password
	 * @param IMEI
	 * @param routes
	 * @return Lista de rutas bloqueadas y lista de errores en caso de haber ocurrido
	 */
	public static DataAccessResult<List<Route>> filterLockedRoutes(String username, 
			String password, List<Route> routes)
	{
		DataAccessResult<List<Route>> result = new  DataAccessResult<List<Route>>(true);
		DataAccessResult<Boolean> res;
		List<Route> lockedRoutes = new ArrayList<Route>();
		for (Route route : routes) {
			res = isRouteLocked(username, password, route);
			if(res.hasErrors())
				break;
			if(res.getResult()) // is locked
				lockedRoutes.add(route);
		}
		result.setResult(lockedRoutes);
		return result;
	}
	
	/**
	 * Verifica si es que una ruta está bloqueada de carga
	 * @param username
	 * @param password
	 * @param IMEI
	 * @param route
	 * @return resultado
	 */
	public static DataAccessResult<Boolean> isRouteLocked(String username,
			String password, Route route) {
		DataAccessResult<Boolean> result = new DataAccessResult<Boolean>(true);
		try {
			DataExchangeControl dataExchangeControl = DataExchangeControlRDA
					.requestDataExchangeControl(username, password,
							route.getRouteRemoteId(), DateTime.now());
			result.setResult(dataExchangeControl != null);
		} catch (ConnectException e) {
			result.addError(e);
		} catch (SQLException e) {
			result.addError(e);
		} catch (Exception e) {
			e.printStackTrace();
			result.addError(e);
		}
		return result;
	}

	/**
	 * Bloquea una ruta para que ningún otro dispositivo la pueda cargar
	 * @param username
	 * @param password
	 * @param IMEI
	 * @param route
	 * @return resultado del acceso remoto a datos
	 */
	public static DataAccessResult<Void> lockRoute(String username, String password, String IMEI, Route route)
	{
		 DataAccessResult<Void> result = new DataAccessResult<Void>(true);
		 try {
			long lockRemoteId = DataExchangeControlRDA.registerDataImportControl(username, password, 
						new DataExchangeControl(route.getRouteRemoteId(), username, DateTime.now(), IMEI, DataExchangeStatus.IMPORTED));
			if(lockRemoteId==-1)
				throw new UnableToChangeRouteStateException(route.getRouteRemoteId(), true);
			route.setLockRemoteId(lockRemoteId);
			route.save();
		 } catch (ConnectException e) {
			result.addError(e);
		 } catch (SQLException e) {
			result.addError(e);
		 }catch(UnableToChangeRouteStateException e){
			result.addError(e);
		 }
		 return result;
	}
	
	/**
	 * Desbloquea una ruta para que ningún otro dispositivo la pueda cargar
	 * @param username
	 * @param password
	 * @param IMEI
	 * @param route
	 * @param unlockType el tipo de desbloqueo si por eliminación o descarga, no se 
	 * puede enviar {@link DataExchangeStatus}.IMPORTED da excepción
	 * @return resultado del acceso remoto a datos
	 */
	public static DataAccessResult<Void> unlockRoute(String username,
			String password, String IMEI, Route route,
			DataExchangeStatus unlockType) {
		DataAccessResult<Void> result = new DataAccessResult<Void>(true);
		try {
			if(unlockType==DataExchangeStatus.IMPORTED)
				throw new IllegalArgumentException("No se puede pasar el parámetro de estado "
						+ "importada (DataExchangeStatus.IMPORTED) para desbloquear remotamente una ruta");
			DataExchangeControlRDA.registerDataExportControl(
					username, password, route.getLockRemoteId(),
					new DataExchangeControl(username, DateTime.now(), IMEI,
							unlockType));
		} catch (ConnectException e) {
			result.addError(e);
		} catch (SQLException e) {
			result.addError(e);
		}
		return result;
	}
}
