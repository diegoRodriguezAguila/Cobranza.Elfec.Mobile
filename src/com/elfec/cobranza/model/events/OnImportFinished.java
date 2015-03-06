package com.elfec.cobranza.model.events;

import com.elfec.cobranza.model.DataAccessResult;

/**
 * Evento que se llama cuando se finalizó una importación
 * @author drodriguez
 *
 */
public interface OnImportFinished {

	/**
	 * La función que se llama cuando finalizó un evento de importación de datos
	 * @param result
	 */
	public void importCallback(DataAccessResult<?> result);
}
