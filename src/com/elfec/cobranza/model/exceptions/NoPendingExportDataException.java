package com.elfec.cobranza.model.exceptions;
/**
 * Excepción que se lanza cuando ya no existe información pendiente para exportar
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
		return "No existe información pendiente para descargar al servidor!";
	}
}
