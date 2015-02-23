package com.elfec.cobranza.presenter.views;

/**
 * Abstracción de una vista de login
 * @author drodriguez
 *
 */
public interface ILoginView {

	/**
	 * Obtiene el nombre de usuario
	 * @return nombre de usuario
	 */
	public String getUsername();
	/**
	 * Obtiene el password
	 * @return password
	 */
	public String getPassword();
	/**
	 * Obtiene el IMEI del dispositivo
	 * @return
	 */
	public String getIMEI();
	/**
	 * Limpia el campo del password
	 */
	public void clearPassword();
}
