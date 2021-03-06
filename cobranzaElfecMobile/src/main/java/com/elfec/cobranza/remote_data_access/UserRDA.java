package com.elfec.cobranza.remote_data_access;

import com.elfec.cobranza.model.User;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provee una capa de acceso remoto a la base de datos oracle
 * @author drodriguez
 *
 */
public class UserRDA {

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
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT USUARIO, ESTADO, NO, IDEMPLEADO "
							+ "FROM MOVILES.USUARIO_APP MU, ERP_ELFEC.SEG_USER SU "
							+ "WHERE upper(MU.USUARIO)=upper(SU.LASTNAME) AND upper(MU.USUARIO)=upper('"+username+"') "
							+ "AND MU.APLICACION='Cobranza Movil' AND SU.BLOQUEADO=0");
		while(rs.next())
		{
			return new User(username, password, rs.getInt("IDEMPLEADO"), rs.getInt("NO"), rs.getShort("ESTADO"));
		}
		rs.close();
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
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT  /*+CHOOSE*/  CAJA FROM COBRANZA.CAJERO_CAJA "
						+ "WHERE CAJERO="+cashierId+" AND PERFIL='OFFLINE' AND ESTADO=1");
		while(rs.next())
		{
			return rs.getInt("CAJA");
		}
		rs.close();
		return -1;
	}
	
	/**
	 * Obtiene remotamente la descripción de caja del usuario especificado, se requiere password
	 * y nombre de usuario para la conexión
	 * @param username
	 * @param password
	 * @param cashierId , este es el IDEMPLEADO en SEG_USER
	 * @return descripción de caja
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static String requestUserCashDeskDesc(String username, String password, int cashierId) throws ConnectException, SQLException
	{
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT  /*+CHOOSE*/  DESCRIPCION FROM COBRANZA.CAJERO_CAJA "
						+ "WHERE CAJERO="+cashierId+" AND PERFIL='OFFLINE' AND ESTADO=1");
		while(rs.next())
		{
			return rs.getString("DESCRIPCION");
		}
		rs.close();
		return null;
	}
}
