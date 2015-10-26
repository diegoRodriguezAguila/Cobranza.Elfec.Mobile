package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.data_exchange.DataExporter;
import com.elfec.cobranza.business_logic.data_exchange.DataExporter.ExportSpecs;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.PeriodBankAccount;
import com.elfec.cobranza.model.WSCollection;
import com.elfec.cobranza.model.enums.ExportStatus;
import com.elfec.cobranza.model.enums.TransactionAction;
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
	public static WSCollection generateWSCollection(CoopReceipt receipt, TransactionAction type) throws NoPeriodBankAccountException {
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
				return WSCollection.getExportPendingWSCollections();
			}

			@Override
			public int exportData(WSCollection wSCollection) throws ConnectException,
					SQLException {
				return WSCollectionRDA.insertWSCollection(username, password, 
						getWSCollectionWithRemoteTransactionNumber(username, password, wSCollection));
			}
		}, exportListener);
	}
	
	/**
	 * Solicita y asigna un numero de transacción remoto para el WSCollection
	 * @param username
	 * @param password
	 * @param wSCollection
	 * @return la misma instancia del wSCollection con el numero de transacción remoto asignado
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static WSCollection getWSCollectionWithRemoteTransactionNumber(String username, String password, WSCollection wSCollection) throws ConnectException, SQLException
	{
		if(wSCollection.getRemoteTransactionNumber()==null)			
		{
			wSCollection.setRemoteTransactionNumber(
					WSCollectionRDA.requestRemoteTransactionNumber(username, password));
			wSCollection.save();
		}
		return wSCollection;
	}

}
