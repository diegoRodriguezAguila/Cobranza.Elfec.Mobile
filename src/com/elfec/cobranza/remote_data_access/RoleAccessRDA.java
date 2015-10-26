package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.SQLException;

import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;


/**
 * Provee de una capa de acceso a datos remota para la activación de roles
 * @author drodriguez
 *
 */
public class RoleAccessRDA {

	/**
	 * Habilita un rol remotamente
	 * @param username
	 * @param password
	 * @param rolename nombre del rol a habilitar
	 * @param rolepassword contraseña del rol a habilitar, puede ser nulo o vacio si es que
	 * el rol no requiere de una contraseña
	 * @throws SQLException 
	 * @throws ConnectException 
	 */
	public static void enableRole(String username, String password, String rolename, String rolepassword) throws ConnectException, SQLException
	{
		OracleDatabaseConnector.instance(username, password).executeUpdate("SET ROLE "+rolename+(rolepassword==null||rolepassword.isEmpty()?"":
			(" IDENTIFIED BY "+rolepassword)));
	}
}
                                                                                                                                            