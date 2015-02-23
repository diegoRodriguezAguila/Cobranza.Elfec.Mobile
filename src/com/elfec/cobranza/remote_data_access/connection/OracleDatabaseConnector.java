package com.elfec.cobranza.remote_data_access.connection;

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
	
	private OracleDatabaseConnector(String connectionUrl, String user, String password)
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(connectionUrl, user, password);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Instancia u obtiene la instancia única del conector de base de datos oracle
	 * @param context
	 * @param user
	 * @param password
	 * @return la instancia del conector
	 */
	public static OracleDatabaseConnector instance(Context context, String user, String password)
	{
		if(dbConnectorInstance==null)
			dbConnectorInstance = new OracleDatabaseConnector(
					OracleDatabaseSettings.getConnectionString(context), user, password);
		return dbConnectorInstance;
	}
	
	/**
	 * Obtiene la instancia única del conector de base de datos oracle, si es que no se definió un context o si no 
	 * se instanció previamente tira IllegalStateException
	 * @param user
	 * @param password
	 * @return
	 */
	public static OracleDatabaseConnector instance( String user, String password)
	{
		if(dbConnectorInstance==null)
		{
			if(context==null)
				throw new IllegalStateException("Debe llamar a instance pasandole los parámetros por lo menos una vez");
			else dbConnectorInstance =  new OracleDatabaseConnector(
					OracleDatabaseSettings.getConnectionString(context), user, password);
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
		context = null;
		System.gc();
	}
}
