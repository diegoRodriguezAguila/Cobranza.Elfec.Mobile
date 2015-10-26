package com.elfec.cobranza.model.exceptions;
/**
 * Excepci�n que se lanza cuando ya no existe informaci�n pendiente para exportar
 * @author drodriguez
 *
 */
public class NoPendingExportDataException extends Exception{

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -6073911036665973850L;
	@Override
	public String getMessage()
	{
		return "No existe informaci�n pendiente para descargar al servidor!";
	}
}
