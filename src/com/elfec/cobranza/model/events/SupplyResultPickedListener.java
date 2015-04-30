package com.elfec.cobranza.model.events;

import com.elfec.cobranza.model.Supply;
/**
 * Evento utilizado por el di�logo de selecci�n de resultados de suministros
 * @author drodriguez
 *
 */
public interface SupplyResultPickedListener {

	/**
	 * Se llama cuando se selecciona un resultado de suministro
	 * @param supply
	 */
	public void onSupplyResultPicked(Supply supply);
	/**
	 * Se llama si es que se cancela la selecci�n de resultados de suministros
	 */
	public void onSupplyResultPickCanceled();
}
