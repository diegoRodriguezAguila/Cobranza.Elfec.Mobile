package com.elfec.cobranza.model.exceptions;

/**
 * Excepción que se lanza cuando un usuario que itenta logearse no tiene una caja asignada
 * @author drodriguez
 *
 */
public class UnassignedCashDeskException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1945399843496970358L;
	
	private String username;
	public UnassignedCashDeskException(String username) {
		this.username = username;
	}
	@Override
	public String getMessage()
	{
		return "El usuario <b>"+username+"</b> no tiene asignada una caja o el perfil de cobranza <b>offline</b>.";
	}
}
