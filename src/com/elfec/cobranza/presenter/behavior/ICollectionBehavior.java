package com.elfec.cobranza.presenter.behavior;

import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;

/**
 * Determina un comportamiento para la vista de cobranzas
 * @author drodriguez
 *
 */
public interface ICollectionBehavior {

	/**
	 * Obtiene los recibos del suministro según lo que se quiera mostrar
	 * @param supply
	 * @return
	 */
	public List<CoopReceipt> getSupplyReceipts(Supply supply);
}
