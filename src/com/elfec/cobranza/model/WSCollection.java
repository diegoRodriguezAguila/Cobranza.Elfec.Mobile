package com.elfec.cobranza.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Contiene los datos de la tabla COB_WS
 * @author drodriguez
 *
 */
@Table(name = "WSCollections")
public class WSCollection extends Model{
	
	/**
	 * ACCION en Oracle
	 */
	@Column(name = "Action")
	private String action;
	/**
	 * IDCBTE Identificador unico de comprobantes
	 */
	@Column(name = "ReceiptId", notNull=true)
	private int receiptId;
	/**
	 * ESTADO 'P' pendiente, cobro que no fué procesado
	 */
	@Column(name = "Status")
	private String status;
	/**
	 * IDBANCO en Oracle
	 */
	@Column(name = "BankId")
	private int bankId;	
	/**
	 * IDBAN_CTA en Oracle
	 */
	@Column(name = "BankAccountId")
	private int bankAccountId;	
	/**
	 * NROPERIODO en Oracle
	 */
	@Column(name = "PeriodNumber")
	private int periodNumber;	
	/**
	 * FECHA_COBRO en Oracle, en la que se realizó el cobro
	 */
	@Column(name = "PaymentDate", notNull=true)
	private DateTime paymentDate;
	
	public WSCollection() {
		super();
	}

	public WSCollection(String action, int receiptId, String status, int bankId,
			int bankAccountId, int periodNumber, DateTime paymentDate) {
		super();
		this.action = action;
		this.receiptId = receiptId;
		this.status = status;
		this.bankId = bankId;
		this.bankAccountId = bankAccountId;
		this.periodNumber = periodNumber;
		this.paymentDate = paymentDate;
	}

	//#region Getters y Setters
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public int getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public int getPeriodNumber() {
		return periodNumber;
	}

	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}

	public DateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(DateTime paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	//#endregion
}
