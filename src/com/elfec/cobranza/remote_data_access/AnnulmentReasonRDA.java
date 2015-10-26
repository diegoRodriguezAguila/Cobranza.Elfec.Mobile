package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.AnnulmentReason;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para
 *  operaciones relacionadas con <b>MOTIVOS_ANULACION</b>
 * @author drodriguez
 */
public class AnnulmentReasonRDA {
	/**
	 * Obtiene remotamente los MOTIVOS_ANULACION
	 * @param username
	 * @param password
	 * @return Lista de MOTIVOS_ANULACION
	 * @throws ConnectException 
	 */
	public static List<AnnulmentReason> requestAnnulmentReasons(String username, String password) throws ConnectException, SQLException
	{
		List<AnnulmentReason> annulmentReasons = new ArrayList<AnnulmentReason>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
					executeSelect("SELECT * FROM COBRANZA.MOTIVOS_ANULACION WHERE ESTADO=1");
		while(rs.next())
		{
			annulmentReasons.add(new AnnulmentReason(rs.getInt("IDMOTIVO_ANULA"), rs.getString("DESCRIPCION")));
		}
		rs.close();
		return annulmentReasons;
	}
}
