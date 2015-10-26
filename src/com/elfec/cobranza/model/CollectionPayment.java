package com.elfec.cobranza.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.elfec.cobranza.helpers.text_format.ObjectListToSQL;
import com.elfec.cobranza.model.enums.ExportStatus;
import com.elfec.cobranza.model.interfaces.IExportable;
import com.elfec.cobranza.model.printer.CashDeskResume;
import com.elfec.cobranza.model.serializers.JodaDateTimeSerializer;
/**
 * Almacena la información de los COBROS
 * @author drodriguez
 *
 */
@Table(name = "CollectionPayments")
public class CollectionPayment extends Model implements IExportable{
	
	public static final String INSERT_QUERY = "INSERT INTO COBRANZA.COBROS VALUES (%d, %d, TO_DATE('%s', 'dd/mm/yyyy'), '%s', %d, %s, %d, %d, USER, "
			+ "TO_DATE('%s', 'dd/mm/yyyy hh24:mi:ss'), %s, %s, %d, %s, %d, %s, %d, %d, %d, '%s', 'F', %s, 1, TO_DATE('%s', 'dd/mm/yyyy hh24:mi:ss'))";
	
	public static final String UPDATE_QUERY = "UPDATE COBRANZA.COBROS SET ESTADO=%d, FECHA_BAJA=%s, USUARIO_BAJA=%s, "
			+ "NROTRANSACCION_A=%s, IDMOTIVO_ANULA=%s"
			+ "WHERE CI=%d AND IDCBTE=%d AND ANIO=%d AND NROPER=%d";
	
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
	@Column(name = "ReceiptId", notNull=true, index=true)
	private int receiptId;
	/**
	 * MONTO en Oracle
	 */
	@Column(name = "Amount", notNull=true)
	private BigDecimal amount;	
	/**
	 * ESTADO 1 cobrado, 0 anulado
	 */
	@Column(name = "Status", notNull=true, index=true)
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
	@Column(name = "SupplyId", index=true)
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
	
	//ATRIBUTO EXTRA
	@Column(name = "ExportStatus", index=true, notNull=true)
	private short exportStatus;

	public CollectionPayment() {
		super();
	}
	
	public CollectionPayment(int cashDeskNumber, DateTime paymentDate,
			String user, int receiptId, BigDecimal amount, int status,
			long transactionNumber, int supplyId, String supplyNumber,
			int receiptNumber, int year, int periodNumber,
			String cashDeskDescription, ExportStatus exportStatus) {
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
		this.exportStatus = exportStatus.toShort();
	}
	
	

	/**
	 * Anula el cobro, poniendo su estado en cero y llenando los campos de
	 * anulación. <br><b>NOTA.-</b> No lo guarda, es necesario llamar a save()
	 * @param annulmentUser
	 * @param annulmentTransacNum
	 * @param annulmentReasonId
	 */
	public void setAnnulled(String annulmentUser, long annulmentTransacNum, int annulmentReasonId)
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
	public static List<CollectionPayment> getValidCollectionPayments(DateTime startDate, 
			DateTime endDate, int cashDeskNum)
	{
		return getCollectionPayments(1, startDate, endDate, cashDeskNum);
	}
	
	/**
	 * Obtiene todos los cobros realizados
	 * entre las fechas proporcionadas realizadas por el cajero
	 * @param status si se pasa -1 se omite esta condición
	 * @param startDate fecha inicio (inclusiva)
	 * @param endDate fecha fin (inclusiva)
	 * @param cashDeskNum
	 * @return lista de cobros
	 */
	public static List<CollectionPayment> getCollectionPayments(int status, DateTime startDate, 
			DateTime endDate, int cashDeskNum)
	{
		JodaDateTimeSerializer serializer = new JodaDateTimeSerializer();
		From query = new Select().from(CollectionPayment.class);
		if(status!=-1)
			query.where("Status=?", status);
		query.where("CashDeskNumber = ?", cashDeskNum)
			.where("PaymentDate >= ?", serializer.serialize(startDate.withTimeAtStartOfDay()))
			.where("PaymentDate <= ?", serializer.serialize(endDate.withTime(23, 59, 59, 999)))
			.orderBy("PaymentDate");
		return query.execute();
	}
	
