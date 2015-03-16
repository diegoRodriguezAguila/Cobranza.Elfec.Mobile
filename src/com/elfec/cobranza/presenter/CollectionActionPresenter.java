package com.elfec.cobranza.presenter;

import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.behavior.ICollectionBehavior;
import com.elfec.cobranza.presenter.views.ICollectionActionView;

public class CollectionActionPresenter {
	
	private ICollectionActionView view;
	private ICollectionBehavior collectionBehavior;

	public CollectionActionPresenter(ICollectionActionView view) {
		this.view = view;
	}
	
	/**
	 * Asigna el comportamiento del presenter
	 * @param collectionBehavior
	 */
	public void setCollectionBehavior(ICollectionBehavior collectionBehavior)
	{
		this.collectionBehavior = collectionBehavior;
	}
	
	/**
	 * Carga las facturas del suministro
	 * @param supply
	 */
	public void loadSupplyReceipts(Supply supply)
	{	
		view.showReceipts(collectionBehavior.getSupplyReceipts(supply));
	}
	
}
