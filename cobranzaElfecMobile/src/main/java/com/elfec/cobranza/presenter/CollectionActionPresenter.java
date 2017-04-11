package com.elfec.cobranza.presenter;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.views.ICollectionActionView;

import java.util.List;

public abstract class CollectionActionPresenter {
	
	protected ICollectionActionView view;
	protected Supply currentSupply;

	public CollectionActionPresenter(ICollectionActionView view) {
		this.view = view;
	}
	
	/**
	 * Carga las facturas del suministro
	 * @param supply
	 */
	public abstract void loadSupplyReceipts(Supply supply);
	
	/**
	 * Procesa la acción con el comportamiento definido actual
	 * @param selectedReceipts
	 */
	public abstract void processAction(final List<CoopReceipt> selectedReceipts);
	
}
