package com.elfec.cobranza.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
/**
 * De SUMIN_ESTADOS
 * @author drodriguez
 */
@Table(name = "SuppliesStatus")
public class SupplyStatus extends Model {
	/**
	 * FECHA
	 * Fecha medicion
	 */
	@Column(name = "Date", notNull=true)
	private DateTime date;	
	/**
	 * IDLOTE
	 * Identificador del lote, tabla de referencia fac_lotes
	 */
	@Column(name = "BatchId", notNull=true)
	private int batchId;	
	/**
	 * IDSUMINISTRO
	 * Identificador del suministros, tabla de referencia suministros	
	 */
	@Column(name = "SupplyId", notNull=true)
	private int supplyId;	
	/**
	 * IDMEDIDOR
	 * Identificador de medidor, tabla de referencia medidores
	 */
	@Column(name = "MeterId", notNull=true)
	private int meterId;	
	/**
	 * IDCONCEPTO
	 * Identificador del concepto de lectura, tabla de referencia conceptos - 
	 * Ejemplos-Energia: 5 Cargo Fijo, 10 Cargo Variable Energia, 30 Cargo 
	 * Variable  Pico, 31 Cargo Variable Resto, 32 Cargo Variable  en Valle, 
	 * 34 Cargo por Potencia Pico, 35 Cargo por Potencia Resto, 36 Cargo por Potencia  Valle, 
	 * 44 Exceso Potencia Pico, 45  Exceso Potencia Resto, 60 Reactiva, O.S.		
	 */
	@Column(name = "ConceptId", notNull=true, index=true)
	private int conceptId;	
	/**
	 * IDSUBCONCEPTO en Oracle
	 */
	@Column(name = "SubconceptId")
	private int subconceptId;
	/**
	 * LECTURA en Oracle
	 * El valor de la lectura actual de medidor		
	 */
	@Column(name = "Reading")
	private int reading;
	/**
	 * NROTRANSACCION en Oracle
	 */
	@Column(name = "TransactionNumber")
	private int transactionNumber;
	/**
	 * LECTURA_ANT en Oracle
	 * El valor de la lectura anterior de medidor		
	 */
	@Column(name = "LastReading")
	private int lastReading;
	/**
	 * FECHA_ANT en Oracle
	 * Fecha de la ultima lectura anterior			
	 */
	@Column(name = "LastReadingDate")
	private DateTime lastReadingDate;
	/**
	 * CONSUMO en Oracle
	 * Consumo la real teniendo en cuenta el factor de multiplicacion.. Consumo la real teniendo en cuenta el factor de multiplicacion.
	 */
	@Column(name = "Consume")
	private int consume;
	/**
	 * IDRUTA
	 */
	@Column(name = "RouteId")
	private int routeId;
	/**
	 * IDCBTE Identificador unico de Cbtes_Coop
	 */
	@Column(name = "ReceiptId", index=true)
	private int receiptId;
	/**
	 * IDCBTE_SIM Identificador unico de Sim_Cbtes,(Facturacion Provisoria).
	 */
	@Column(name = "SimReceiptId")
	private int simReceiptId;
	/**
	 * CONS_FACTURACION en Oracle
	 * Este campo tendra el mismo valor que el campo 
	 * Consumo si el campo anterior se encuentra en cero, 
	 * se podria decir que su valor por defecto es = consumo, luego puede variar
	 */
	@Column(name = "BilledConsume")
	private int billedConsume;
	/**
	 * DELTA_PEND_COMPEN en Oracle
	 * Este campo dependiendo de su valor, tanto negativo como positivo, se sumara al campo Cons_Facturacion		
	 */
	@Column(name = "DeltaOutstandingConsume")
	private int deltaOutstandingConsume;
	
	public SupplyStatus() {
		super();
	}
	
	
	
	public SupplyStatus(DateTime date, int batchId, int supplyId, int meterId,
			int conceptId, int subconceptId, int reading,
			int transactionNumber, int lastReading, DateTime lastReadingDate,
			int consume, int routeId, int receiptId, int simReceiptId,
			int billedConsume, int deltaOutstandingConsume) {
		super();
		this.date = date;
		this.batchId = batchId;
		this.supplyId = supplyId;
		this.meterId = meterId;
		this.conceptId = conceptId;
		this.subconceptId = subconceptId;
		this.reading = reading;
		this.transactionNumber = transactionNumber;
		this.lastReading = lastReading;
		this.lastReadingDate = lastReadingDate;
		this.consume = consume;
		this.routeId = routeId;
		this.receiptId = receiptId;
		this.simReceiptId = simReceiptId;
		this.billedConsume = billedConsume;
		this.deltaOutstandingConsume = deltaOutstandingConsume;
	}
	
	/**
	 * Elimina todos los sumin estados que se encuentren en la lista de facturas provista
	 * @param supplyIdsString lista de facturas en forma de clausula IN
	 */
	public static void cleanSupplyStatuses(String coopReceiptIdsString)
	{
		new Delete().from(SupplyStatus.class).where("ReceiptId IN "+coopReceiptIdsString).execute();
	}


	//#region Getters y Setters
	
	
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	public int getSupplyId() {
		return supplyId;
	}
	public void setSupplyId(int supplyId) {
		this.supplyId = supplyId;
	}
	public int getMeterId() {
		return meterId;
	}
	public void setMeterId(int meterId) {
		this.meterId = meterId;
	}
	public int getConceptId() {
		return conceptId;
	}
	public void setConceptId(int conceptId) {
		this.conceptId = conceptId;
	}
	public int getSubconceptId() {
		return subconceptId;
	}
	public void setSubconceptId(int subconceptId) {
		this.subconceptId = subconceptId;
	}
	public int getReading() {
		return reading;
	}
	public void setReading(int reading) {
		this.reading = reading;
	}
	public int getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public int getLastReading() {
		return lastReading;
	}
	public void setLastReading(int lastReading) {
		this.lastReading = lastReading;
	}
	public DateTime getLastReadingDate() {
		return lastReadingDate;
	}
	public void setLastReadingDate(DateTime lastReadingDate) {
		this.lastReadingDate = lastReadingDate;
	}
	public int getConsume() {
		return consume;
	}
	public void setConsume(int consume) {
		this.consume = consume;
	}
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public int getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}
	public int getSimReceiptId() {
		return simReceiptId;
	}
	public void setSimReceiptId(int simReceiptId) {
		this.simReceiptId = simReceiptId;
	}
	public int getBilledConsume() {
		return billedConsume;
	}
	public void setBilledConsume(int billedConsume) {
		this.billedConsume = billedConsume;
	}
	public int getDeltaOutstandingConsume() {
		return deltaOutstandingConsume;
	}
	public void setDeltaOutstandingConsume(int deltaOutstandingConsume) {
		this.deltaOutstandingConsume = deltaOutstandingConsume;
	}
	
	//#endregion
	
}
