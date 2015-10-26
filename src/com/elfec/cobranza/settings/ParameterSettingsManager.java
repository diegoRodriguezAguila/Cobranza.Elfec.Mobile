package com.elfec.cobranza.settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elfec.cobranza.model.settings.ParameterSetting;

/**
 * Controla los par�metros que se descargan de la tabla de par�metros
 * @author drodriguez
 *
 */
public class ParameterSettingsManager {
	
	/**
	 * Los par�metros cargados en memoria
	 */
	private static Map<String, ParameterSetting> parametersTable;

	static
	{
		parametersTable = new HashMap<String, ParameterSetting>();
		loadParameters(ParameterSetting.getAll(ParameterSetting.class));
	}
	
	/**
	 * Carga los par�metros de la tabla de par�metros
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
	 * Define las llaves para acceder a la tabla de par�metros
	 * @author drodriguez
	 *
	 */
	public enum ParamKey
	{
		/**
		 * Fecha de cambio de sistema comercial SFV
		 */
		SFV_DATE("FECHA_SFV"),
		/**
		 * Fecha de actualizaci�n de imagen del header de las facturas
		 */
		HEADER_IMG_DATE("FECHA_HEADER_IMG"),
		/**
		 * Fecha de actualizaci�n de imagen del footer de las facturas
		 */
		FOOTER_IMG_DATE("FECHA_FOOTER_IMG"),
		/**
		 * Direcci�n del servidor de donde deben sacarse las im�genes
		 */
		IMAGES_SERVER("SERVIDOR_IMAGENES"),
		/**
		 * Direcci�n del servidor de web services
		 */
		WS_SERVER("SERVIDOR_WS"),
		/**
		 * Tiempo m�ximo que el usuario tiene para eliminar una factura en horas
		 */
		ANNULMENT_HOURS_LIMIT("TIEMPO_ANULACION_HORAS"),
		/**
		 * Bandera que indica si se deben activar los web services
		 */
		WS_ENABLED("ACTIVAR_WS"),
		/**
		 * Mensaje antiguo de facturas (pre fecha SFV)
		 */
		OLD_MSG("LEYENDA_ANTIGUA"),
		/**
		 * Mensaje nuevo de facturas (post fecha SFV)
		 */
		NEW_MSG("LEYENDA_NUEVA"),
		/**
		 * Mensaje de la empresa a los usuarios
		 */
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
