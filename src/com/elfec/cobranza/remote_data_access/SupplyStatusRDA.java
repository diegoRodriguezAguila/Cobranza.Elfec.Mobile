package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.helpers.text_format.RouteListToSQL;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.SupplyStatus;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para operaciones relacionadas con SUMIN_ESTADOS
 * @author drodriguez
 *
 */
public class SupplyStatusRDA {
	/**
	 * Obtiene los SUMIN_ESTADOS de oracle
	 * @param username
	 * @param password
	 * @param routes la lista de rutas en formato (12334,1234)
	 * @return Lista de SUMIN_ESTADOS
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<SupplyStatus> requestSupplyStatuses(String username, String password, String routes) throws ConnectException, SQLException
	{
		List<SupplyStatus> supplyStatuses = new ArrayList<SupplyStatus>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT * FROM ERP_ELFEC.SUMIN_ESTADOS WHERE IDRUTA IN "+routes);
		while(rs.next())
		{
			supplyStatuses.add(new SupplyStatus(new DateTime(rs.getDate("FECHA")), rs.getInt("IDLOTE"), rs.getInt("IDSUMINISTRO"), rs.getInt("IDMEDIDOR"), 
					rs.getInt("IDCONCEPTO"), rs.getInt("IDSUBCONCEPTO"), rs.getInt("LECTURA"), rs.getInt("NROTRANSACCION"), 
					rs.getInt("LECTURA_ANT"), new DateTime(rs.getDate("FECHA_ANT")), rs.getInt("CONSUMO"), rs.getInt("IDRUTA"), rs.getInt("IDCBTE"), 
					rs.getInt("IDCBTE_SIM"), rs.getInt("CONS_FACTURACION"), rs.getInt("DELTA_PEND_COMPEN")));
		}
		return supplyStatuses;
	}
	
	/**
	 * Obtiene los SUMIN_ESTADOS de oracle
	 * @param username
	 * @param password
	 * @param routes la lista de rutas
	 * @return Lista de SUMIN_ESTADOS
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<SupplyStatus> requestSupplyStatuses(String username, String password, List<Route> routes) throws ConnectException, SQLException
	{
		return requestSupplyStatuses(username, password, RouteListToSQL.convertToSQL(routes));
	}
}
