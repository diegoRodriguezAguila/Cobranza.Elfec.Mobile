package com.elfec.cobranza.model.exceptions;

/**
 * Excepci�n que se lanza cuando no existen cobros descargados al dispositivo
 * @author drodriguez
 *
 */
public class NoPaidCollectionsException extends Exception{

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1232503305239940397L;
	@Override
	public String getMessage()
	{
		return "No se realiz� ning�n cobro, asegurese de haber cargado datos al tel�fono y realizado cobros antes de intentar descargar al servidor!";
	}
}
