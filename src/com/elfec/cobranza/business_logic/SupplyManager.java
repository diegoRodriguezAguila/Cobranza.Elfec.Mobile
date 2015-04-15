package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.data_exchange.DataImporter;
import com.elfec.cobranza.business_logic.data_exchange.DataImporter.ImportSpecs;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.exceptions.SupplyNotFoundException;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.SupplyRDA;

/**
 * Se encarga de las operaciones de negocio de <b>SUMINISTROS</b> 
 * @author drodriguez
 *
 */
public class SupplyManager {
	/**
	 * Importa los SUMINISTROS de oracle y los guarda
	 * @param username
	 * @param password
	 * @param routeIds
	 * @return resultado del acceso remoto a datos
	 */
	public static DataAccessResult<Boolean> importSupplies(final String username, final String password, final String routeIds)
	{
		return DataImporter.importData(new ImportSpecs<Supply, Boolean>() {
			@Override
			public List<Supply> requestData() throws ConnectException,
					SQLException {
				return SupplyRDA.requestSupplies(username, password, routeIds);
			}
			@Override
			public Boolean resultHandle(List<Supply> importList) {
				return (importList==null)?false:importList.size()>0;
			}
		});
	}
	
	/**
	 * Obtiene el suministro que coincide con el nus o el numero de cuenta proporcionados, o ambos
	 * También verifica que la ruta a la que pertenece el suministro se haya cargado
	 * @param nus
	 * @param accountNumber
	 * @return Suministro que coinicida con los parámetros
	 * @throws SupplyNotFoundException 
	 */
	public static Supply getSupplyByNUSOrAccount(String nus, String accountNumber) throws SupplyNotFoundException
	{
		int nusInt;
		try{nusInt = Integer.parseInt(nus);}
		catch(NumberFormatException e){nusInt = -1;}
		Supply foundSupply = Supply.findSupplyByNUSOrAccount(nusInt, accountNumber);
		if(foundSupply!=null)
		{
			Route loadedRoute = Route.findRouteByRemoteId(foundSupply.getRouteRemoteId());
			if(loadedRoute!=null && loadedRoute.isLoaded())
				return foundSupply;
		}
		throw new SupplyNotFoundException(nus, accountNumber);
	}
	/**
	 * Obtiene las facturas pendientes de un suministro
	 * @param supply
	 * @return
	 */
	public static List<CoopReceipt> getPendingReceipts(Supply supply)
	{
		return supply.getPendingReceipts();		
	}
	/**
	 * Obtiene las facturas que ya fueron pagadas de un suministro en la fecha actual
	 * @param supply
	 * @return
	 */
	public static List<CoopReceipt> getTodayPaidReceipts(Supply supply)
	{
		return supply.getDatePaidReceipts(DateTime.now());		
	}
}
