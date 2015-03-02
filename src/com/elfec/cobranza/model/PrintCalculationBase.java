package com.elfec.cobranza.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Almacena los GBASES_CALC_IMP
 * @author drodriguez
 *
 */
@Table(name = "PrintCalculationBases")
public class PrintCalculationBase extends Model {
	/**
	 * IDBASE_CALCULO en Oracle
	 */
	@Column(name = "ClaculationBaseId", notNull=true)
	private int claculationBaseId;		
	/**
	 * ORDEN_IMPRESION en Oracle
	 */
	@Column(name = "PrintOrder")
	private short printOrder;	
	/**
	 * DESCRIPCION en Oracle
	 */
	@Column(name = "Description")
	private String description;
	
	//#region Getters y Setters
	public int getClaculationBaseId() {
		return claculationBaseId;
	}
	public void setClaculationBaseId(int claculationBaseId) {
		this.claculationBaseId = claculationBaseId;
	}
	public short getPrintOrder() {
		return printOrder;
	}
	public void setPrintOrder(short printOrder) {
		this.printOrder = printOrder;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	//#endregion
}
