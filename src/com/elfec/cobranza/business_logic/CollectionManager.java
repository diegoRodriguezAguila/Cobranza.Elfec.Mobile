package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;

import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.data_exchange.DataExporter;
import com.elfec.cobranza.model.data_exchange.DataExporter.ExportSpecs;
import com.elfec.cobranza.model.enums.ExportStatus;
import com.elfec.cobranza.model.events.DataExportListener;
import com.elfec.cobranza.model.exceptions.AnnulationTimeExpiredException;
import com.elfec.cobranza.model.exceptions.CollectionException;
import com.elfec.cobranza.model.exceptions.NoPeriodBankAccountException;
import com.elfec.cobranza.model.printer.CashDeskDailyResume;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.CollectionPaymentRDA;
import com.elfec.cobranza.settings.ParameterSettingsManager;
import com.elfec.cobranza.settings.ParameterSettingsManager.ParamKey;

import android.database.SQLException;

/**
 * Maneja las operaciones de negocio de COBROS
 * @author drodriguez
 *
 */
public class CollectionManager {

	/**
	 * Registra el cobro de multiples facturas
	 * @param receipts
	 * @return Lista de los Ids de los cobros realizados
	 */
	public static DataAccessResult<List<Long>> payCollections(List<CoopReceipt> receipts){
		DataAccessResult<List<Long>> result = new DataAccessResult<List<Long>>();
		DataAccessResult<Long> res;
		List<Long> collectionPaymentIds = new ArrayList<Long>();
		int size = receipts.size();
		for (int i = 0; i < size; i++) {
			res = payCollection(receipts.get(i));
			result.addErrors(res.getErrors());
			collectionPaymentIds.add(res.getResult());
			if(result.hasErrors())
				break;
		}
		result.setResult(collectionPaymentIds);
		return result;
	}
	
	/**
	 * Registra el cobro de una factura
	 * @param receipt
	 * @return Resultado del acceso a datos, donde el resultado es el id que se le asignó al comprobante
	 */
	public static DataAccessResult<Long> payCollection(CoopReceipt receipt){
		DataAccessResult<Long> result = new DataAccessResult<Long>();
		try
		{
			long transactionNumber = WSCollectionManager.generateWSCollection(receipt, "COBRANZA").save();
			
			result.setResult(new CollectionPayment(SessionManager.getLoggedCashdeskNumber(), DateTime.now(), 
					SessionManager.getLoggedInUsername(), receipt.getReceiptId(), receipt.getTotalAmount(), 
					1, transactionNumber, receipt.getSupplyId(), 
					receipt.getSupplyNumber(), receipt.getReceiptNumber(), receipt.getYear(), 
					receipt.getPeriodNumber(), SessionManager.getLoggedCashdeskDesc(), ExportStatus.NOT_EXPORTED).save());
		}
		catch(SQLException e)
		{ 
			result.addError(new CollectionException(receipt.getReceiptNumber()));
		} catch (NoPeriodBankAccountException e) {
			result.addError(e);
		}
		return result;
	}
	/**
	 * Realiza la anulación de múltiples cobros
	 * @param receipts
	 * @return
	 */
	public static DataAccessResult<Void> annulateCollections(List<CoopReceipt> receipts, int annulmentReasonId){
		DataAccessResult<Void> result = new DataAccessResult<Void>();
		DataAccessResult<Void> res;
		
		int size = receipts.size();
		for (int i = 0; i < size; i++) {
			res = annulateCollection(receipts.get(i), annulmentReasonId);
			result.addErrors(res.getErrors());
			if(result.hasErrors())
				break;
		}
		return result;
	}
	
	/**
	 * Anula el cobro de una factura
	 * @param receipt
	 * @return Resultado del acceso a datos, donde el resultado es el id que se le asignó al comprobante
	 */
	public static DataAccessResult<Void> annulateCollection(CoopReceipt receipt, int annulmentReasonId){
		DataAccessResult<Void> result = new DataAccessResult<Void>();
		try
		{
			CollectionPayment payment = receipt.getActiveCollectionPayment();
			int maxDif = ParameterSettingsManager.getParameter(ParamKey.ANNULMENT_HOURS_LIMIT).getIntValue();
			if(Hours.hoursBetween(payment.getPaymentDate(),DateTime.now()).getHours()>maxDif)
				throw new AnnulationTimeExpiredException(receipt.getReceiptNumber(), receipt.getId());
			
			long transactionNumber = WSCollectionManager.generateWSCollection(receipt, "ANULACION_COBRANZA").save();			
			if(payment !=null)
			{
				payment.setAnnulled(SessionManager.getLoggedInUsername(), transactionNumber, annulmentReasonId);
				payment.save();
				receipt.clearActiveCollectionPayment();
			}
		}
		catch(SQLException e){ 
			result.addError(new CollectionException(receipt.getReceiptNumber()));
		} catch (NoPeriodBankAccountException e) {
			result.addError(e);
		} catch (AnnulationTimeExpiredException e) {
			result.addError(e);
		}
		return result;
	}

	/**
	 * Obtiene el resumen diario de caja
	 * @param date
	 * @return resumen diario de caja
	 */
	public static CashDeskDailyResume generateDailyResume(DateTime date)
	{
		CashDeskDailyResume cashDeskDailyResume = new CashDeskDailyResume();
		int cashDeskNum = SessionManager.getLoggedCashdeskNumber();
		
		cashDeskDailyResume.addMultipleCashDeskResumes(
				CollectionPayment.getRangedCashDeskResume("1. Cobrado", date, date, cashDeskNum, 0, 1));
		cashDeskDailyResume.addMultipleCashDeskResumes(
				CollectionPayment.getRangedCashDeskResume("2. Anulado", date, date, cashDeskNum, 0));
		cashDeskDailyResume.addMultipleCashDeskResumes(
				CollectionPayment.getRangedCashDeskResume("3. Total", date, date, cashDeskNum, 1));		
		List<CollectionPayment> payments = CollectionPayment.getValidCollectionPayments(date, date, cashDeskNum);
		int size = payments.size();
		cashDeskDailyResume.setInternalControlCodeStart(size==0?0:payments.get(0).getId());
		cashDeskDailyResume.setInternalControlCodeEnd(payments.get(size==0?0:size-1).getId());
		
		return cashDeskDailyResume;
	}
	
	/**
	 * Exporta todos los COBROS
	 * @return resultado del acceso remoto a datos
	 */
	public static DataAccessResult<Boolean> exportAllCollectionPayments(final String username, final String password, DataExportListener exportListener)
	{
		return DataExporter.exportData(new ExportSpecs<CollectionPayment>() {

			@Override
			public List<CollectionPayment> requestExportData() {
				return CollectionPayment.getAll(CollectionPayment.class);
			}

			@Override
			public int exportData(CollectionPayment collectionPayment) throws ConnectException,
					java.sql.SQLException {
				return CollectionPaymentRDA.insertCollectionPayment(username, password, collectionPayment);
			}
		}, exportListener);
	}
}
