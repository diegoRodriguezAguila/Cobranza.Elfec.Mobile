package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.business_logic.data_exchange.DataImporter;
import com.elfec.cobranza.business_logic.data_exchange.DataImporter.ImportSpecs;
import com.elfec.cobranza.model.FineBonus;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.FineBonusRDA;

/**
 * Se encarga de las operaciones de negocio de <b>BONIF_MULTAS</b> y <b>BONIF_MULTAS_IT</b> 
 * @author drodriguez
 *
 */
public class FineBonusManager {
	/**
	 * Importa las <b>BONIF_MULTAS</b> y <b>BONIF_MULTAS_IT</b> de oracle y las guarda
	 * @param username
	 * @param password
	 * @param coopReceiptIds la lista de IDCBTE en formato (12334,1234)
	 * @return
	 */
	public static DataAccessResult<Boolean> importFineBonuses(final String username, final String password, final String coopReceiptIds)
	{
		return DataImporter.importData(new ImportSpecs<FineBonus, Boolean>() {
			@Override
			public List<FineBonus> requestData() throws ConnectException,
					SQLException {
				return FineBonusRDA.requestFineBonuses(username, password, coopReceiptIds);
			}

			@Override
			public Boolean resultHandle(List<FineBonus> importList) {
				if(importList==null)
					return false;
				return importList.size()>0;
			}			
		});
	}
}
