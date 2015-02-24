package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;

import com.elfec.cobranza.model.User;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle
 * @author drodriguez
 *
 */
public class UserRemoteDataAccess {

	/**
	 * Obtiene remotamente el usuario especificado, se requiere password y nombre de usuario para la conexión
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException 
	 * @throws ConnectException 
	 */
	public static User requestUser(String username, String password) throws ConnectException, SQLException
	{
		ResultSet rs;
		OracleDatabaseConnector.disposeInstance();
		rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT * FROM MOVILES.USUARIO_APP WHERE USUARIO='"+username+"' AND APLICACION='Cobranza Movil'");
		while(rs.next())
		{
			return new User(username,password, DateTime.now(), rs.getShort("ESTADO"));
		}
		return null;
	}
}
