package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.SupplyCategoryType;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para operaciones relacionadas con TIPOS_CATEG_SUM
 * @author drodriguez
 *
 */
public class SupplyCategoryTypeRDA {

	/**
	 * Obtiene las TIPOS_CATEG_SUM de oracle
	 * @param username
	 * @param password
	 * @return Lista de TIPOS_CATEG_SUM
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<SupplyCategoryType> requestSupplyCategoryTypes(String username, String password) throws ConnectException, SQLException
	{
		List<SupplyCategoryType> supplyCategoryTypes = new ArrayList<SupplyCategoryType>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT * FROM ERP_ELFEC.TIPOS_CATEG_SUM");
		while(rs.next())
		{
			supplyCategoryTypes.add(new SupplyCategoryType(rs.getInt("IDTIPO_CATEG"), rs.getInt("IDTIPO_SRV"), 
					rs.getString("DESCRIPCION"), rs.getString("DESC_CORTA")));
		}
		rs.close();
		return supplyCategoryTypes;
	}
}
