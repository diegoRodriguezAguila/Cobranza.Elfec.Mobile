package com.elfec.cobranza.model.exceptions;
/**
 * Excepci�n que se lanza cuando ocurri� un error al realizar un cobro
 * @author drodriguez
 *
 */
public class CollectionPaymentException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 663244117472226502L;
	
	private int receiptNumber;
	
	public CollectionPaymentException(int receiptNumber)
	{
		this.receiptNumber = receiptNumber;
	}
	
	@Override
	public String getMessage()
	{
		return "Ocurri� un error al guardar el cobro de la factura: "+receiptNumber;
	}
}
