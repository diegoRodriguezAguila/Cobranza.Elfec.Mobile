package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.PeriodBankAccount;
import com.elfec.cobranza.model.WSCollection;
import com.elfec.cobranza.model.data_exchange.DataExporter;
import com.elfec.cobranza.model.data_exchange.DataExporter.ExportSpecs;
import com.elfec.cobranza.model.enums.ExportStatus;
import com.elfec.cobranza.model.events.DataExportListener;
import com.elfec.cobranza.model.exceptions.NoPeriodBankAccountException;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.WSCollectionRDA;

/**
 * Maneja las operaciones de negocio de COB_WS
 * @author drodriguez
 *
 */
public class WSCollectionManager {

	/**
	 * Genera el COB_WS para el cobro realizado
	 * @param receipt
	 * @param type COBRANZA o ANULACION_COBRANZA
	 * @return WSCollection
	 * @throws NoPeriodBankAccountException 
	 */
	public static WSCollection generateWSCollection(CoopReceipt receipt, String type) throws NoPeriodBankAccountException {
		PeriodBankAccount period = PeriodBankAccount.findByCashdeskNumberAndDate(SessionManager.getLoggedCashdeskNumber());
		if(period==null)
			throw new NoPeriodBankAccountException(SessionManager.getLoggedCashdeskNumber());
		return new WSCollection(type, receipt.getReceiptId(), "P", 
					1, SessionManager.getLoggedCashdeskNumber(), 
					period.getPeriodNumber(), 
					DateTime.now(), ExportStatus.NOT_EXPORTED);
	}
	
	/**
	 * Exporta todos los COB_WS
	 * @return resultado del acceso remoto a datos
	 */
	public static DataAccessResult<Boolean> exportAllWSCollections(final String username, final String password, 
			DataExportListener exportListener)
	{
		return DataExporter.exportData(new ExportSpecs<WSCollection>() {

			@Override
			public List<WSCollection> requestExportData() {
				return WSCollection.getAll(WSCollection.class);
			}

			@Override
			public int exportData(WSCollection wSCollection) throws ConnectException,
					SQLException {
				return WSCollectionRDA.insertWSCollection(username, password, wSCollection);
			}
		}, exportListener);
	}

}
