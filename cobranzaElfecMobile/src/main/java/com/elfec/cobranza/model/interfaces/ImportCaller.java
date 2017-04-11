package com.elfec.cobranza.model.interfaces;

import com.elfec.cobranza.model.results.DataAccessResult;

/**
 * Interfaz que sirve para llamar a los diferentes importers
 * @author drodriguez
 *
 */
public interface ImportCaller {

	/**
	 * Llama al metodo de importación adecuado
	 * @return resultado importación
	 */
	public DataAccessResult<?> callImport();
}
