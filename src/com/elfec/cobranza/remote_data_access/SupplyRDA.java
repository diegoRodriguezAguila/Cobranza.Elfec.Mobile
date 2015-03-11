package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para operaciones relacionadas con SUMINISTROS
 * @author drodriguez
 *
 */
public class SupplyRDA {
	/**
	 * Obtiene los SUMINISTROS de oracle
	 * @param username
	 * @param password
	 * @param supplyIds la lista de ids de los comprobantes en formato (12334,1234)
	 * @return Lista de SUMINISTROS
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<Supply> requestSupplies(String username, String password, String supplyIds) throws ConnectException, SQLException
	{
		List<Supply> supplies = new ArrayList<Supply>();
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		ResultSet rs = stmt.executeQuery("SELECT  /*+CHOOSE*/ DISTINCT  G.IDCLIENTE, H.RAZON_SOCIAL NOMBRE, G.IDSUMINISTRO,"
				+ "G.NROSUM, MOVILES.FCOBRA_OBTENER_DIRECCION(G.IDSUMINISTRO) AS DIRECCION "
				+ "FROM ERP_ELFEC.SUMINISTROS G, ERP_ELFEC.CLIENTES H "
				+ "WHERE G.IDCLIENTE=H.IDCLIENTE AND G.IDSUMINISTRO IN "+supplyIds);
		while(rs.next())
		{
			supplies.add(new Supply(rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getInt("IDSUMINISTRO"), 
					rs.getString("NROSUM"), rs.getString("DIRECCION")));
		}
		rs.close();
		stmt.close();
		return supplies;
	}
}
