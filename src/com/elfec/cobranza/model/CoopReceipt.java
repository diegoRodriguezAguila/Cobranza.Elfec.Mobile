package com.elfec.cobranza.model;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
/**
 * Almacena los CBTES_COOP
 * @author drodriguez
 *
 */
public class CoopReceipt extends Model {
	/**
	 * IDCBTE Identificador unico de comprobantes
	 */
	@Column(name = "ReceiptId", notNull=true)
	private int receiptId;
	/**
	 * IDSUMINISTRO
	 * Identificador del suministros, tabla de referencia suministros	
	 */
	@Column(name = "SupplyId")
	private int supplyId;	
	/**
	 * IDCLIENTE en Oracle
	 */
	@Column(name = "ClientId")
	private int clientId;
	/**
	 * IDEMPRESA en Oracle
	 */
	@Column(name = "EnterpriseId")
	private int enterpriseId;
	/**
	 * IDSUCURSAL en Oracle
	 */
	@Column(name = "BranchOfficeId")
	private int branchOfficeId;
	/**
	 * TIPO_CBTE en Oracle
	 */
	@Column(name = "ReceiptType")
	private String receiptType;
	/**
	 * GRUPO_CBTE en Oracle
	 */
	@Column(name = "ReceiptGroup")
	private int receiptGroup;
	/**
	 * LETRA_CBTE en Oracle
	 */
	@Column(name = "ReceiptLetter")
	private String receiptLetter;
	/**
	 * NROCBTE en Oracle
	 */
	@Column(name = "ReceiptNumber")
	private int receiptNumber;
	/**
	 * FECHA_EMISION en Oracle
	 */
	@Column(name = "IssueDate")
	private DateTime issueDate;
	/**
	 * FECHA_VTO_ORIGINAL en Oracle
	 */
	@Column(name = "OrigExpirationDate")
	private DateTime origExpirationDate;
	/**
	 * FECHA_VTO en Oracle
	 */
	@Column(name = "ExpirationDate")
	private DateTime expirationDate;
	/**
	 * FECHA_INICIO en Oracle
	 */
	@Column(name = "StartDate")
	private DateTime startDate;
	/**
	 * FECHA_FIN en Oracle
	 */
	@Column(name = "EndDate")
	private DateTime endDate;
	/**
	 * ANIO en Oracle
	 */
	@Column(name = "Year")
	private int year;
	/**
	 * NROPER en Oracle
	 */
	@Column(name = "PeriodNumber")
	private int periodNumber;
	/**
	 * NROSUM en Oracle
	 */
	@Column(name = "SupplyNumber")
	private int supplyNumber;
	/**
	 * IDRUTA en Oracle
	 */
	@Column(name = "RouteId")
	private int routeId;
	/**
	 * NOMBRE en Oracle
	 */
	@Column(name = "Name")
	private String name;
	/**
	 * IDIVA en Oracle
	 */
	@Column(name = "IVAId")
	private int IVAId;	
	/**
	 * CUIT en Oracle
	 */
	@Column(name = "NIT")
	private String NIT;
	/**
	 * DOMICILIO_SUM en Oracle
	 */
	@Column(name = "SupplyAddress")
	private String supplyAddress;
	/**
	 * IDCATEGORIA en Oracle
	 */
	@Column(name = "CategoryId")
	private String categoryId;	
	/**
	 * SRV_IMPORTE en Oracle
	 */
	@Column(name = "ServiceAmount")
	private BigDecimal serviceAmount;	
	/**
	 * SRV_SALDO en Oracle
	 */
	@Column(name = "ServiceBalance")
	private BigDecimal serviceBalance;	
	/**
	 * TOTALIMP en Oracle
	 */
	@Column(name = "TotalAmount")
	private BigDecimal totalAmount;		
	/**
	 * ESTADO en Oracle
	 */
	@Column(name = "Status")
	private String status;	
	/**
	 * IDLOTE
	 * Identificador del lote, tabla de referencia fac_lotes
	 */
	@Column(name = "BatchId")
	private int batchId;	
	/**
	 * NRO_AUT_IMPRESION
	 */
	@Column(name = "AuthorizationNumber")
	private String authorizationNumber;
	/**
	 * FECHA_VTO_AUT
	 */
	@Column(name = "AuthExpirationDate")
	private DateTime authExpirationDate;
	/**
	 * COD_CONTROL
	 */
	@Column(name = "ControlCode")
	private String controlCode;
	
	//ATRIBUTOS OBTENIDOS CON FUNCIONES
	/**
	 * COBRANZA.FLITERAL(TOTALIMP) LITERAL en Oracle
	 */
	@Column(name = "Literal")
	private String literal;	
	/**
	 * DIRECCION sacada de funcion MOVILES.FCOBRA_OBTENER_DIRECCION(IDSUMINISTRO)
	 */
	@Column(name = "ClientAddress")
	private String clientAddress;
	
