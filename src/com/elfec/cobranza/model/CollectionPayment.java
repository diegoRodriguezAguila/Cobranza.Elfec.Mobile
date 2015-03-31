package com.elfec.cobranza.model;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.elfec.cobranza.model.serializers.JodaDateTimeSerializer;
/**
 * Almacena la información de los COBROS
 * @author drodriguez
 *
 */
@Table(name = "CollectionPayments")
public class CollectionPayment extends Model {
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
	 * NROTRANSACCION en Oracle
	 */
	@Column(name = "TransactionNumber", notNull=true)
	private long transactionNumber;
	/**
	 * FECHA_BAJA en Oracle, en la que se anuló un cobro
	 */
	@Column(name = "AnnulmentDate")
	private DateTime annulmentDate;
	/**
	 * USUARIO_BAJA el usuario que realizó la anulación del cobro
	 */
	@Column(name = "AnnulmentUser")
	private String annulmentUser;
	/**
	 * NROTRANSACCION_A en Oracle
	 */
	@Column(name = "AnnulmentTransacNum")
	private long annulmentTransacNum;
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
	private Integer annulmentReasonId;
	
	public CollectionPayment() {
		super();
	}
	
	public CollectionPayment(int cashDeskNumber, DateTime paymentDate,
			String user, int receiptId, BigDecimal amount, int status,
			long transactionNumber, int supplyId, String supplyNumber,
			int receiptNumber, int year, int periodNumber,
			String cashDeskDescription) {
		super();
		this.cashDeskNumber = cashDeskNumber;
		this.paymentDate = paymentDate;
		this.user = user;
		this.receiptId = receiptId;
		this.amount = amount;
		this.status = status;
		this.transactionNumber = transactionNumber;
		this.supplyId = supplyId;
		this.supplyNumber = supplyNumber;
		this.receiptNumber = receiptNumber;
		this.year = year;
		this.periodNumber = periodNumber;
		this.cashDeskDescription = cashDeskDescription;
	}

	/**
	 * Anula el cobro, poniendo su estado en cero y llenando los campos de
	 * anulación. <br><b>NOTA.-</b> No lo guarda, es necesario llamar a save()
	 * @param annulmentUser
	 * @param annulmentTransacNum
	 * @param annulmentReasonId
	 */
	public void setAnnulated(String annulmentUser, long annulmentTransacNum, int annulmentReasonId)
	{
		status = 0;
		annulmentDate = DateTime.now();
		this.annulmentUser = annulmentUser;
		this.annulmentTransacNum = annulmentTransacNum;
		this.annulmentReasonId = annulmentReasonId;
	}
	
	/**
	 * Obtiene todos los cobros válidos, es decir con estado 1, realizados
	 * entre las fechas proporcionadas realizadas por el cajero
	 * @param startDate fecha inicio (inclusiva)
	 * @param endDate fecha fin (inclusiva)
	 * @param cashDeskNum
	 * @return lista de cobros
	 */
	public static List<CollectionPayment> getValidCollectionPayments(DateTime startDate, DateTime endDate, int cashDeskNum)
	{
		JodaDateTimeSerializer serializer = new JodaDateTimeSerializer();
		return new Select().from(CollectionPayment.class)
				.where("Status=1").where("CashDeskNumber = ?", cashDeskNum)
				.where("PaymentDate >= ?", serializer.serialize(startDate))
				.where("PaymentDate <= ?", serializer.serialize(endDate))
				.orderBy("PaymentDate")
				.execute();
	}
	
	/**
	 * Obtiene todos los cobros anulados, es decir con estado 0, realizados
	 * entre las fechas proporcionadas realizadas por el cajero
	 * @param startDate fecha inicio (inclusiva)
	 * @param endDate fecha fin (inclusiva)
	 * @param cashDeskNum
	 * @return lista de cobros
	 */
	public static List<CollectionPayment> getAnnuledCollectionPayments(DateTime startDate, DateTime endDate, int cashDeskNum)
	{
		JodaDateTimeSerializer serializer = new JodaDateTimeSerializer();
		return new Select().from(CollectionPayment.class)
				.where("Status=0").where("CashDeskNumber = ?", cashDeskNum)
				.where("PaymentDate >= ?", serializer.serialize(startDate))
				.where("PaymentDate <= ?", serializer.serialize(endDate))
				.orderBy("PaymentDate")
				.execute();
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

	public long getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(long transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public DateTime getAnnulmentDate() {
		return annulmentDate;
	}

	public void setAnnulmentDate(DateTime annulmentDate) {
		this.annulmentDate = annulmentDate;
	}

	public String getAnnulmentUser() {
		return annulmentUser;
	}

	public void setAnnulmentUser(String withdrawalUser) {
		this.annulmentUser = withdrawalUser;
	}

	public long getAnnulmentTransacNum() {
		return annulmentTransacNum;
	}

	public void setAnnulmentTransacNum(long withdrawalTransacNum) {
		this.annulmentTransacNum = withdrawalTransacNum;
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

	public Integer getAnnulmentReasonId() {
		return annulmentReasonId;
	}

	public void setAnnulmentReasonId(Integer annulmentReasonId) {
		this.annulmentReasonId = annulmentReasonId;
	}
	
	//#endregion
}
