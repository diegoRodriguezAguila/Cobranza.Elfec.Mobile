package com.elfec.cobranza.business_logic;

import com.elfec.cobranza.model.User;
import com.elfec.cobranza.settings.PreferencesManager;

/**
 * Maneja las sesiones de los usuarios
 * @author drodriguez
 *
 */
public class SessionManager {

	/**
	 * Inicia o sobreescribe la sesión para el usuario actual
	 * @param user
	 */
	public static void startSession(User user)
	{
		PreferencesManager.instance().setLoggedUsername(user.getUsername());
		PreferencesManager.instance().setLoggedCashdeskNumber(user.getCashDeskNumber());
		PreferencesManager.instance().setLoggedCashdeskDesc(user.getCashDeskDesc());
	}
	
	/**
	 * Cierra la sesión, eliminando todas las variables de sesión actuales
	 */
	public static void finishSession()
	{
		PreferencesManager.instance().setLoggedUsername(null);
		PreferencesManager.instance().setLoggedCashdeskNumber(-1);
		PreferencesManager.instance().setLoggedCashdeskDesc(null);
	}
	
	/**
	 * Obtiene el nombre de usuario logeado actual
	 * @return null si es que ningun usuario inició sesión
	 */
	public static String getLoggedInUsername()
	{
		return PreferencesManager.instance().getLoggedUsername();
	}
	
	/**
	 * Obtiene el numero de caja del usuario logeado actual
	 * @return -1 si es que ningun usuario inició sesión
	 */
	public static int getLoggedCashdeskNumber()
	{
		return PreferencesManager.instance().getLoggedCashdeskNumber();
	}

	/**
	 * Obtiene la descripción de caja del usuario logeado actual
	 * @return null si es que ningun usuario inició sesión
	 */
	public static String getLoggedCashdeskDesc()
	{
		return PreferencesManager.instance().getLoggedCashdeskDesc();
	}
	
	/**
	 * Vericia si el usuario proporcionado ha iniciado sesión y es el actual
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
	 * Verifica si es que algún usuario inició sesión
	 * @return
	 */
	public static boolean isSessionStarted()
	{
		return PreferencesManager.instance().getLoggedUsername()!=null;
	}
}
