package com.elfec.cobranza.business_logic;

import android.nfc.FormatException;

import com.elfec.cobranza.model.enums.ConnectionParam;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.RoleAccessRDA;
import com.elfec.cobranza.settings.PreferencesManager;
import com.elfec.cobranza.settings.remote_data_access.OracleDatabaseSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.sql.SQLException;

/**
 * Se encarga de la lógica de negocio relacionada a roles
 * @author drodriguez
 *
 */
public class RoleAccessManager {
	
	/**
	 * Habilita el rol MOVIL_COBRANZA
	 * @param username
	 * @param password
	 * @return resultado del acceso remoto a datos
	 */
	public static DataAccessResult<Void> enableMobileCollectionRole(String username, String password)
	{
		DataAccessResult<Void> result = new DataAccessResult<Void>(true);
		String errorWhileEnablingRole = "Error al activar el rol: ";
		JSONObject settings;
		try {
			settings = OracleDatabaseSettings.getJSONConnectionSettings(PreferencesManager.getApplicationContext());
			RoleAccessRDA.enableRole(username, password, 
					settings.getString(ConnectionParam.ROLE.toString()), 
					settings.getString(ConnectionParam.PASSWORD.toString()));
		} catch (JSONException e) {
			result.addError(new FormatException(errorWhileEnablingRole+"Los parámetros de la configuración de conexión a la base de datos tienen un formato incorrecto!"));
			e.printStackTrace();
		} catch (ConnectException e) {
			result.addError(e);
		} catch (SQLException e) {
			result.addError(new SQLException(errorWhileEnablingRole+e.getMessage()));
		}
		catch (Exception e) {
			result.addError(new SQLException(errorWhileEnablingRole+e.getMessage()));
		}
		return result;
	}
}
