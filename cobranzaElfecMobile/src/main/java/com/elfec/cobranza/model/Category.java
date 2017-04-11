package com.elfec.cobranza.model;

import java.math.BigDecimal;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
/**
 * Almacena las CATEGORIAS
 * @author drodriguez
 *
 */
@Table(name = "Categories")
public class Category extends Model {
	/**
	 * IDCATEGORIA en Oracle
	 */
	@Column(name = "CategoryId", notNull=true, index=true)
	private String categoryId;	
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
	 * IDTIPO_CATEG en Oracle
	 */
	@Column(name = "CategoryTypeId", notNull=true, index=true)
	private int categoryTypeId;		
	/**
	 * CTROL_IVAS en Oracle
	 * Tipos de ivas que pueden tener los suministros de esta Categoria. 
	 * Ejemplo: `(1,2,3)? donde 1 2 y 3 son distintos tipos de iva.
	 */
	@Column(name = "IVAsControl")
	private String IVAsControl;		
	/**
	 * IDSTATUS en Oracle
	 */
	@Column(name = "StatusId")
	private short statusId;		
	/**
	 * CLASIF en Oracle
	 * Clasificación de la categoría
	 */
	@Column(name = "Classification")
	private String classification;	
	/**
	 * CTROL_TMEDID en Oracle
	 * Control de Tipos de Medidores 
	 */
	@Column(name = "MeterTypeControl")
	private String meterTypeControl;	
	/**
	 * CLASIF2 en Oracle
	 * MultiUso, creado por la exportacion de la res. 1434
	 */
	@Column(name = "Classification2")
	private String classification2;	
	/**
	 * FACTOR_CARGA en Oracle
	 */
	@Column(name = "LoadFactor")
	private BigDecimal loadFactor;	
	/**
	 * FACTURAR_DEM en Oracle
	 * facturar demanda
	 */
	@Column(name = "BillingDemand")
	private short billingDemand;
	
	public Category() {
		super();
	}
	
	public Category(String categoryId, int serviceTypeId, String description,
			int categoryTypeId, String IVAsControl, short statusId,
			String classification, String meterTypeControl,
			String classification2, BigDecimal loadFactor, short billingDemand) {
		super();
		this.categoryId = categoryId;
		this.serviceTypeId = serviceTypeId;
		this.description = description;
		this.categoryTypeId = categoryTypeId;
		this.IVAsControl = IVAsControl;
		this.statusId = statusId;
		this.classification = classification;
		this.meterTypeControl = meterTypeControl;
		this.classification2 = classification2;
		this.loadFactor = loadFactor;
		this.billingDemand = billingDemand;
	}
	
	/**
	 * Obtiene la cadena para mostrar de la categoría
	 * @param categoryId
	 * @return la cadena en formato DOMICILIARIA/PDBTR1
	 */
	public static String getFullCategoryDesc(String categoryId)
	{
		From query = new Select("trim(b.Description)||'/'||trim(a.Classification2) FullCategoryDesc")
	    .from(Category.class).as("a")
	    .innerJoin(SupplyCategoryType.class).as("b")
	    .on("a.CategoryTypeId = b.CategoryTypeId")
	    .where("a.CategoryId = ?", categoryId);

		Cursor cursor = Cache.openDatabase().rawQuery(query.toSql(), query.getArguments());
		if(cursor!=null)
		{
			cursor.moveToFirst();
			return cursor.getString(0);
		}
		return categoryId;//retorna el mismo id como categoría en caso de no encontrarse
	}

	//#region Getters y Setters
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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
	public int getCategoryTypeId() {
		return categoryTypeId;
	}
	public void setCategoryTypeId(int categoryTypeId) {
		this.categoryTypeId = categoryTypeId;
	}
	public String getIVAsControl() {
		return IVAsControl;
	}
	public void setIVAsControl(String iVAsControl) {
		IVAsControl = iVAsControl;
	}
	public short getStatusId() {
		return statusId;
	}
	public void setStatusId(short statusId) {
		this.statusId = statusId;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getMeterTypeControl() {
		return meterTypeControl;
	}
	public void setMeterTypeControl(String meterTypeControl) {
		this.meterTypeControl = meterTypeControl;
	}
	public String getClassification2() {
		return classification2;
	}
	public void setClassification2(String classification2) {
		this.classification2 = classification2;
	}
	public BigDecimal getLoadFactor() {
		return loadFactor;
	}
	public void setLoadFactor(BigDecimal loadFactor) {
		this.loadFactor = loadFactor;
	}
	public short getBillingDemand() {
		return billingDemand;
	}
	public void setBillingDemand(short billingDemand) {
		this.billingDemand = billingDemand;
	}	
	
	//#endregion
	
}
