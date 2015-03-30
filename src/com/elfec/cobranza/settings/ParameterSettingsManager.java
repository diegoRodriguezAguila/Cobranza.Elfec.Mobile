package com.elfec.cobranza.settings;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elfec.cobranza.model.downloaders.DataImporter;
import com.elfec.cobranza.model.downloaders.DataImporter.ImportSource;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.model.settings.ParameterSetting;
import com.elfec.cobranza.remote_data_access.ParameterSettingsRDA;

/**
 * Controla los parámetros que se descargan de la tabla de parámetros
 * @author drodriguez
 *
 */
public class ParameterSettingsManager {
	
	/**
	 * Los parámetros cargados en memoria
	 */
	private static Map<String, ParameterSetting> parametersTable;

	static
	{
		parametersTable = new HashMap<String, ParameterSetting>();
		loadParameters(ParameterSetting.getAll(ParameterSetting.class));
	}
	
	/**
	 * Carga los parámetros de la tabla de parámetros
	 */
	private static void loadParameters(List<ParameterSetting> parameters)
	{
		parametersTable.clear();
		for(ParameterSetting parameter : parameters)
		{
			parametersTable.put(parameter.getKey(), parameter);
		}
	}
	
	/**
	 * Importa la tabla de parámetros COBRANZA.COBRANZA_MOVIL_PARAM
	 * @param username
	 * @param password
	 * @return resultado del acceso remoto
	 */
	public static DataAccessResult<Boolean> importParameterSettings(final String username, final String password)
	{
		return DataImporter.importOnceRequiredData(new ImportSource<ParameterSetting>() {
			@Override
			public List<ParameterSetting> requestData() throws ConnectException, SQLException {
				List<ParameterSetting> parameters = ParameterSettingsRDA.requestParameterSettings(username, password);
				loadParameters(parameters);
				return parameters;
			}
		});
	}
	
	/**
	 * Obtiene el parametro con la key especificada
	 * @param paramKey
	 * @return ParameterSetting, si no existe ningun archivo con esa key devuelve un nuevo ParameterSetting con la key definida,
	 * pero con valor null
	 */
	public static ParameterSetting getParameter(ParamKey paramKey)
	{
		ParameterSetting setting = parametersTable.get(paramKey.toString());
		return setting!=null?setting:new ParameterSetting(paramKey.toString(), (String)null);
	}
	
	/**
	 * Define las llaves para acceder a la tabla de parámetros
	 * @author drodriguez
	 *
	 */
	public enum ParamKey
	{
		SFV_DATE("FECHA_SFV"),
		HEADER_IMG_DATE("FECHA_HEADER_IMG"),
		FOOTER_IMG_DATE("FECHA_FOOTER_IMG"),
		IMAGES_SERVER("SERVIDOR_IMAGENES"),
		WS_SERVER("SERVIDOR_WS"),
		ANULATION_HOURS_LIMIT("TIEMPO_ANULACION_HORAS"),
		WS_ENABLED("ACTIVAR_WS"),
		OLD_MSG("LEYENDA_ANTIGUA"),
		NEW_MSG("LEYENDA_NUEVA"),
		ENTERPRISE_MSG("LEYENDA_EMPRESA");
		
		private String string;
		private ParamKey(String string)
		{
			this.string = string;
		}
		@Override
		public String toString() {
	       return string;
	   }
	}
}
