package com.elfec.cobranza.business_logic;

import com.elfec.cobranza.helpers.PreferencesManager;
import com.elfec.cobranza.model.User;

/**
 * Maneja las sesiones de los usuarios
 * @author drodriguez
 *
 */
public class SessionManager {

	/**
	 * Inicia o sobreescribe la sesi�n para el usuario actual
	 * @param user
	 */
	public static void startSession(User user)
	{
		PreferencesManager.instance().setLoggedUsername(user.getUsername());
	}
	
	/**
	 * Cierra la sesi�n, eliminando todas las variables de sesi�n actuales
	 */
	public static void finishSession()
	{
		PreferencesManager.instance().setLoggedUsername(null);
	}
	
	/**
	 * Obtiene el nombre de usuario logeado actual
	 * @return null si es que ningun usuario inici� sesi�n
	 */
	public static String getLoggedInUsername()
	{
		return PreferencesManager.instance().getLoggedUsername();
	}
	
	/**
	 * Vericia si el usuario proporcionado ha iniciado sesi�n y es el actual
	 * @param username
	 * @return
	 */
	public static boolean isUserLoggedIn(String username)
	{
		if(username==null)
			return false;
		return username.equals(PreferencesManager.instance().getLoggedUsername());
	}
	
	/**
	 * Verifica si es que alg�n usuario inici� sesi�n
	 * @return
	 */
	public static boolean isSessionStarted()
	{
		return PreferencesManager.instance().getLoggedUsername()!=null;
	}
}