	/**
	 * Obtiene todos los cobros anulados, es decir con estado 0, realizados
	 * entre las fechas proporcionadas realizadas por el cajero
	 * @param startDate fecha inicio (inclusiva)
	 * @param endDate fecha fin (inclusiva)
	 * @param cashDeskNum
	 * @return lista de cobros
	 */
	public static List<CollectionPayment> getAnnuledCollectionPayments(DateTime startDate, 
			DateTime endDate, int cashDeskNum)
	{
		return getCollectionPayments(0, startDate, endDate, cashDeskNum);
	}
	
	/**
	 * Obtiene los resúmenes de caja del rango de fechas provisto, de aquellos cobros efectivos , es decir que tienen 
	 * estado 1
	 * @param startDate
	 * @param endDate
	 * @param cashDeskNum
	 * @return lista de resumenes de caja en el rango de fechas
	 */
	public static List<CashDeskResume> getEffectiveCollectionsRangedCashDeskResume(DateTime startDate, 
			DateTime endDate, int cashDeskNum)
	{
		return getRangedCashDeskResume(null, startDate, endDate, cashDeskNum, 1);
	}
	
	/**
	 * Obtiene los resúmenes de caja del rango de fechas provisto
	 * @param concept
	 * @param startDate
	 * @param endDate
	 * @param cashDeskNum
	 * @param status la lista de estados que tiene que tener
	 * @return lista de resumenes de caja en el rango de fechas
	 */
	public static List<CashDeskResume> getRangedCashDeskResume(String concept, DateTime startDate, 
			DateTime endDate, int cashDeskNum, Integer... status)
	{
		String inClause = ObjectListToSQL.convertToSQL(status);
		JodaDateTimeSerializer serializer = new JodaDateTimeSerializer();
		From subQuery = new Select("PaymentDate, SUM(Amount) TotalAmount, COUNT(1) TotalCount")
	    .from(CollectionPayment.class);
		
		if(status.length>0)
			subQuery.where("Status IN "+inClause);
		
	    subQuery.where("CashDeskNumber = ?", cashDeskNum)
		.where("PaymentDate >= ?", serializer.serialize(startDate.withTimeAtStartOfDay()))
		.where("PaymentDate <= ?", serializer.serialize(endDate.withTime(23, 59, 59, 999)))
		.groupBy("date(PaymentDate/1000, 'unixepoch')");
		
		List<CashDeskResume> cashDeskResumes = new ArrayList<CashDeskResume>();
		Cursor cursor = Cache.openDatabase().rawQuery(subQuery.toSql(), subQuery.getArguments());
		if(cursor!=null && cursor.moveToFirst())
		{
			do{ 	
				cashDeskResumes.add(new CashDeskResume(
						concept,
						serializer.deserialize(cursor.getLong(cursor.getColumnIndex("PaymentDate"))).withTimeAtStartOfDay(), 
						new BigDecimal(cursor.getString(cursor.getColumnIndex("TotalAmount"))), 
						cursor.getInt(cursor.getColumnIndex("TotalCount"))));
			} while(cursor.moveToNext());
		}
		return cashDeskResumes;
	}
	
	/**
	 * Obtiene todos los COBROS pendientes de exportación
	 * @return
	 */
	public static List<CollectionPayment> getExportPendingCollections()
	{
		return new Select().from(CollectionPayment.class)
				.where("ExportStatus = ?", ExportStatus.NOT_EXPORTED.toShort()).execute();
	}
	
	/**
	 * Obtiene la transacción (WScollection) relacionada con este cobro
	 * @return WSCollection
	 */
	public WSCollection getRelatedTransaction()
	{
		if(transactionNumber!=0)
			return WSCollection.load(WSCollection.class, transactionNumber);
		return null;
	}
	
