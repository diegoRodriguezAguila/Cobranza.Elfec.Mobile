package com.elfec.cobranza.model.exceptions;
/**
 * Excepción que se lanza cuando no se pasó un comportamiento en el presenter de las cobranzas
 * @author drodriguez
 *
 */
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
