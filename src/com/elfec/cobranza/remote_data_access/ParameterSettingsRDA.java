package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.settings.ParameterSetting;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee de una capa de acceso a datos remotos de Oracle para COBRANZA.COBRANZA_MOVIL_PARAM
 * @author drodriguez
 */
public class ParameterSettingsRDA {
	/**
	 * Obtiene las COBRANZA.COBRANZA_MOVIL_PARAM de oracle
	 * @param username
	 * @param password
	 * @return Lista de ParameterSetting
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<ParameterSetting> requestParameterSettings(String username, String password) throws ConnectException, SQLException
	{
		List<ParameterSetting> parameters = new ArrayList<ParameterSetting>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT * FROM COBRANZA.COBRANZA_MOVIL_PARAM");
		ResultSetMetaData meta = rs.getMetaData();
		while(rs.next())
		{
			for (int i = 1; i <= meta.getColumnCount(); i++) {
                String key = meta.getColumnName(i);
                String value = rs.getString(key);
                parameters.add(new ParameterSetting(key, value));
            }
		}
		rs.close();
		return parameters;
	}
}
