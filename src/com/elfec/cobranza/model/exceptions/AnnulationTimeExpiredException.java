package com.elfec.cobranza.model.exceptions;

public class AnnulationTimeExpiredException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -2891692551677166721L;
	private int receiptNumber;
	private long controlCode;
	
	public AnnulationTimeExpiredException(int receiptNumber, long controlCode) {
		this.receiptNumber = receiptNumber;
		this.controlCode = controlCode;
	}

	@Override
	public String getMessage()
	{
		return "No se puede anular la factura N°: "+receiptNumber+"/"+controlCode+" porque se venció su plazo de anulación";
	}
}
