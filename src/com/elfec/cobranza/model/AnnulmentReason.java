package com.elfec.cobranza.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
/**
 * Almacena la información de la tabla COBRANZA.MOTIVOS_ANULACION
 * @author drodriguez
 *
 */
import com.activeandroid.annotation.Table;
@Table(name = "AnnulmentReasons")
public class AnnulmentReason extends Model {
	/**
	 * IDMOTIVO_ANULA en Oracle
	 */
	@Column(name = "AnnulmentReasonRemoteId", notNull=true)
	private int annulmentReasonRemoteId;
	/**
	 * DESCRIPCION en Oracle
	 */
	@Column(name = "Description")
	private String description;
	
	public AnnulmentReason() {
		super();
	}

	public AnnulmentReason(int annulmentReasonRemoteId, String description) {
		super();
		this.annulmentReasonRemoteId = annulmentReasonRemoteId;
		this.description = description;
	}

	public int getAnnulmentReasonRemoteId() {
		return annulmentReasonRemoteId;
	}

	public void setAnnulmentReasonRemoteId(int annulmentReasonRemoteId) {
		this.annulmentReasonRemoteId = annulmentReasonRemoteId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
		
}
