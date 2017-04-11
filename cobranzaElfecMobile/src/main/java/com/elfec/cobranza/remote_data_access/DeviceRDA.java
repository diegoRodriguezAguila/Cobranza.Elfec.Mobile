package com.elfec.cobranza.remote_data_access;

import com.elfec.cobranza.model.enums.DeviceStatus;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Se encarga de las conexiones remotas a oracle para realizar tareas sobre el dispositivo
 * @author drodriguez
 *
 */
public class DeviceRDA {
	
	/**
	 * Obtiene remotamente el estado de un dispositivo, si es que no existe en la tabla de IMEI_APP, se
	 * retorna cero como estado no disponible
	 * @param username
	 * @param password
	 * @param IMEI
	 * @return 0 estado de dispositivo inactivo, 1 activo y válido par autilizar
	 * @throws SQLException 
	 * @throws ConnectException 
	 */
	public static DeviceStatus requestDeviceStatus(String username, String password, String IMEI) throws ConnectException, SQLException
	{
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
					executeSelect("SELECT * FROM MOVILES.IMEI_APP WHERE IMEI="+IMEI+" AND APLICACION='Cobranza Movil'");
		while(rs.next())
		{
			return DeviceStatus.get(rs.getShort("ESTADO"));
		}
		rs.close();
		return DeviceStatus.UNABLED;
	}
}
