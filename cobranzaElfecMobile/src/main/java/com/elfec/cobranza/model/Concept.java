package com.elfec.cobranza.model;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.elfec.cobranza.model.printer.PrintConcept;
import com.elfec.cobranza.model.printer.PrintConceptQuerier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Almacena los CONCEPTOS
 * @author drodriguez
 *
 */
@Table(name = "Concepts")
public class Concept extends Model {
	/**
	 * IDCONCEPTO en Oracle
	 */
	@Column(name = "ConceptId", notNull=true, index=true)
	private int conceptId;	
	/**
	 * IDSUBCONCEPTO en Oracle
	 */
	@Column(name = "SubconceptId", notNull=true)
	private int subconceptId;
	/**
	 * IDTIPO_SRV en Oracle
	 */
	@Column(name = "ServiceTypeId")
	private int serviceTypeId;
	/**
	 * DESCRIPCION en Oracle
	 */
	@Column(name = "Description")
	private String description;
	/**
	 * CODCONCEPTO en Oracle
	 * Codigo del concepto
	 */
	@Column(name = "ConceptCode")
	private String conceptCode;	
	/**
	 * IDGRUPO en Oracle
	 * Identificador del grupo, tabla grupos
	 */
	@Column(name = "GroupId")
	private int groupId;	
	/**
	 * IVA_TIPO_SRV en Oracle
	 * Iva aplicado al tipo de servicio			
	 */
	@Column(name = "IVAServiceType")
	private int IVAserviceType;
	/**
	 * EXCLUIR_LIVA en Oracle
	 * Indica si excluye el iva para el concepto, 1 Si 0 No
	 */
	@Column(name = "ExcludeIVA")
	private int excludeIVA;
	/**
	 * IVA_COLUMNA en Oracle
	 * Columna del iva, valor 0 por defecto
	 */
	@Column(name = "IVAColumn")
	private int IVAColumn;
	/**
	 * IMPRESION_AREA en Oracle
	 * Area de impresión
	 */
	@Column(name = "PrintArea")
	private short printArea;
	/**
	 * TIPO_PART en Oracle
	 * Tipo de partida
	 */
	@Column(name = "PartType")
	private short partType;
	/**
	 * INCLUYA_UNIDADES	en Oracle
	 * 1 Si 0 No
	 */
	@Column(name = "IncludeUnits")
	private short includeUnits;
	
	public Concept() {
		super();
	}
	
	public Concept(int conceptId, int subconceptId, int serviceTypeId,
			String description, String conceptCode, int groupId,
			int iVAserviceType, int excludeIVA, int iVAColumn, short printArea,
			short partType, short includeUnits) {
		super();
		this.conceptId = conceptId;
		this.subconceptId = subconceptId;
		this.serviceTypeId = serviceTypeId;
		this.description = description;
		this.conceptCode = conceptCode;
		this.groupId = groupId;
		IVAserviceType = iVAserviceType;
		this.excludeIVA = excludeIVA;
		IVAColumn = iVAColumn;
		this.printArea = printArea;
		this.partType = partType;
		this.includeUnits = includeUnits;
	}
	
	/**
	 * Obtiene el concepto de consumo total para impresión
	 * @param receiptId
	 * @return concepto TOTAL CONSUMO
	 */
	public static PrintConcept getTotalConsumeConcept(int receiptId)
	{
		return (new PrintConceptQuerier(receiptId, 1)
				.setDescription("TOTAL CONSUMO").setGroupBy("PrintArea").execute()).get(0);
	}
	
	/**
	 * Obtiene los conceptos del consumo total para impresión
	 * @param receiptId
	 * @return Lista de conceptos del área de TOTAL CONSUMO
	 */
	public static List<PrintConcept> getTotalConsumeAreaConcepts(int receiptId)
	{
		return (new PrintConceptQuerier(receiptId, 2).setGroupBy("PrintArea, Description").execute());
	}
	
