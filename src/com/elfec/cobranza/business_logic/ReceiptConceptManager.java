package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.business_logic.data_exchange.DataImporter;
import com.elfec.cobranza.business_logic.data_exchange.DataImporter.ImportSpecs;
import com.elfec.cobranza.model.ReceiptConcept;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.ReceiptConceptRDA;

/**
 * Se encarga de las operaciones de negocio de <b>CBTES_CPTOS</b> 
 * @author drodriguez
 *
 */
public class ReceiptConceptManager {
	/**
	 * Importa los CBTES_CPTOS de oracle y los guarda
	 * @param username
	 * @param password
	 * @param coopReceiptIds la lista de IDCBTE en formato (12334,1234)
	 * @return
	 */
	public static DataAccessResult<Boolean> importCoopReceipts(final String username, final String password, final String coopReceiptIds)
	{
		return DataImporter.importData(new ImportSpecs<ReceiptConcept, Boolean>() {
			@Override
			public List<ReceiptConcept> requestData() throws ConnectException,
					SQLException {
				return ReceiptConceptRDA.requestReceiptConcepts(username, password, coopReceiptIds);
			}

			@Override
			public Boolean resultHandle(List<ReceiptConcept> importList) {
				if(importList==null)
					return false;
				return importList.size()>0;
			}			
		});
	}
}