	/**
	 * MEDIDOR sacada de funcion MOVILES.FCOBRA_OBTENER_MEDIDOR(IDCBTE)
	 */
	@Column(name = "MeterNumber")
	private String meterNumber;
	
	/**
	 * DESC_AUT_IMPRESION sacada de funcion MOVILES.FCOBRA_OBTENER_DESC_AUT(NRO_AUT_IMPRESION)
	 */
	@Column(name = "AuthorizationDescription")
	private String authorizationDescription;
	
	public CoopReceipt() {
		super();
	}
	
	public CoopReceipt(int receiptId, int supplyId, int clientId,
			int enterpriseId, int branchOfficeId, String receiptType,
			int receiptGroup, String receiptLetter, int receiptNumber,
			DateTime issueDate, DateTime origExpirationDate,
			DateTime expirationDate, DateTime startDate, DateTime endDate,
			int year, int periodNumber, int supplyNumber, int routeId,
			String name, int iVAId, String nIT, String supplyAddress,
			String categoryId, BigDecimal serviceAmount,
			BigDecimal serviceBalance, BigDecimal totalAmount, String status,
			int batchId, String authorizationNumber,
			DateTime authExpirationDate, String controlCode) {
		super();
		this.receiptId = receiptId;
		this.supplyId = supplyId;
		this.clientId = clientId;
		this.enterpriseId = enterpriseId;
		this.branchOfficeId = branchOfficeId;
		this.receiptType = receiptType;
		this.receiptGroup = receiptGroup;
		this.receiptLetter = receiptLetter;
		this.receiptNumber = receiptNumber;
		this.issueDate = issueDate;
		this.origExpirationDate = origExpirationDate;
		this.expirationDate = expirationDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.year = year;
		this.periodNumber = periodNumber;
		this.supplyNumber = supplyNumber;
		this.routeId = routeId;
		this.name = name;
		IVAId = iVAId;
		NIT = nIT;
		this.supplyAddress = supplyAddress;
		this.categoryId = categoryId;
		this.serviceAmount = serviceAmount;
		this.serviceBalance = serviceBalance;
		this.totalAmount = totalAmount;
		this.status = status;
		this.batchId = batchId;
		this.authorizationNumber = authorizationNumber;
		this.authExpirationDate = authExpirationDate;
		this.controlCode = controlCode;
	}



	//#region Getters y Setters
	
	public int getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}
	public int getSupplyId() {
		return supplyId;
	}
	public void setSupplyId(int supplyId) {
		this.supplyId = supplyId;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
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
	public DateTime getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(DateTime issueDate) {
		this.issueDate = issueDate;
	}
	public DateTime getOrigExpirationDate() {
		return origExpirationDate;
	}
	public void setOrigExpirationDate(DateTime origExpirationDate) {
		this.origExpirationDate = origExpirationDate;
	}
	public DateTime getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(DateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	public DateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
	public DateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}
	public int getSupplyNumber() {
		return supplyNumber;
	}
	public void setSupplyNumber(int supplyNumber) {
		this.supplyNumber = supplyNumber;
	}
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIVAId() {
		return IVAId;
	}
	public void setIVAId(int iVAId) {
		IVAId = iVAId;
	}
	public String getNIT() {
		return NIT;
	}
	public void setNIT(String nIT) {
		NIT = nIT;
	}
	public String getSupplyAddress() {
		return supplyAddress;
	}
	public void setSupplyAddress(String supplyAddress) {
		this.supplyAddress = supplyAddress;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}
	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
	}
	public BigDecimal getServiceBalance() {
		return serviceBalance;
	}
	public void setServiceBalance(BigDecimal serviceBalance) {
		this.serviceBalance = serviceBalance;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	public String getAuthorizationNumber() {
		return authorizationNumber;
	}
	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}
	public DateTime getAuthExpirationDate() {
		return authExpirationDate;
	}
	public void setAuthExpirationDate(DateTime authExpirationDate) {
		this.authExpirationDate = authExpirationDate;
	}
	public String getControlCode() {
		return controlCode;
	}
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}
	public String getLiteral() {
		return literal;
	}
	public void setLiteral(String literal) {
		this.literal = literal;
	}
	public String getClientAddress() {
		return clientAddress;
	}
	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}
	public String getMeterNumber() {
		return meterNumber;
	}
	public void setMeterNumber(String meterNumber) {
		this.meterNumber = meterNumber;
	}
	public String getAuthorizationDescription() {
		return authorizationDescription;
	}
	public void setAuthorizationDescription(String authorizationDescription) {
		this.authorizationDescription = authorizationDescription;
	}
	
	//#endregion
}
