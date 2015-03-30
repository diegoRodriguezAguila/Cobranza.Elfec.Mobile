package com.elfec.cobranza.presenter.views;

import java.util.List;

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
	 * Obtiene las reglas de validación a nivel de UI para el campo del nombre de usuario
	 * @return
	 */
	public String getUsernameValidationRules();
	/**
	 * Obtiene las reglas de validación a nivel de UI para el campo del password
	 * @return
	 */
	public String getPasswordValidationRules();
	/**
	 * Muestra errores en el campo del nombre de usuario
	 * @param errors
	 */
	public void setUsernameFieldErrors(List<String> errors);
	/**
	 * Muestra errores en el campo de la contraseña
	 * @param errors
	 */
	public void setPasswordFieldErrors(List<String> errors);
	/**
	 * Limpia el campo del password
	 */
	public void clearPassword();
	/**
	 * Notifica al usuario que existen errores en los campos
	 */
	public void notifyErrorsInFields();
	/**
	 * Indica al usuario que debe esperar
	 */
	public void showWaiting();
	/**
	 * Acualiza el mensaje de espera del usuario
	 */
	public void updateWaiting(int strId);
	/**
	 * Borra el mensaje de espera
	 */
	public void hideWaiting();
	/**
	 * Cambia la vista actual al menú principal
	 */
	public void goToLoadData();
	/**
	 * Muestra los errores ocurridos durante el intento de login
	 * @param validationErrors
	 */
	public void showLoginErrors(List<Exception> validationErrors);
}
