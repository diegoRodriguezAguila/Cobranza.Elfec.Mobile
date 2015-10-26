package com.elfec.cobranza.model.exceptions;
/**
 * Excepción que se lanza cuando ocurrió un error al realizar una acción sobre un cobro
 * @author drodriguez
 *
 */
public class CollectionException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 663244117472226502L;
	
	private int receiptNumber;
	/**
	 * Excepción que se lanza cuando ocurrió un error al realizar una acción sobre un cobro
	 * @author drodriguez
	 *
	 */
	public CollectionException(int receiptNumber)
	{
		this.receiptNumber = receiptNumber;
	}
	
	@Override
	public String getMessage()
	{
		return "Ocurrió un error al guardar la factura: "+receiptNumber;
	}
}
