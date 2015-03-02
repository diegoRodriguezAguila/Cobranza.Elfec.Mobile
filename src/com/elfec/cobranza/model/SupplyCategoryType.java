package com.elfec.cobranza.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Almacena los TIPOS_CATEG_SUM
 * @author drodriguez
 *
 */
@Table(name = "SupplyCategoryTypes")
public class SupplyCategoryType extends Model {
	/**
	 * IDTIPO_CATEG en Oracle
	 */
	@Column(name = "CategoryTypeId", notNull=true)
	private int categoryTypeId;	
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
	 * DESC_CORTA en Oracle
	 */
	@Column(name = "ShortDescription")
	private String shortDescription;

	public SupplyCategoryType() {
		super();
	}
	
	public SupplyCategoryType(int categoryTypeId, int serviceTypeId,
			String description, String shortDescription) {
		super();
		this.categoryTypeId = categoryTypeId;
		this.serviceTypeId = serviceTypeId;
		this.description = description;
		this.shortDescription = shortDescription;
	}

	//#region Getters y Setters
	
	public int getCategoryTypeId() {
		return categoryTypeId;
	}

	public void setCategoryTypeId(int categoryTypeId) {
		this.categoryTypeId = categoryTypeId;
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

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	//#endregion
}
