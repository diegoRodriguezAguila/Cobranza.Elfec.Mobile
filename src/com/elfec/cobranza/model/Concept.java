package com.elfec.cobranza.model;

import java.math.BigDecimal;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.elfec.cobranza.model.printer.PrintConcept;

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
	@Column(name = "ConceptId", notNull=true)
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
	public static PrintConcept getTotalConsumeConcepts(int receiptId)
	{
		String query = "SELECT 'TOTAL CONSUMO' Description, SUM(Amount) Amount FROM (";
		String groupBy = ") GROUP BY PrintArea ";
		From subQuery = new Select("A.ReceiptId,A.EnterpriseId, A.BranchOfficeId, A.ReceiptType, A.ReceiptGroup, A.ReceiptLetter,"
				+ "A.ReceiptNumber, A.ConceptId, A.Description Desc, A.Amount, B.PrintArea, C.ClaculationBaseId, D.Description")
	    .from(ReceiptConcept.class).as("A")
	    .join(Concept.class).as("B")
	    .on("(A.ConceptId = B.ConceptId AND A.SubconceptId = B.SubconceptId)")
	    .join(ConceptCalculationBase.class).as("C")
	    .on("(A.ConceptId = C.ConceptId AND A.SubconceptId = C.SubconceptId)")
	    .join(PrintCalculationBase.class).as("D")
	    .on(" C.ClaculationBaseId = D.ClaculationBaseId")
	    .where(" (A.EnterpriseId = 1) AND (A.BranchOfficeId = 10) "
	    		+ "AND (A.ReceiptType = 'FC') AND (A.ReceiptLetter = 'Y') "
	    		+ "AND (B.PrintArea IN (1)) "
	    		+ "AND A.ReceiptId = ? "
	    		+ "AND C.ClaculationBaseId>=100 "
	    		+ "AND A.Amount<>0", receiptId);

		Cursor cursor = Cache.openDatabase().rawQuery(query+subQuery.toSql()+groupBy, subQuery.getArguments());
		if(cursor!=null)
		{
			cursor.moveToFirst();
			return new PrintConcept(cursor.getString(0), new BigDecimal(cursor.getString(1)));
		}
		return null;
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
