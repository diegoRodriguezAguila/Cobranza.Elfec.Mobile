package com.elfec.cobranza.model.exceptions;

/**
 * Excepción que se lanza cuando el tiempo limite para la anulación de una factura expiró
 * @author drodriguez
 *
 */
public class AnnulationTimeExpiredException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -2891692551677166721L;
	private int receiptNumber;
	private long internalControlCode;
	
	public AnnulationTimeExpiredException(int receiptNumber, long internalControlCode) {
		this.receiptNumber = receiptNumber;
		this.internalControlCode = internalControlCode;
	}

	@Override
	public String getMessage()
	{
		return "No se puede anular la factura N°: "+receiptNumber+"/"+internalControlCode+" porque se venció su plazo de anulación";
	}
}
