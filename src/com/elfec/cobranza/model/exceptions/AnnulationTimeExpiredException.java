package com.elfec.cobranza.model.exceptions;

/**
 * Excepci�n que se lanza cuando el tiempo limite para la anulaci�n de una factura expir�
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
		return "No se puede anular la factura N�: "+receiptNumber+"/"+internalControlCode+" porque se venci� su plazo de anulaci�n";
	}
}
