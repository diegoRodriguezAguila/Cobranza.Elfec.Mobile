package com.elfec.cobranza.business_logic;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.PeriodBankAccount;
import com.elfec.cobranza.model.WSCollection;
import com.elfec.cobranza.model.exceptions.CollectionPaymentException;

import android.database.SQLException;

/**
 * Maneja las operaciones de negocio de COBROS y COB_WS
 * @author drodriguez
 *
 */
public class CollectionManager {

	/**
	 * Registra el cobro de multiples facturas
	 * @param receipt
	 * @return Lista de los Ids de los cobros realizados
	 */
	public static DataAccessResult<List<Long>> savePayments(List<CoopReceipt> receipts){
		DataAccessResult<List<Long>> result = new DataAccessResult<List<Long>>();
		DataAccessResult<Long> res;
		List<Long> collectionPaymentIds = new ArrayList<Long>();
		int size = receipts.size();
		for (int i = 0; i < size; i++) {
			res = savePayment(receipts.get(i));
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
	public static DataAccessResult<Long> savePayment(CoopReceipt receipt){
		DataAccessResult<Long> result = new DataAccessResult<Long>();
		try
		{
			result.setResult((new CollectionPayment(SessionManager.getLoggedCashdeskNumber(), DateTime.now(),
					SessionManager.getLoggedInUsername(), receipt.getReceiptId(), receipt.getTotalAmount(), 1, 
					null, null, receipt.getSupplyId(), 
					receipt.getSupplyNumber(), receipt.getReceiptNumber(), receipt.getYear(), 
					receipt.getPeriodNumber(), SessionManager.getLoggedCashdeskDesc(), null)).save());
			generateWSCollection(receipt).save();
		}
		catch(SQLException e)
		{ 
			result.addError(new CollectionPaymentException(receipt.getReceiptNumber()));
		}
		return result;
	}

	/**
	 * Genera el COB_WS para el cobro realizado
	 * @param receipt
	 * @return WSCollection
	 */
	private static WSCollection generateWSCollection(CoopReceipt receipt) {
		return new WSCollection("COBRANZA", receipt.getReceiptId(), "P", 
				1, SessionManager.getLoggedCashdeskNumber(), 
				PeriodBankAccount.findByCashdeskNumberAndDate(SessionManager.getLoggedCashdeskNumber()).getPeriodNumber(), 
				DateTime.now());
	}
}
