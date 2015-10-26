package com.elfec.cobranza.model;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.elfec.cobranza.business_logic.SupplyStatusSet;
/**
 * Almacena los CBTES_COOP
 * @author drodriguez
 *
 */
@Table(name = "CoopReceipts")
public class CoopReceipt extends Model {
	/**
	 * IDCBTE Identificador unico de comprobantes
	 */
	@Column(name = "ReceiptId", notNull=true, index=true)
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
	private String supplyNumber;
	/**
	 * IDRUTA en Oracle
	 */
	@Column(name = "RouteId", index=true)
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
	
	//EXTRA ATTRIBUTES
	private SupplyStatusSet supplyStatusSet;
	/**
	 * Sirve de caché de la consulta, debe actualizarse al realizar una anulación
	 */
	private CollectionPayment collectionPayment;
	
	public CoopReceipt() {
		super();
	}
	
	public CoopReceipt(int receiptId, int supplyId, int clientId,
			int enterpriseId, int branchOfficeId, String receiptType,
			int receiptGroup, String receiptLetter, int receiptNumber,
			DateTime issueDate, DateTime origExpirationDate,
			DateTime expirationDate, DateTime startDate, DateTime endDate,
			int year, int periodNumber, String supplyNumber, int routeId,
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

	/**
	 * Encuentra todas las facturas de la ruta indicada
	 * @param routeId
	 * @return
	 */
	public static List<CoopReceipt> findRouteReceipts(int routeId)
	{
		return new Select().from(CoopReceipt.class).where("RouteId=?",routeId).execute();
	}
	/**
	 * Obtiene la lista de SUMIN_ESTADOS (De consumo energia activa) de este comprobante
	 * @return supplyStatus
	 */
	public SupplyStatusSet getSupplyStatusSet()
	{
		if(supplyStatusSet==null)
			supplyStatusSet = new SupplyStatusSet(getRelatedSupplyStatus());
		return supplyStatusSet;
	}
	
	/**
	 * Obtiene los SUMIN_ESTADOS de energia activa relacionados al comprobante
	 * @return lista SUMIN_ESTADOS
	 */
	public List<SupplyStatus> getRelatedSupplyStatus()
	{
		return new Select().from(SupplyStatus.class)
				.where("ReceiptId = ?", this.receiptId)
				.where("ConceptId IN (10010, 10080, 10090, 10100)").execute();
	}
	
	/**
	 * Obtiene el SUMIN_ESTADOS (De consumo de potencia) de este comprobante
	 * @return supplyStatus
	 */
	public SupplyStatus getPowerSupplyStatus()
	{
		return new Select().from(SupplyStatus.class)
							.where("ReceiptId = ?", this.receiptId)
							.where("ConceptId IN (10020, 10140)").executeSingle();
	}
	/**
	 * Obtiene su cobro (CollectionPayment) en caso de tener uno, y que tenga el estado en 1
	 * @return
	 */
	public CollectionPayment getActiveCollectionPayment()
	{
		if(collectionPayment==null)
			collectionPayment  = new Select().from(CollectionPayment.class)
					.where("Status=1")
					.where("ReceiptId = ? ", receiptId)
					.where("SupplyId = ?", supplyId)
					.orderBy("PaymentDate DESC")
					.executeSingle();
		return collectionPayment;
	}

	/**
	 * Limpia la variable que sirve de caché para la consulta de obtención del cobro  actual activo (estado 1)
	 */
	public void clearActiveCollectionPayment()
	{
		collectionPayment = null;
	}
	
	/**
	 * Elimina todas las facturas que pertenecen a la lista de rutas provista
	 * @param routesString lista de rutas en forma de clausula IN
	 */
	public static void cleanRoutesCoopReceipts(String routesString)
	{
		new Delete().from(CoopReceipt.class).where("RouteId IN "+routesString).execute();
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
	public String getSupplyNumber() {
		return supplyNumber;
	}
	public void setSupplyNumber(String supplyNumber) {
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
