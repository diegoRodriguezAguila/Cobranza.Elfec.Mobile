package com.elfec.cobranza.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
/**
 * Almacena los GBASES_CALC_CPTOS
 * @author drodriguez
 *
 */
@Table(name = "ConceptCalculationBases")
public class ConceptCalculationBase extends Model {
	/**
	 * IDBASE_CALCULO en Oracle
	 */
	@Column(name = "ClaculationBaseId", notNull=true)
	private int claculationBaseId;	
	/**
	 * IDCONCEPTO en Oracle
	 */
	@Column(name = "ConceptId", notNull=true)
	private int conceptId;	
	/**
	 * IDSUBCONCEPTO en Oracle
	 */
	@Column(name = "SubconceptId")
	private int subconceptId;
	
	public ConceptCalculationBase() {
		super();
	}
	
	public ConceptCalculationBase(int claculationBaseId, int conceptId,
			int subconceptId) {
		super();
		this.claculationBaseId = claculationBaseId;
		this.conceptId = conceptId;
		this.subconceptId = subconceptId;
	}



	//#region Getters y Setters
	public int getClaculationBaseId() {
		return claculationBaseId;
	}
	public void setClaculationBaseId(int claculationBaseId) {
		this.claculationBaseId = claculationBaseId;
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
	
	//#endregion
}
