package com.elfec.cobranza.model.exceptions;
/**
 * Excepci�n que se lanza cuando no se pas� un comportamiento en el presenter de las cobranzas
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
		return "No se sete� un comportamiento para el presenter, utilice el m�todo setCollectionBehavior para asignar"
				+ "un presentador!";
	}
}
