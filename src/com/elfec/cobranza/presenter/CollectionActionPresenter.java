package com.elfec.cobranza.presenter;

import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.views.ICollectionActionView;

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
