package com.elfec.cobranza.model;

import java.util.List;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.elfec.cobranza.model.enums.EnergySupplyStatus;
import com.elfec.cobranza.model.serializers.JodaDateTimeSerializer;
/**
 * Contiene la información del suministro, cliente y medidor
 * @author drodriguez
 *
 */
@Table(name = "Supplies")
public class Supply extends Model {
	/**
	 * IDCLIENTE en Oracle
	 */
	@Column(name = "ClientId", notNull=true)
	private int clientId;
	/**
	 * RAZON_SOCIAL en Oracle
	 */
	@Column(name = "ClientName", index=true)
	private String clientName;
	/**
	 * CUIT en Oracle
	 */
	@Column(name = "ClientNIT", index=true)
	private String clientNIT;
	/**
	 * IDSUMINISTRO
	 * Identificador del suministros, tabla de referencia suministros	
	 */
	@Column(name = "SupplyId", index=true)
	private int supplyId;		
	/**
	 * NROSUM en Oracle
	 */
	@Column(name = "SupplyNumber", index=true)
	private String supplyNumber;
	/**
	 * DIRECCION sacada de funcion MOVILES.FCOBRA_OBTENER_DIRECCION(IDSUMINISTRO)
	 */
	@Column(name = "ClientAddress")
	private String clientAddress;
	
	@Column(name = "RouteRemoteId", index=true)
	private int routeRemoteId;
	
	/**
	 * Estado del suministro {@link SupplyStatus} , 
	 * 1 Normal 2 Pendiente Conexion 3 Pendiente de desconexion 
	 * 4 Suspendido 5 Baja 6 Incobrable 
	 * 7 Desconectado, tabla de referencia status_sumin con el tipo de servicio
	 */
	@Column(name = "Status")
	private short status;
	
	/**
	 * Las facturas relacionadas con el suministro
	 */
	private List<CoopReceipt> allReceipts;
	
	public Supply() {
		super();
	}

	public Supply(int clientId, String clientName, String clientNIT, int supplyId,
			String supplyNumber, String clientAddress, int routeRemoteId, short status) {
		super();
		this.clientId = clientId;
		this.clientName = clientName;
		this.clientNIT = clientNIT;
		this.supplyId = supplyId;
		this.supplyNumber = supplyNumber;
		this.clientAddress = clientAddress;
		this.routeRemoteId = routeRemoteId;
		this.status = status;
	}
	/**
	 * Busca al suministro que coincida con alguno de los parámetros
	 * @param nus
	 * @param accountNumber
	 * @return lista de suministros que coinciden con los términos de búsqueda
	 */
	public static List<Supply> findSupply(int nus, String accountNumber, String clientName, long nit)
	{
		From query = new Select()
        .from(Supply.class);
		if(nus!=-1)
			query.where("SupplyId=?",nus);
		if(accountNumber!=null && !accountNumber.isEmpty())
			query.where("SupplyNumber=?",accountNumber);
		if(nit!=-1)
			query.where("ClientNIT=?",nit);
		if(clientName!=null && !clientName.isEmpty())
			query.where("ClientName LIKE '%"+clientName+"%'");
        return query.execute();
	}
	
	/**
	 * Busca un suministro por nus y número de cuenta
	 * @param nus
	 * @return el suministro encontrado / null si no se encontró
	 */
	public static Supply findSupplyByNUS(int nus) {
		List<Supply> foundSupplies = findSupply(nus, null, null, -1);
		return foundSupplies.size()>0 ? foundSupplies.get(0) : null;
	}
	
	/**
	 * Obtiene las facturas de este suministro
	 * @return Lista de CoopReceipt
	 */
	public List<CoopReceipt> getAllReceipts()
	{
		return getAllReceipts(false);
	}
	
