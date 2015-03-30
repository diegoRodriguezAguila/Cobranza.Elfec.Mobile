package com.elfec.cobranza.settings.remote_data_access;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.elfec.cobranza.helpers.FileManager;

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
	 * Obtiene la cadena de conexión de las configuraciones
	 * @param context
	 * @return
	 */
	public static String getConnectionString(Context context)
	{
		try 
		{
			InputStream is = FileManager.getExternalFileInput(SETTINGS_FILE, false);
			if(is==null)
				is = context.getAssets().open(SETTINGS_FILE);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String bufferString = new String(buffer);
			return getConnectionStringFromJSON(bufferString);
		} 
		catch (FileNotFoundException e){
			e.printStackTrace();
		} 
		catch (IOException e){
			e.printStackTrace();
		} 
		catch (JSONException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Obtiene una cadena de conexión a partir de una configuración json
	 * @param jsonString
	 * @return
	 * @throws JSONException
	 */
	public static String getConnectionStringFromJSON(String jsonString) throws JSONException
	{
		JSONObject dbSettings = new JSONObject(jsonString);
		return "jdbc:oracle:thin:@//"+dbSettings.getString("ip")+":"+dbSettings.getString("port")+"/"+dbSettings.getString("service");
	}
}
