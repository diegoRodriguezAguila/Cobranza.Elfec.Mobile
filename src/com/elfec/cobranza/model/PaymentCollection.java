package com.elfec.cobranza.model;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
/**
 * Almacena la información de los COBROS
 * @author drodriguez
 *
 */
@Table(name = "PaymentCollections")
public class PaymentCollection extends Model {
	/**
	 * CAJA en Oracle Número de caja
	 */
	@Column(name = "CashDeskNumber", notNull=true)
	private int cashDeskNumber;
	/**
	 * FECHA en Oracle, en la que se realizó el cobro
	 */
	@Column(name = "PaymentDate", notNull=true)
	private DateTime paymentDate;
	/**
	 * USUARIO el usuario que realizó el cobro
	 */
	@Column(name = "User", notNull=true)
	private String user;
	/**
	 * IDCBTE Identificador unico de comprobantes
	 */
	@Column(name = "ReceiptId", notNull=true)
	private int receiptId;
	/**
	 * MONTO en Oracle
	 */
	@Column(name = "Amount", notNull=true)
	private BigDecimal amount;	
	/**
	 * ESTADO 1 cobrado, 0 anulado
	 */
	@Column(name = "Status", notNull=true)
	private int status;
	
	/**
	 * FECHA_BAJA en Oracle, en la que se anuló un cobro
	 */
	@Column(name = "WithdrawalDate")
	private DateTime withdrawalDate;
	/**
	 * USUARIO_BAJA el usuario que realizó la anulación del cobro
	 */
	@Column(name = "WithdrawalUser")
	private String withdrawalUser;
	/**
	 * IDSUMINISTRO
	 * Identificador del suministros, tabla de referencia suministros	
	 */
	@Column(name = "SupplyId")
	private int supplyId;	
	/**
	 * NROSUM en Oracle
	 */
	@Column(name = "SupplyNumber")
	private String supplyNumber;
	/**
	 * NROCBTE en Oracle
	 */
	@Column(name = "ReceiptNumber")
	private int receiptNumber;
	/**
	 * ANIO en Oracle
	 */
	@Column(name = "Year")
	private int year;
	/**
	 * NROPER en Oracle
	 */
	@Column(name = "PeriodNumber")
	private int periodNumber;
	/**
	 * DESC_CAJA en Oracle Descripción de caja
	 */
	@Column(name = "CashDeskDescription")
	private String cashDeskDescription;
	/**
	 * IDMOTIVO_ANULA en Oracle El id del motivo de la anulación de un cobro
	 */
	@Column(name = "AnnulmentReasonId")
	private int annulmentReasonId;
	
	public PaymentCollection() {
		super();
	}

	public PaymentCollection(int cashDeskNumber, DateTime paymentDate,
			String user, int receiptId, BigDecimal amount, int status,
			DateTime withdrawalDate, String withdrawalUser, int supplyId,
			String supplyNumber, int receiptNumber, int year, int periodNumber,
			String cashDeskDescription, int annulmentReasonId) {
		super();
		this.cashDeskNumber = cashDeskNumber;
		this.paymentDate = paymentDate;
		this.user = user;
		this.receiptId = receiptId;
		this.amount = amount;
		this.status = status;
		this.withdrawalDate = withdrawalDate;
		this.withdrawalUser = withdrawalUser;
		this.supplyId = supplyId;
		this.supplyNumber = supplyNumber;
		this.receiptNumber = receiptNumber;
		this.year = year;
		this.periodNumber = periodNumber;
		this.cashDeskDescription = cashDeskDescription;
		this.annulmentReasonId = annulmentReasonId;
	}

	//#region Getters y Setters
	
	public int getCashDeskNumber() {
		return cashDeskNumber;
	}

	public void setCashDeskNumber(int cashDeskNumber) {
		this.cashDeskNumber = cashDeskNumber;
	}

	public DateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(DateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public DateTime getWithdrawalDate() {
		return withdrawalDate;
	}

	public void setWithdrawalDate(DateTime withdrawalDate) {
		this.withdrawalDate = withdrawalDate;
	}

	public String getWithdrawalUser() {
		return withdrawalUser;
	}

	public void setWithdrawalUser(String withdrawalUser) {
		this.withdrawalUser = withdrawalUser;
	}

	public int getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(int supplyId) {
		this.supplyId = supplyId;
	}

	public String getSupplyNumber() {
		return supplyNumber;
	}

	public void setSupplyNumber(String supplyNumber) {
		this.supplyNumber = supplyNumber;
	}

	public int getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(int receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getPeriodNumber() {
		return periodNumber;
	}

	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}

	public String getCashDeskDescription() {
		return cashDeskDescription;
	}

	public void setCashDeskDescription(String cashDeskDescription) {
		this.cashDeskDescription = cashDeskDescription;
	}

	public int getAnnulmentReasonId() {
		return annulmentReasonId;
	}

	public void setAnnulmentReasonId(int annulmentReasonId) {
		this.annulmentReasonId = annulmentReasonId;
	}
	
	//#endregion
}