	/**
	 * Obtiene el concepto de total suministro para impresión
	 * @param receiptId
	 * @return concepto TOTAL SUMINISTRO
	 */
	public static PrintConcept getTotalSupplyConcept(int receiptId)
	{
		return (new PrintConceptQuerier(receiptId, 1, 2).setDescription("TOTAL SUMINISTRO").execute()).get(0);
	}
	
	/**
	 * Obtiene los conceptos del consumo total para impresión
	 * @param receiptId
	 * @return Lista de conceptos del área de TOTAL SUMINISTRO
	 */
	public static List<PrintConcept> getTotalSupplyAreaConcepts(int receiptId)
	{
		return (new PrintConceptQuerier(receiptId, 3).setGroupBy("PrintArea, Description").execute());
	}
	
	/**
	 * Obtiene el concepto del total de la factura para impresión
	 * @param receiptId
	 * @return Concepto de TOTAL FACTURA
	 */
	public static PrintConcept getTotalReceiptConcept(int receiptId)
	{
		return (new PrintConceptQuerier(receiptId, 1, 2, 3).setDescription("TOTAL FACTURA").execute()).get(0);
	}
	
	/**
	 * Obtiene el concepto de IMPORTE BASE PARA CREDITO FISCAL
	 * @param receiptId
	 * @return concepto IMPORTE BASE PARA CREDITO FISCAL
	 */
	public static PrintConcept getSubjectToTaxCreditConcept(int receiptId)
	{
		return (new PrintConceptQuerier(receiptId, 1, 2).setDescription("IMPORTE BASE PARA CRÉDITO FISCAL").execute()).get(0);
	}
	
	/**
	 * Obtiene los conceptos de devoluciones y bonificaciones de multas
	 * @param receiptId
	 * @return Lista de conceptos del área de Bonificaciones y devoluciones
	 */
	public static List<PrintConcept> getFineBonusConcepts(int receiptId)
	{
		From subQuery = new Select("Description, Amount")
	    .from(FineBonus.class)
	    .where("ReceiptId = ? ", receiptId);
		
		List<PrintConcept> concepts = new ArrayList<PrintConcept>();
		Cursor cursor = Cache.openDatabase().rawQuery(subQuery.toSql(), subQuery.getArguments());
		if(cursor!=null && cursor.moveToFirst())
		{
			do{ 	
				concepts.add(new PrintConcept(cursor.getString(cursor.getColumnIndex("Description")), 
						new BigDecimal(cursor.getString(cursor.getColumnIndex("Amount")))));			
			} while(cursor.moveToNext());
		}
		return concepts;
	}
	
	/**
	 * Obtiene el concepto de IMPORTE NO SUJETO A CRÉDITO FISCAL
	 * @param receiptId
	 * @return concepto IMPORTE NO SUJETO A CRÉDITO FISCAL
	 */
	public static PrintConcept getNotSubjectToTaxCreditConcept(int receiptId)
	{
		return (new PrintConceptQuerier(receiptId, 3).setDescription("IMPORTE NO SUJETO A CREDITO FISCAL").execute()).get(0);
	}

	//#region Getters y Setters
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
	public int getServiceTypeId() {
		return serviceTypeId;
	}
	public void setServiceTypeId(int serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getConceptCode() {
		return conceptCode;
	}
	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getIVAserviceType() {
		return IVAserviceType;
	}
	public void setIVAserviceType(int iVAserviceType) {
		IVAserviceType = iVAserviceType;
	}
	public int getExcludeIVA() {
		return excludeIVA;
	}
	public void setExcludeIVA(int excludeIVA) {
		this.excludeIVA = excludeIVA;
	}
	public int getIVAColumn() {
		return IVAColumn;
	}
	public void setIVAColumn(int iVAColumn) {
		IVAColumn = iVAColumn;
	}
	public short getPrintArea() {
		return printArea;
	}
	public void setPrintArea(short printArea) {
		this.printArea = printArea;
	}
	public short getPartType() {
		return partType;
	}
	public void setPartType(short partType) {
		this.partType = partType;
	}
	public short getIncludeUnits() {
		return includeUnits;
	}
	public void setIncludeUnits(short includeUnits) {
		this.includeUnits = includeUnits;
	}
	//#endregion
	
}
