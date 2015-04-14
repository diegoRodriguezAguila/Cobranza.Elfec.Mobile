package com.elfec.cobranza.model;

import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.elfec.cobranza.model.enums.ExportStatus;
import com.elfec.cobranza.model.interfaces.IExportable;

/**
 * Contiene los datos de la tabla COB_WS
 * @author drodriguez
 *
 */
@Table(name = "WSCollections")
public class WSCollection extends Model implements IExportable{
	public static final String INSERT_QUERY = "INSERT INTO ERP_ELFEC.COB_WS VALUES (%d, %d, 'T:%s', %d, '%s', %d, %d, %d, "
			+ "NULL, NULL, NULL, NULL, TO_DATE('%s', 'dd/mm/yyyy'))";
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
	
	//ATRIBUTO EXTRA
	@Column(name = "ExportStatus", notNull=true, index=true)
	private short exportStatus;
	
	public WSCollection() {
		super();
	}

	public WSCollection(String action, int receiptId, String status, int bankId,
			int bankAccountId, int periodNumber, DateTime paymentDate, ExportStatus exportStatus) {
		super();
		this.action = action;
		this.receiptId = receiptId;
		this.status = status;
		this.bankId = bankId;
		this.bankAccountId = bankAccountId;
		this.periodNumber = periodNumber;
		this.paymentDate = paymentDate;
		this.exportStatus = exportStatus.toShort();
	}
	
	/**
	 * Convierte esta transaccion en la consulta INSERT de Oracle
	 * @return INSERT query
	 */
	public String toInsertSQL()
	{
		return String.format(Locale.getDefault(), INSERT_QUERY, getId(), getId(),
				getAction(), getReceiptId(), getStatus(), 
				getBankId(), getBankAccountId(), getPeriodNumber(),
				getPaymentDate().toString("dd/MM/yyyy"));
	}
	
	/**
	 * Obtiene todos los COB_WS pendientes de exportación
	 * @return
	 */
	public static List<WSCollection> getExportPendingWSCollections()
	{
		return new Select().from(WSCollection.class)
				.where("ExportStatus = ?", ExportStatus.NOT_EXPORTED.toShort()).execute();
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
	
	public ExportStatus getExportStatus() {
		return ExportStatus.get(exportStatus);
	}
	
	@Override
	public void setExportStatus(ExportStatus exportStatus) {
		this.exportStatus = exportStatus.toShort();
	}

	@Override
	public String getRegistryResume() {
		return "COB_WS - Transación No.: "+getId()+", IDCBTE: "+receiptId;
	}
	
	//#endregion
}
