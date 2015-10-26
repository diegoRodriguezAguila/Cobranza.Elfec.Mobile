package com.elfec.cobranza.model.exceptions;
/**
 * Excepci�n que se lanza cuando ocurri� un error al realizar una acci�n sobre un cobro
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
	 * Excepci�n que se lanza cuando ocurri� un error al realizar una acci�n sobre un cobro
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
		return "Ocurri� un error al guardar la factura: "+receiptNumber;
	}
}
