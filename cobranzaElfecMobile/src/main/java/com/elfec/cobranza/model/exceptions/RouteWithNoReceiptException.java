package com.elfec.cobranza.model.exceptions;
/**
 * Excepci�n que se lanza cuando se intent� importar una ruta que no tiene facturas pendientes
 * @author drodriguez
 *
 */
public class RouteWithNoReceiptException extends RuntimeException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String getMessage()
	{
		return "La(s) ruta(s) seleccionada(s) no tiene(n) ninguna factura pendiente!";
	}
}
