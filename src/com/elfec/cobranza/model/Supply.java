package com.elfec.cobranza.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
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
	@Column(name = "ClientName")
	private String clientName;
	/**
	 * IDSUMINISTRO
	 * Identificador del suministros, tabla de referencia suministros	
	 */
	@Column(name = "SupplyId")
	private int supplyId;		
	/**
	 * NROSUM en Oracle
	 */
	@Column(name = "SupplyNumber")
	private String supplyNumber;
	/**
	 * DIRECCION sacada de funcion MOVILES.FCOBRA_OBTENER_DIRECCION(IDSUMINISTRO)
	 */
	@Column(name = "ClientAddress")
	private String clientAddress;
	
	/**
	 * Las facturas relacionadas con el suministro
	 */
	private List<CoopReceipt> allReceipts;
	
	public Supply() {
		super();
	}

	public Supply(int clientId, String clientName, int supplyId,
			String supplyNumber, String clientAddress) {
		super();
		this.clientId = clientId;
		this.clientName = clientName;
		this.supplyId = supplyId;
		this.supplyNumber = supplyNumber;
		this.clientAddress = clientAddress;
	}
	/**
	 * Busca al suministro que coincida con alguno de los parámetros
	 * @param nus
	 * @param accountNumber
	 */
	public static Supply findSupplyByNUSOrAccount(int nus, String accountNumber)
	{
		From query = new Select()
        .from(Supply.class);
		if(nus!=-1)
			query.where("SupplyId=?",nus);
		if(accountNumber!=null && !accountNumber.isEmpty())
			query.where("SupplyNumber=?",accountNumber);
        return query.executeSingle();
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
				receipt.getSupplyStatus();
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
        		+ "(SELECT 0 FROM CollectionPayments "
        		+ "WHERE r.ReceiptId = c.ReceiptId AND Status=1)", supplyId)
        		.orderBy("Year, PeriodNumber").execute();
	}
	
	/**
	 * Obtiene todas las facturas  del suministro que ya fueron pagadas
	 * @return lista de comprobantes
	 */
	public List<CoopReceipt> getPaidReceipts()
	{
		return new Select()
	        .from(CoopReceipt.class).as("r").where("SupplyId = ? AND EXISTS "
	        		+ "(SELECT 0 FROM CollectionPayments "
	        		+ "WHERE r.ReceiptId = c.ReceiptId AND Status=1)", supplyId)
	        		.orderBy("Year, PeriodNumber DESC").execute();
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
	
	//#endregion
}
