package com.elfec.cobranza.settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elfec.cobranza.model.settings.ParameterSetting;

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
	public static void loadParameters(List<ParameterSetting> parameters)
	{
		parametersTable.clear();
		for(ParameterSetting parameter : parameters)
		{
			parametersTable.put(parameter.getKey(), parameter);
		}
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
		ANNULMENT_HOURS_LIMIT("TIEMPO_ANULACION_HORAS"),
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
