package com.elfec.cobranza.helpers;

import com.elfec.cobranza.model.exceptions.InitializationException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Maneja las sharedpreferences de toda la aplicación
 * @author Diego
 *
 */
public class PreferencesManager {

	private final String LOGGED_USERNAME = "loggedUsername";
	
	private SharedPreferences preferences;
	
	private PreferencesManager(Context context)
	{
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	
	private static Context context;
	private static PreferencesManager preferencesManager;
	/**
	 * Este método se debe llamar al inicializar la aplicación
	 * @param context
	 */
	public static void initialize(Context context)
	{
		PreferencesManager.context = context;
	}
	
	public static PreferencesManager instance()
	{
		if(preferencesManager==null)
		{
			if(context==null)
				throw new InitializationException();
			preferencesManager = new PreferencesManager(context);
		}
		return preferencesManager;
	}
	
	/**
	 * Obtiene el usuario logeado actual
	 * @return null si es que ninguno se ha logeado
	 */
	public String getLoggedUsername()
	{
		return preferences.getString(LOGGED_USERNAME, null);
	}
	
	/**
	 * Asigna el usuario logeado actual, sobreescribe cualquier usuario que haya sido logeado antes
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setLoggedUsername(String loggedUsername)
	{
		preferences.edit().putString(LOGGED_USERNAME, loggedUsername).commit();
		return this;
	}
	
}
