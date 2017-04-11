package com.elfec.cobranza.remote_data_access;

import com.elfec.cobranza.model.data_exchange.DataExchangeControl;
import com.elfec.cobranza.model.enums.DataExchangeStatus;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

import org.joda.time.DateTime;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para operaciones relacionadas con <B>MOVILES.COBRA_RUTAS_ESTADOS</B>
 * @author drodriguez
 *
 */
public class DataExchangeControlRDA {
	
	/**
	 * Obtiene el control de rutas para la ruta y fecha dada
	 * @param username
	 * @param password
	 * @param date
	 * @return {@link DataExchangeControl}
	 * @throws SQLException 
	 * @throws ConnectException 
	 */
	public static DataExchangeControl requestDataExchangeControl(String username, String password, 
			int routeRemoteId, DateTime date) throws ConnectException, SQLException
	{
		DataExchangeControl dataExchangeControl = null;
		String query = "SELECT * FROM MOVILES.COBRA_RUTAS_ESTADOS WHERE ESTADO=1 AND IDRUTA = %d AND ANIO=%d AND MES=%d";
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect(String.format(query, routeRemoteId, date.getYear(), date.getMonthOfYear()));
		if(rs.next())
		{
			dataExchangeControl = new DataExchangeControl(rs.getInt("IDRUTA"), 
					rs.getString("USUARIO_CARGA"), new DateTime(rs.getDate("FECHA_CARGA")), 
					rs.getString("IMEI_CARGA"), DataExchangeStatus.get(rs.getShort("ESTADO")));
		}
		rs.close();
		return dataExchangeControl;
	}
	
	/**
	 * Inserta remotamente un MOVILES.COBRA_RUTAS_ESTADOS de oracle
	 * @param username
	 * @param password
	 * @param dataImportControl registro de control a insertar
	 * @return el id del registro insertado
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static long registerDataImportControl(String username, String password, DataExchangeControl dataImportControl) throws ConnectException, SQLException
	{
		long lastId = -1;
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		String query = "SELECT MOVILES.AUTONUM_COBRA_RUTAS_ESTADOS.CURRVAL FROM DUAL";
		int result = stmt.executeUpdate(dataImportControl.toInsertSQL());
		if(result>0)
		{
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				lastId = rs.getLong(1);
			}
			rs.close();
		}
		stmt.close();
		return lastId;
	}
	
	/**
	 * Actualiza remotamente el registro MOVILES.COBRA_RUTAS_ESTADOS de oracle
	 * @param username
	 * @param password
	 * @param dataExchangeControlId eL Id remoto del control de exportación de rutas
	 * @param dataExportControl
	 * @return 0 si es que no se actualizó correctamente 
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static int registerDataExportControl(String username, String password, 
			long dataExchangeControlId, DataExchangeControl dataExportControl) throws ConnectException, SQLException
	{
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		int result = stmt.executeUpdate(dataExportControl.toUpdateSQL(dataExchangeControlId));
		stmt.close();
		return result;
	}
}
