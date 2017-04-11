package com.elfec.cobranza.model.exceptions;
/**
 * Excepción que se lanza cuando un dispositivo cuyo IMEI no fue habilitado intenta ingresar al sistema
 * @author drodriguez
 *
 */
public class UnabledDeviceException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -3790297534925951450L;
	@Override
	public String getMessage()
	{
		return "Este dispositivo no fue habilitado para acceder a este sistema";
	}

}
