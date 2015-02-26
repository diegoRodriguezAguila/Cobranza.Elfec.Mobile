package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		OracleDatabaseConnector.disposeInstance();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT USUARIO, ESTADO, IDEMPLEADO"
						+ " FROM MOVILES.USUARIO_APP MU, ERP_ELFEC.SEG_USER SU "
						+ "WHERE upper(MU.USUARIO)=upper(SU.LASTNAME) AND MU.USUARIO=upper('"+username+"') AND MU.APLICACION='Cobranza Movil'");
		while(rs.next())
		{
			return new User(username, password, rs.getInt("IDEMPLEADO"), rs.getShort("ESTADO"));
		}
		return null;
	}
	
	/**
	 * Obtiene remotamente el numero de caja del usuario especificado, se requiere password
	 * y nombre de usuario para la conexión
	 * @param username
	 * @param password
	 * @param cashierId , este es el IDEMPLEADO en SEG_USER
	 * @return numero de caja, -1 si es que no se encontró al usuario en la tabla COBRANZA.CAJERO_CAJA
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static int requestUserCashDeskNumber(String username, String password, int cashierId) throws ConnectException, SQLException
	{
		OracleDatabaseConnector.disposeInstance();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT CAJA FROM COBRANZA.CAJERO_CAJA "
						+ "WHERE CAJERO="+cashierId+" AND PERFIL='OFFLINE' AND ESTADO=1");
		while(rs.next())
		{
			return rs.getInt("CAJA");
		}
		return -1;
	}
}
