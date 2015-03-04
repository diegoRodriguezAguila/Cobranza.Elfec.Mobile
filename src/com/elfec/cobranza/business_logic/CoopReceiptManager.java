package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.business_logic.DataImporter.ImportSpecs;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.remote_data_access.CoopReceiptRDA;

/**
 * Se encarga de las operaciones de negocio de <b>CBTES_COOP</b> 
 * @author drodriguez
 *
 */
public class CoopReceiptManager {
	/**
	 * Importa los CBTES_COOP de oracle y los guarda
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataAccessResult<List<CoopReceipt>> importCoopReceipts(final String username, final String password, final String routes)
	{
		return DataImporter.importData(new ImportSpecs<CoopReceipt, List<CoopReceipt>>() {
			@Override
			public List<CoopReceipt> requestData() throws ConnectException,
					SQLException {
				return CoopReceiptRDA.requestCoopReceipts(username, password, routes);
			}

			@Override
			public List<CoopReceipt> resultHandle(List<CoopReceipt> importList) {
				return importList;
			}
			
		});
	}
}