	/**
	 * Obtiene las facturas de este suministro
	 * @param cacheSupplyStatus indica si se debe cachear los sumin_estados de cada cbtes_coop
	 * @return Lista de CoopReceipt
	 */
	public List<CoopReceipt> getAllReceipts(boolean cacheSupplyStatus)
	{
		if(allReceipts==null)
			allReceipts = new Select()
			        .from(CoopReceipt.class).where("SupplyId = ?", supplyId)
			        .orderBy("Year, PeriodNumber").execute();
		if(cacheSupplyStatus)
			for(CoopReceipt receipt : allReceipts)
				receipt.getSupplyStatusSet();
		return allReceipts;
	}
	
	/**
	 * Obtiene todas las facturas pendientes del suministro
	 * @return lista de comprobantes
	 */
	public List<CoopReceipt> getPendingReceipts()
	{
		return new Select()
        .from(CoopReceipt.class).as("r").where("SupplyId = ? AND NOT EXISTS "
        		+ "(SELECT 0 FROM CollectionPayments AS c "
        		+ "WHERE r.ReceiptId = c.ReceiptId AND Status=1)", supplyId)
        		.orderBy("Year, PeriodNumber").execute();
	}
	
	/**
	 * Obtiene todas las facturas pendientes del suministro cuya fecha
	 * de vencimiento es más antigua a la fecha actual
	 * @return lista de comprobantes
	 */
	public List<CoopReceipt> getExpiredPendingReceipts()
	{
		JodaDateTimeSerializer serializer = new JodaDateTimeSerializer();
		return new Select()
        .from(CoopReceipt.class).as("r").where("SupplyId = ? "
        		+ " AND ExpirationDate < ? AND NOT EXISTS "
        		+ "(SELECT 0 FROM CollectionPayments AS c "
        		+ "WHERE r.ReceiptId = c.ReceiptId AND Status=1)", supplyId, 
        		serializer.serialize(DateTime.now().withTime(23, 59, 59, 0)))
        		.orderBy("Year, PeriodNumber").execute();
	}
	
	/**
	 * Obtiene todas las facturas  del suministro que ya fueron pagadas, a partir de la fecha proporcionada
	 * @param fromDate
	 * @return lista de comprobantes
	 */
	public List<CoopReceipt> getDatePaidReceipts(DateTime fromDate)
	{
		JodaDateTimeSerializer serializer = new JodaDateTimeSerializer();
		return new Select()
	        .from(CoopReceipt.class).as("r").where("SupplyId = ? AND EXISTS "
	        		+ "(SELECT 0 FROM CollectionPayments AS c "
	        		+ "WHERE r.ReceiptId = c.ReceiptId AND c.PaymentDate >= ? AND Status=1)", supplyId,
	        		serializer.serialize(fromDate.withTimeAtStartOfDay()))
	        		.orderBy("Year DESC, PeriodNumber DESC").execute();
	}
	
	/**
	 * Elimina todos los suministros que se encuentren en la lista provista
	 * @param routeIdsString lista de rutas en forma de clausula IN
	 */
	public static void cleanSupplies(String routeIdsString)
	{
		new Delete().from(Supply.class).where("RouteRemoteId IN "+routeIdsString).execute();
	}
	
	//#region Getters y Setters

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String getClientNIT() {
		return clientNIT;
	}

	public void setClientNIT(String clientNIT) {
		this.clientNIT = clientNIT;
	}

	public int getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(int supplyId) {
		this.supplyId = supplyId;
	}

	public String getSupplyNumber() {
		return supplyNumber;
	}

	public void setSupplyNumber(String supplyNumber) {
		this.supplyNumber = supplyNumber;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public int getRouteRemoteId() {
		return routeRemoteId;
	}

	public void setRouteRemoteId(int routeRemoteId) {
		this.routeRemoteId = routeRemoteId;
	}

	public EnergySupplyStatus getStatus() {
		return EnergySupplyStatus.get(status);
	}

	public void setStatus(EnergySupplyStatus status) {
		this.status = status.toShort();
	}
	
	//#endregion
}
