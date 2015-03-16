package com.elfec.cobranza.model.exceptions;

public class NoBehaviorSetException extends RuntimeException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -1636252336687412708L;
	@Override
	public String getMessage()
	{
		return "No se seteó un comportamiento para el presenter, utilice el método setCollectionBehavior para asignar"
				+ "un presentador!";
	}
}
