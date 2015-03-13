package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.business_logic.DataImporter.ImportSpecs;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.exceptions.SupplyNotFoundException;
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
	 * @param coopReceiptIds
	 * @return
	 */
	public static DataAccessResult<Boolean> importSupplies(final String username, final String password, final String supplyIds)
	{
		return DataImporter.importData(new ImportSpecs<Supply, Boolean>() {
			@Override
			public List<Supply> requestData() throws ConnectException,
					SQLException {
				return SupplyRDA.requestSupplies(username, password, supplyIds);
			}
			@Override
			public Boolean resultHandle(List<Supply> importList) {
				if(importList==null)
					return false;
				return importList.size()>0;
			}
		});
	}
	
	/**
	 * Obtiene el suministro que coincide con el nus o el numero de cuenta proporcionados, o ambos
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
		if(foundSupply==null)
			throw new SupplyNotFoundException(nus, accountNumber);
		return foundSupply;
	}
	/**
	 * Obtiene las facturas de un suministro con su respectivo estado de anulada, etc
	 * @param supply
	 * @return
	 */
	public static List<CoopReceipt> getSupplyReceiptsWithStatus(Supply supply)
	{
		return supply.getReceipts(true);		
	}
}