	/**
	 * Obtiene la transacción (WScollection) de anulación relacionada con este cobro
	 * @return WSCollection, NULL si es que no existe una anulación
	 */
	public WSCollection getRelatedAnnulmentTransaction()
	{
		if(annulmentTransacNum!=0)
			return WSCollection.load(WSCollection.class, annulmentTransacNum);
		return null;
	}

	/**
	 * Convierte esta transaccion en la consulta INSERT de Oracle asignandole los numeros 
	 * de transacción remotos respectivos. 
	 * @return INSERT query
	 */
	public String toRemoteInsertSQL()
	{
		return toInsertSQL(getRelatedTransaction().getRemoteTransactionNumber(), 
				annulmentTransacNum==0?
					annulmentTransacNum:
					getRelatedAnnulmentTransaction().getRemoteTransactionNumber());
	}
	
	/**
	 * Convierte esta transaccion en la consulta INSERT de Oracle
	 * @return INSERT query
	 */
	public String toInsertSQL()
	{
		return toInsertSQL(getTransactionNumber(), getAnnulmentTransacNum());
	}
	
	/**
	 * Convierte esta transaccion en la consulta INSERT de Oracle utilizando los
	 * números de transaccion de los parámetros
	 * @param transactionNumber
	 * @param annulmentTransactionNumber , pasar 0 para NULL
	 * @return INSERT query
	 */
	public String toInsertSQL(long transactionNumber, long annulmentTransactionNumber)
	{
		return String.format(Locale.getDefault(), INSERT_QUERY, getId(), getCashDeskNumber(),
				getPaymentDate().toString("dd/MM/yyyy"), getUser(), getReceiptId(), 
				getAmount().toPlainString(), getId(), getStatus(), 
				getPaymentDate().toString("dd/MM/yyyy HH:mm:ss"), 
				(getAnnulmentUser()==null?"NULL":"'"+getAnnulmentUser()+"'"),
				(getAnnulmentDate()==null?"NULL":String.format("TO_DATE('%s', 'dd/mm/yyyy hh24:mi:ss')", 
						getAnnulmentDate().toString("dd/MM/yyyy HH:mm:ss"))),
						transactionNumber, 
				(annulmentTransactionNumber==0?"NULL":""+annulmentTransactionNumber),
				getSupplyId(), getSupplyNumber(), getReceiptNumber(),
				getYear(), getPeriodNumber(), getCashDeskDescription(),
				getAnnulmentReasonId()==null?"NULL":getAnnulmentReasonId().toString(),
				getPaymentDate().toString("dd/MM/yyyy HH:mm:ss"));
	}
	
	/**
	 * Convierte esta transaccion en la consulta UPDATE de Oracle,
	 * se uliliza para obtener la consulta de actualización en caso de anulación
	 * @return UPDATE query
	 */
	public String toUpdateSQL()
	{
		return String.format(Locale.getDefault(), UPDATE_QUERY, getStatus(), 
				(getAnnulmentDate()==null?"NULL":String.format("TO_DATE('%s', 'dd/mm/yyyy hh24:mi:ss')",
						getAnnulmentDate().toString("dd/MM/yyyy HH:mm:ss"))),
				(getAnnulmentUser()==null?"NULL":"'"+getAnnulmentUser()+"'"),
				(getAnnulmentTransacNum()==0?"NULL":""+getAnnulmentTransacNum()),
				getAnnulmentReasonId()==null?"NULL":getAnnulmentReasonId().toString(),
				getId(), getReceiptId(), getYear(), getPeriodNumber());
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
	
	public ExportStatus getExportStatus() {
		return ExportStatus.get(exportStatus);
	}
	@Override
	public void setExportStatus(ExportStatus exportStatus) {
		this.exportStatus = exportStatus.toShort();
	}

	@Override
	public String getRegistryResume() {
		return "COBRO - Control interno: "+getId()+", IDCBTE: "+receiptId;
	}
	
	//#endregion
}
