package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.model.AnnulmentReason;
import com.elfec.cobranza.model.data_exchange.DataImporter;
import com.elfec.cobranza.model.data_exchange.DataImporter.ImportSource;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.AnnulmentReasonRDA;

/**
 * Se encarga de las operaciones de negocio de <b>MOTIVOS_ANULACION</b>
 * @author drodriguez
 *
 */
public class AnnulmentReasonManager {
	/**
	 * Importa los MOTIVOS_ANULACION de oracle y los guarda
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataAccessResult<Boolean> importAnnulmentReasons(final String username, final String password)
	{
		return DataImporter.importOnceRequiredData(new ImportSource<AnnulmentReason>() {
			@Override
			public List<AnnulmentReason> requestData() throws ConnectException, SQLException {
				return AnnulmentReasonRDA.requestAnnulmentReasons(username, password);
			}
		}); 
	}
}
