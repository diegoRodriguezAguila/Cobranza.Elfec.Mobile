package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.nfc.FormatException;

import com.elfec.cobranza.model.enums.ConnectionParam;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.RoleAccessRDA;
import com.elfec.cobranza.settings.PreferencesManager;
import com.elfec.cobranza.settings.remote_data_access.OracleDatabaseSettings;

/**
 * Se encarga de la l�gica de negocio relacionada a roles
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
		JSONObject settings;
		try {
			settings = OracleDatabaseSettings.getJSONConnectionSettings(PreferencesManager.getApplicationContext());
			RoleAccessRDA.enableRole(username, password, 
					settings.getString(ConnectionParam.ROLE.toString()), 
					settings.getString(ConnectionParam.PASSWORD.toString()));
		} catch (JSONException e) {
			result.addError(new FormatException("Los par�metros de la configuraci�n de conexi�n a la base de datos tienen un formato incorrecto!"));
			e.printStackTrace();
		} catch (ConnectException e) {
			result.addError(e);
		} catch (SQLException e) {
			result.addError(e);
		}
		return result;
	}
}