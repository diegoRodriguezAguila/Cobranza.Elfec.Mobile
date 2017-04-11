package com.elfec.cobranza.model.exceptions;

public class NoPeriodBankAccountException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -8138101956697241227L;
	private int cashDeskNumber;
	public NoPeriodBankAccountException(int cashDeskNumber) {
		this.cashDeskNumber = cashDeskNumber;
	}
	@Override
	public String getMessage()
	{
		return "El número de caja "+cashDeskNumber+" no tiene asignado ningún periodo de caja, para el día de hoy!";
	}
}
