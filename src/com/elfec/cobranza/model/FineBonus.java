package com.elfec.cobranza.model;

import java.math.BigDecimal;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;

/**
 * Almacena las BONIF_MULTAS y BONIF_MULTAS_IT
 * @author drodriguez
 *
 */
@Table(name = "FineBonuses")
public class FineBonus extends Model {
	/**
	 * IDCBTE Identificador unico de comprobantes
	 */
	@Column(name = "ReceiptId", notNull=true, index=true)
	private int receiptId;
	/**
	 * IDCONCEPTO en Oracle
	 */
	@Column(name = "ConceptId")
	private int conceptId;	
	/**
	 * DESCRIPCION en Oracle
	 */
	@Column(name = "Description")
	private String description;
	/**
	 * IMPORTE en Oracle
	 */
	@Column(name = "Amount")
	private BigDecimal amount;	
	/**
	 * IMPRESION_AREA en Oracle
	 * Area de impresión
	 */
	@Column(name = "PrintArea")
	private short printArea;
	
	public FineBonus() {
		super();
	}
	
	public FineBonus(int receiptId, int conceptId, String description,
			BigDecimal amount, short printArea) {
		super();
		this.receiptId = receiptId;
		this.conceptId = conceptId;
		this.description = description;
		this.amount = amount;
		this.printArea = printArea;
	}
	
	/**
	 * Elimina todos las BONIF_MULTAS y BONIF_MULTAS_IT que se encuentren en la lista de facturas provista
	 * @param coopReceiptIdsString lista de facturas en forma de clausula IN
	 */
	public static void cleanFineBonuses(String coopReceiptIdsString)
	{
		new Delete().from(FineBonus.class).where("ReceiptId IN "+coopReceiptIdsString).execute();
	}

	//#region Getters y Setters
	
	public int getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}
	public int getConceptId() {
		return conceptId;
	}
	public void setConceptId(int conceptId) {
		this.conceptId = conceptId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public short getPrintArea() {
		return printArea;
	}
	public void setPrintArea(short printArea) {
		this.printArea = printArea;
	}
	
	//#endregion
}
