package com.elfec.cobranza.settings.remote_data_access;

import android.content.Context;
import android.nfc.FormatException;

import com.elfec.cobranza.helpers.FileManager;
import com.elfec.cobranza.model.enums.ConnectionParam;
import com.elfec.cobranza.model.results.ManagerProcessResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Es un objeto estático que sirve para poder obtener y escribir el archivo de configuración que está en JSON bajo la carpeta
 * de assets, el nombre del archivo es <b>config_bd.json</b>.
 * @author drodriguez
 *
 */
public class OracleDatabaseSettings {
	/** Define el nombre del archivo de configuración. **/
	private static final String SETTINGS_FILE="db_settings.json";
	
	/**
	 * Sobre escribe las configuración de conexión actual
	 * @param settings
	 */
	public static ManagerProcessResult overwriteConnectionSettings(Context context, Map<String, String> settings)
	{
		ManagerProcessResult result = new ManagerProcessResult();
		JSONObject settingsJSON = new JSONObject();
		Iterator<String> keysItr = settings.keySet().iterator();
		String key;
		try {
			while(keysItr.hasNext()) {
				key = keysItr.next();	
				settingsJSON.put(key, settings.get(key));
		    }
			OutputStream os = FileManager.getFileOutputStream((FileManager.getInternalFile(context, SETTINGS_FILE, true)));
			os.write(settingsJSON.toString().getBytes());
			os.close();
		} catch (JSONException e) {
			e.printStackTrace();
			result.addError(new FormatException("Los parámetros de la configuración tienen un formato incorrecto"));
		} catch (IOException e) {
			e.printStackTrace();
			result.addError(new IOException("Ocurrió un error al escribir en el archivo de configuración, revise el estado de la memoria"));
		}
		return result;
	}
	
	/**
	 * Obtiene las configuraciones de conexion en un mapa
	 * @param context
	 * @return configuraciones de conexión
	 */
	public static Map<String, String> getConnectionSettings(Context context)
	{
		Map<String, String> settings = new HashMap<String, String>();
		try {
			JSONObject settingsJSON = getJSONConnectionSettings(context);
			Iterator<String> keysItr = settingsJSON.keys();
			String key;
			while(keysItr.hasNext()) {
					key = keysItr.next();
					settings.put(key, settingsJSON.getString(key));
			    }
			    return settings;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return settings;
	}
	
	/**
	 * Obtiene la cadena de conexión de las configuraciones
	 * @param context
	 * @return cadena de conexión JDBC <i>ej. jdbc:oracle:thin:@//iphost:port/service</i>
	 */
	public static String getConnectionString(Context context)
	{
		try {
			return getConnectionStringFromJSON(getJSONConnectionSettings(context));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Obtiene las configuraciones en un objeto JSON
	 * @return objeto json con las configuraciones
	 */
	public static JSONObject getJSONConnectionSettings(Context context) throws JSONException
	{
		try 
		{
			InputStream is = FileManager.getFileInputStream((FileManager.getInternalFile(context, SETTINGS_FILE, false)));
			if(is==null)
				is = context.getAssets().open(SETTINGS_FILE);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String bufferString = new String(buffer);
			return new JSONObject(bufferString);
		} 
		catch (FileNotFoundException e){
			e.printStackTrace();
		} 
		catch (IOException e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * Obtiene una cadena de conexión a partir de una configuración json
	 * @param dbSettings
	 * @return Cadena de conexión JDBC
	 * @throws JSONException
	 */
	public static String getConnectionStringFromJSON(JSONObject dbSettings) throws JSONException
	{
		String connectionString = "jdbc:oracle:thin:@//%s:%s/%s";
		return String.format(connectionString, dbSettings.getString(ConnectionParam.IP.toString()), 
				dbSettings.getString(ConnectionParam.PORT.toString()), dbSettings.getString(ConnectionParam.SERVICE.toString()));
	}
}
