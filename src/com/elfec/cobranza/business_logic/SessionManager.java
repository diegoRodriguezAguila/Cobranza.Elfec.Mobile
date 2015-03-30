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
	 * Inicia o sobreescribe la sesi�n para el usuario actual
	 * @param user
	 */
	public static void startSession(User user)
	{
		PreferencesManager.instance().setLoggedUsername(user.getUsername());
		PreferencesManager.instance().setLoggedCashdeskNumber(user.getCashDeskNumber());
		PreferencesManager.instance().setLoggedCashdeskDesc(user.getCashDeskDesc());
	}
	
	/**
	 * Cierra la sesi�n, eliminando todas las variables de sesi�n actuales
	 */
	public static void finishSession()
	{
		PreferencesManager.instance().setLoggedUsername(null);
		PreferencesManager.instance().setLoggedCashdeskNumber(-1);
		PreferencesManager.instance().setLoggedCashdeskDesc(null);
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
	 * Obtiene el numero de caja del usuario logeado actual
	 * @return -1 si es que ningun usuario inici� sesi�n
	 */
	public static int getLoggedCashdeskNumber()
	{
		return PreferencesManager.instance().getLoggedCashdeskNumber();
	}

	/**
	 * Obtiene la descripci�n de caja del usuario logeado actual
	 * @return null si es que ningun usuario inici� sesi�n
	 */
	public static String getLoggedCashdeskDesc()
	{
		return PreferencesManager.instance().getLoggedCashdeskDesc();
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
