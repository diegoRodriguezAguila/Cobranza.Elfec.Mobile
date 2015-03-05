package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.Category;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para
 *  operaciones relacionadas con <b>CATEGORIAS</b> 
 * @author drodriguez
 */
public class CategoryRDA {
	/**
	 * Obtiene las CATEGORIAS de oracle
	 * @param username
	 * @param password
	 * @return Lista de CATEGORIAS
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<Category> requestCategories(String username, String password) throws ConnectException, SQLException
	{
		List<Category> categories = new ArrayList<Category>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT  * FROM ERP_ELFEC.CATEGORIAS");
		while(rs.next())
		{
			categories.add(new Category(rs.getString("IDCATEGORIA"), rs.getInt("IDTIPO_SRV"), rs.getString("DESCRIPCION"), 
					rs.getInt("IDTIPO_CATEG"), rs.getString("CTROL_IVAS"), rs.getShort("IDSTATUS"), rs.getString("CLASIF"), 
					rs.getString("CTROL_TMEDID"), rs.getString("CLASIF2"), rs.getBigDecimal("FACTOR_CARGA"), rs.getShort("FACTURAR_DEM")));
		}
		rs.close();
		return categories;
	}
}
