package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.business_logic.DataImporter.ImportSpecs;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.SupplyStatus;
import com.elfec.cobranza.remote_data_access.SupplyStatusRDA;

/**
 * Se encarga de las operaciones de negocio de <b>SUMIN_ESTADOS</b> 
 * @author drodriguez
 *
 */
public class SupplyStatusManager {
	/**
	 * Importa los SUMIN_ESTADOS de oracle y los guarda
	 * @param username
	 * @param password
	 * @param coopReceiptIds
	 * @return
	 */
	public static DataAccessResult<Boolean> importSupplyStatuses(final String username, final String password, final String coopReceiptIds)
	{
		return DataImporter.importData(new ImportSpecs<SupplyStatus, Boolean>() {
			@Override
			public List<SupplyStatus> requestData() throws ConnectException,
					SQLException {
				return SupplyStatusRDA.requestSupplyStatuses(username, password, coopReceiptIds);
			}
			@Override
			public Boolean resultHandle(List<SupplyStatus> importList) {
				if(importList==null)
					return false;
				return importList.size()>0;
			}
		});
	}
}
