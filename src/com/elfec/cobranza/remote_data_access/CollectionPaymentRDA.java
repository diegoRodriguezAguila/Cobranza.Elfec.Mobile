package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.Statement;

import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para
 *  operaciones relacionadas con <b>COBRANZA.COBROS</b> 
 * @author drodriguez
 */
public class CollectionPaymentRDA {
	
	/**
	 * Inserta remotamente un COBRANZA.COBROS de oracle
	 * @param username
	 * @param password
	 * @param collectionPayment COBRO a insertar
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static int insertCollectionPayment(String username, String password, CollectionPayment collectionPayment) throws ConnectException, SQLException
	{
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		int result = stmt.executeUpdate(collectionPayment.toRemoteInsertSQL());
		stmt.close();
		return result;
	}
}
