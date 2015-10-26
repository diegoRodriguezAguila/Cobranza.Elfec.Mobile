package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.Zone;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para operaciones relacionadas con rutas
 * @author drodriguez
 *
 */
public class RouteRDA {

	/**
	 * 
	 * @param zone la zona de la que se quiere obtener sus rutas
	 * @param username
	 * @param password
	 * @return lista de rutas que pertenecen a esa zona
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<Route> requestZoneRoutes(Zone zone, String username, String password) throws ConnectException, SQLException
	{
		List<Route> routes = new ArrayList<Route>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT IDRUTA, DESCRIPCION FROM ERP_ELFEC.RUTAS WHERE ESTADO=1 AND IDZONA="+zone.getZoneRemoteId());
		while(rs.next())
		{
			routes.add(new Route(zone, rs.getInt("IDRUTA"),rs.getString("DESCRIPCION")));
		}
		rs.close();
		return routes;
	}
}
