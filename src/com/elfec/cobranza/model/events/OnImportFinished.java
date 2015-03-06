package com.elfec.cobranza.model.events;

import com.elfec.cobranza.model.DataAccessResult;

/**
 * Evento que se llama cuando se finaliz� una importaci�n
 * @author drodriguez
 *
 */
public interface OnImportFinished {

	/**
	 * La funci�n que se llama cuando finaliz� un evento de importaci�n de datos
	 * @param result
	 */
	public void importCallback(DataAccessResult<?> result);
}
