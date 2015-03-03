package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.Zone;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para operaciones relacionadas con zonas
 * @author drodriguez
 *
 */
public class ZoneRDA {

	/**
	 *  Obtiene todas las zonas de un usuario que correspondan a su IDEMPLEADO
	 * @param ownerUser el usuario del que se quiere obtener las zonas
	 * @param password para la conexión
	 * @return Lista de zonas que le fueron asignadas al usuario
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<Zone> requestUserZones(User ownerUser, String password) throws ConnectException, SQLException
	{
		List<Zone> zones = new ArrayList<Zone>();
		ResultSet rs = OracleDatabaseConnector.instance(ownerUser.getUsername(), password).
				executeSelect("SELECT Z.IDZONA, DESCRIPCION "
						+ "FROM ERP_ELFEC.SEG_USER_ZONAS UZ, ERP_ELFEC.ZONAS Z "
						+ "WHERE UZ.IDZONA=Z.IDZONA AND NO="+ownerUser.getAssignationNumber()
						+" AND Z.IDZONA BETWEEN 5000 AND 6999 AND Z.IDZONA<>5012");
		while(rs.next())
		{
			zones.add(new Zone(ownerUser, rs.getInt("IDZONA"),rs.getString("DESCRIPCION")));
		}
		return zones;
	}
	
}
