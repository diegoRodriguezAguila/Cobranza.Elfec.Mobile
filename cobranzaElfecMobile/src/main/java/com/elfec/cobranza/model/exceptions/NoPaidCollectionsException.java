package com.elfec.cobranza.model.exceptions;

/**
 * Excepción que se lanza cuando no existen cobros descargados al dispositivo
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
		return "No se realizó ningún cobro, asegurese de haber cargado datos al teléfono y realizado cobros antes de intentar descargar al servidor!";
	}
}
