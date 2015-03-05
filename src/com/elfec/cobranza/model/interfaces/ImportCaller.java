package com.elfec.cobranza.model.interfaces;

import com.elfec.cobranza.model.DataAccessResult;

/**
 * Interfaz que sirve para llamar a los diferentes importers
 * @author drodriguez
 *
 */
public interface ImportCaller {

	/**
	 * Llama al metodo de importaci�n adecuado
	 * @return resultado importaci�n
	 */
	public DataAccessResult<?> callImport();
}
