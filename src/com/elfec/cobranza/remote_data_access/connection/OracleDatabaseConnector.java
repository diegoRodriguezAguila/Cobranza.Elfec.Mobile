package com.elfec.cobranza.remote_data_access.connection;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.elfec.cobranza.remote_data_access.settings.OracleDatabaseSettings;

import android.content.Context;

/**
 * Se encarga de conectar remotamente a la base de datos oracle de la empresa
 * @author drodriguez
 *
 */
public class OracleDatabaseConnector {

	private static Context context;
	private static OracleDatabaseConnector dbConnectorInstance;
	
	private Connection conn=null;
	private Statement stmt=null;
	
	private OracleDatabaseConnector(String connectionUrl, String user, String password) throws ClassNotFoundException, SQLException
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		DriverManager.setLoginTimeout(15);
		conn = DriverManager.getConnection(connectionUrl, user, password);
		stmt = conn.createStatement();
	}
	
	/**
	 * Instancia u obtiene la instancia única del conector de base de datos oracle
	 * @param context
	 * @param user
	 * @param password
	 * @return la instancia del conector, null si es que no se pudo lograr la conexión
	 * @throws ConnectException 
	 */
	public static OracleDatabaseConnector instance(Context context, String user, String password) throws ConnectException
	{
		try {
			if(dbConnectorInstance==null)
				dbConnectorInstance = new OracleDatabaseConnector(
						OracleDatabaseSettings.getConnectionString(context), user, password);
			} catch (ClassNotFoundException e) {
				throw new ConnectException("No se pudo establecer conexión con el servidor, revise su nombre de usuario y contraseña");
			} catch (SQLException e) {
				throw new ConnectException("No se pudo establecer conexión con el servidor, revise su nombre de usuario y contraseña");
			}
		return dbConnectorInstance;
	}
	
	/**
	 * Obtiene la instancia única del conector de base de datos oracle, si es que no se definió un context o si no 
	 * se instanció previamente tira IllegalStateException
	 * @param user
	 * @param password
	 * @return la instancia del conector, null si es que no se pudo lograr la conexión
	 * @throws ConnectException 
	 */
	public static OracleDatabaseConnector instance( String user, String password) throws ConnectException
	{
		if(dbConnectorInstance==null)
		{
			try {
				if(context==null)
					throw new IllegalStateException("Debe llamar a instance pasandole los parámetros por lo menos una vez");
				else dbConnectorInstance =  new OracleDatabaseConnector(
						OracleDatabaseSettings.getConnectionString(context), user, password);
			} catch (ClassNotFoundException e) {
				throw new ConnectException("No se pudo establecer conexión con el servidor, revise su nombre de usuario y contraseña");
			} catch (SQLException e) {
				throw new ConnectException("No se pudo establecer conexión con el servidor, revise su nombre de usuario y contraseña");
			}
		}
		return dbConnectorInstance;
	}
	
	/**
	 * Asigna el conext para la instanciación del objeto
	 * @param context
	 */
	public static void initializeContext(Context context)
	{
		OracleDatabaseConnector.context = context;
	}
	
	/**
	 * Obtiene la clase con la que se realizan las consultas 
	 * @return
	 */
	public Statement getQuerier()
	{
		return stmt;
	}
	
	/**
	 * Ejecuta una consulta de tipo select
	 * @param selectQuery
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet executeSelect(String selectQuery) throws SQLException
	{
		return stmt.executeQuery(selectQuery);
	}
	
	/**
	 * Elimina la instancia del conector actual
	 */
	public static void disposeInstance()
	{
		dbConnectorInstance = null;
		System.gc();
	}
	
	/**
	 * Elimina la instancia del conector actual y el contexto
	 */
	public static void dispose()
	{
		dbConnectorInstance = null;
		context = null;
		System.gc();
	}
}
