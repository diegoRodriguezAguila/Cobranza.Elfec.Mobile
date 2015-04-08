package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.Statement;

import com.elfec.cobranza.model.WSCollection;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para
 *  operaciones relacionadas con <b>ERP_ELFEC.COB_WS</b> 
 * @author drodriguez
 */
public class WSCollectionRDA {
	
	/**
	 * Inserta remotamente un ERP_ELFEC.COB_WS de oracle
	 * @param username
	 * @param password
	 * @param wSCollection COB_WS a insertar
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static int insertWSCollection(String username, String password, WSCollection wSCollection) throws ConnectException, SQLException
	{
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		int result = stmt.executeUpdate(wSCollection.toInsertSQL());
		stmt.close();
		return result;
	}
}
