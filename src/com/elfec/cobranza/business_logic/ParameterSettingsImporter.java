package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.data_exchange.DataImporter;
import com.elfec.cobranza.model.data_exchange.DataImporter.ImportSource;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.model.settings.ParameterSetting;
import com.elfec.cobranza.remote_data_access.ParameterSettingsRDA;
import com.elfec.cobranza.settings.ParameterSettingsManager;

/**
 * Se encarga de importar las configuraciones parametrizables
 * @author drodriguez
 *
 */
public class ParameterSettingsImporter {
	/**
	 * Importa la tabla de parámetros COBRANZA.COBRANZA_MOVIL_PARAM
	 * @param username
	 * @param password
	 * @return resultado del acceso remoto
	 */
	public static DataAccessResult<User> importParameterSettings(final User user, final String password)
	{
		DataAccessResult<User> result = new DataAccessResult<User>(true, user);
		DataAccessResult<Boolean> res = DataImporter.importOnceRequiredData(new ImportSource<ParameterSetting>() {
			@Override
			public List<ParameterSetting> requestData() throws ConnectException, SQLException {
				List<ParameterSetting> parameters = ParameterSettingsRDA.requestParameterSettings(user.getUsername(), password);
				ParameterSettingsManager.loadParameters(parameters);
				return parameters;
			}
		});
		result.addErrors(res.getErrors());
		return result;
	}
}
