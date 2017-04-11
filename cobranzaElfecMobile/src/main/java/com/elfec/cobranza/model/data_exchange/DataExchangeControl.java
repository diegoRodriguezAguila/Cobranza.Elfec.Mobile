package com.elfec.cobranza.model.data_exchange;

import java.util.Locale;

import org.joda.time.DateTime;

import com.elfec.cobranza.model.enums.DataExchangeStatus;

/**
 * Modelo para el control de cargas y descargas en los móviles 
 * tabla en Oracle: <b>MOVILES.COBRA_RUTAS_ESTADOS</b>
 * @author drodriguez
 *
 */
public class DataExchangeControl {
	
	public static final String INSERT_QUERY = "INSERT INTO MOVILES.COBRA_RUTAS_ESTADOS "
			+ "VALUES (%d, %d, %d, %d, '%s', TO_DATE('%s', 'dd/mm/yyyy hh24:mi:ss'), '%s', %s, %s, %s, %d)";
	
	public static final String UPDATE_QUERY = "UPDATE MOVILES.COBRA_RUTAS_ESTADOS "
			+ "SET USUARIO_DESCARGA=%s, FECHA_DESCARGA=%s, IMEI_DESCARGA=%s, ESTADO=%d WHERE IDCOBRA=%d";
	
	private int routeRemoteId;
	
	private String importationUser;
	private DateTime importationDate;
	private String importationIMEI;
	
	private String exportationUser;
	private DateTime exportationDate;
	private String exportationIMEI;
	
	private short status;	

	public DataExchangeControl(int routeRemoteId, String importationUser,
			DateTime importationDate, String importationIMEI, DataExchangeStatus status) {
		this.routeRemoteId = routeRemoteId;
		this.importationUser = importationUser;
		this.importationDate = importationDate;
		this.importationIMEI = importationIMEI;
		setStatus(status);
	}
	
	
	
	public DataExchangeControl(String exportationUser,
			DateTime exportationDate, String exportationIMEI, DataExchangeStatus status) {
		super();
		this.exportationUser = exportationUser;
		this.exportationDate = exportationDate;
		this.exportationIMEI = exportationIMEI;
		setStatus(status);
	}



	/**
	 * Obtiene la cadena insert de este control de estado de ruta
	 * @return
	 */
	public String toInsertSQL()
	{
		return String.format(Locale.getDefault(), INSERT_QUERY, 666, routeRemoteId,
				DateTime.now().getYear(), DateTime.now().getMonthOfYear(), importationUser, 
				importationDate.toString("dd/MM/yyyy HH:mm:ss"), importationIMEI,
				(exportationUser==null?"NULL":"'"+exportationUser+"'"),
				(exportationDate==null?"NULL": String.format("TO_DATE('%s', 'dd/mm/yyyy hh24:mi:ss')", 
						exportationDate.toString("dd/MM/yyyy HH:mm:ss"))),
				(exportationIMEI==null?"NULL":"'"+exportationIMEI+"'"), status);
	}
	
	/**
	 * Obtiene la cadena update de este control de estado de ruta
	 * @param dataExchangeControlId
	 * @return consulta UPDATE
	 */
	public String toUpdateSQL(long dataExchangeControlId)
	{
		return String.format(Locale.getDefault(), UPDATE_QUERY,
				(exportationUser==null?"NULL":"'"+exportationUser+"'"),
				(exportationDate==null?"NULL": String.format("TO_DATE('%s', 'dd/mm/yyyy hh24:mi:ss')", 
						exportationDate.toString("dd/MM/yyyy HH:mm:ss"))),
				(exportationIMEI==null?"NULL":"'"+exportationIMEI+"'"), status, dataExchangeControlId);
	}

	//#region Getters y Setters
	
	public int getRouteRemoteId() {
		return routeRemoteId;
	}

	public void setRouteRemoteId(int routeRemoteId) {
		this.routeRemoteId = routeRemoteId;
	}

	public String getImportationUser() {
		return importationUser;
	}

	public void setImportationUser(String importationUser) {
		this.importationUser = importationUser;
	}

	public DateTime getImportationDate() {
		return importationDate;
	}

	public void setImportationDate(DateTime importationDate) {
		this.importationDate = importationDate;
	}

	public String getImportationIMEI() {
		return importationIMEI;
	}

	public void setImportationIMEI(String importationIMEI) {
		this.importationIMEI = importationIMEI;
	}

	public String getExportationUser() {
		return exportationUser;
	}

	public void setExportationUser(String exportationUser) {
		this.exportationUser = exportationUser;
	}

	public DateTime getExportationDate() {
		return exportationDate;
	}

	public void setExportationDate(DateTime exportationDate) {
		this.exportationDate = exportationDate;
	}

	public String getExportationIMEI() {
		return exportationIMEI;
	}

	public void setExportationIMEI(String exportationIMEI) {
		this.exportationIMEI = exportationIMEI;
	}

	public DataExchangeStatus getStatus() {
		return DataExchangeStatus.get(status);
	}

	public void setStatus(DataExchangeStatus status) {
		this.status = status.toShort();
	}
	
	//#endregion
	
}
