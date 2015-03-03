package com.elfec.cobranza.model;

import java.math.BigDecimal;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
/**
 * Almacena los CBTES_CPTOS
 * @author drodriguez
 *
 */
public class ReceiptConcept extends Model{
	/**
	 * IDEMPRESA en Oracle
	 */
	@Column(name = "EnterpriseId", notNull=true)
	private int enterpriseId;
	/**
	 * IDSUCURSAL en Oracle
	 */
	@Column(name = "BranchOfficeId", notNull=true)
	private int branchOfficeId;
	/**
	 * TIPO_CBTE en Oracle
	 */
	@Column(name = "ReceiptType", notNull=true)
	private String receiptType;
	/**
	 * GRUPO_CBTE en Oracle
	 */
	@Column(name = "ReceiptGroup", notNull=true)
	private int receiptGroup;
	/**
	 * LETRA_CBTE en Oracle
	 */
	@Column(name = "ReceiptLetter", notNull=true)
	private String receiptLetter;
	/**
	 * NROCBTE en Oracle
	 */
	@Column(name = "ReceiptNumber", notNull=true)
	private int receiptNumber;
	/**
	 * IDMEDIDOR
	 * Identificador de medidor, tabla de referencia medidores
	 */
	@Column(name = "MeterId")
	private int meterId;	
	/**
	 * IDCONCEPTO en Oracle
	 */
	@Column(name = "ConceptId")
	private int conceptId;	
	/**
	 * IDSUBCONCEPTO en Oracle
	 */
	@Column(name = "SubconceptId")
	private int subconceptId;
	/**
	 * IMPORTE en Oracle
	 */
	@Column(name = "Amount")
	private BigDecimal amount;		
	/**
	 * ANIO en Oracle
	 */
	@Column(name = "Year")
	private int Year;
	/**
	 * NROPER en Oracle
	 */
	@Column(name = "PeriodNumber")
	private int periodNumber;
	/**
	 * LECTURA_ANTERIOR en Oracle
	 * El valor de la lectura anterior de medidor		
	 */
	@Column(name = "LastReading")
	private int lastReading;
	/**
	 * LECTURA_ACTUAL en Oracle
	 * El valor de la lectura actual de medidor		
	 */
	@Column(name = "CurrentReading")
	private int currentReading;
	/**
	 * DESCRIPCION en Oracle
	 */
	@Column(name = "Description")
	private String description;
	/**
	 * IDCATEGORIA en Oracle
	 */
	@Column(name = "CategoryId")
	private String categoryId;	
	/**
	 * IDCBTE Identificador unico de comprobantes
	 */
	@Column(name = "ReceiptId")
	private int receiptId;
	
	//#region Getters y Setters
	
	public int getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public int getBranchOfficeId() {
		return branchOfficeId;
	}
	public void setBranchOfficeId(int branchOfficeId) {
		this.branchOfficeId = branchOfficeId;
	}
	public String getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
	public int getReceiptGroup() {
		return receiptGroup;
	}
	public void setReceiptGroup(int receiptGroup) {
		this.receiptGroup = receiptGroup;
	}
	public String getReceiptLetter() {
		return receiptLetter;
	}
	public void setReceiptLetter(String receiptLetter) {
		this.receiptLetter = receiptLetter;
	}
	public int getReceiptNumber() {
		return receiptNumber;
	}
	public void setReceiptNumber(int receiptNumber) {
		this.receiptNumber = receiptNumber;
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getYear() {
		return Year;
	}
	public void setYear(int year) {
		Year = year;
	}
	public int getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}
	public int getLastReading() {
		return lastReading;
	}
	public void setLastReading(int lastReading) {
		this.lastReading = lastReading;
	}
	public int getCurrentReading() {
		return currentReading;
	}
	public void setCurrentReading(int currentReading) {
		this.currentReading = currentReading;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public int getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}
	
	//#endregion
	
}
