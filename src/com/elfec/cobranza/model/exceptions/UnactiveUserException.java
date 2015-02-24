package com.elfec.cobranza.model.exceptions;
/**
 * Excepción que se lanza cuando un usuario en estado de baja intenta ingresar al sistema
 * @author drodriguez
 *
 */
public class UnactiveUserException extends Exception {

	/**
	 * serial version
	 */
	private static final long serialVersionUID = 3982877511116964558L;
	private String username;
	public UnactiveUserException(String username) {
		this.username = username;
	}
	@Override
	public String getMessage()
	{
		return "El usuario <b>"+username+"</b> no tiene permiso para acceder a este sistema.";
	}

}
