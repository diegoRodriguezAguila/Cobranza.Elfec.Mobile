package com.elfec.cobranza.model.exceptions;
/**
 * Excepción que se lanza cuando ocurrió un error al realizar un cobro
 * @author drodriguez
 *
 */
public class CollectionPaymentException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 663244117472226502L;
	
	private int receiptNumber;
	/**
	 * Excepción que se lanza cuando ocurrió un error al realizar un cobro
	 * @author drodriguez
	 *
	 */
	public CollectionPaymentException(int receiptNumber)
	{
		this.receiptNumber = receiptNumber;
	}
	
	@Override
	public String getMessage()
	{
		return "Ocurrió un error al guardar el cobro de la factura: "+receiptNumber;
	}
}
