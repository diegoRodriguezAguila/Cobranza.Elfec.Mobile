package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter.OnCollectionAnnulmentCallback;

/**
 * Abstracci�n de la vista de anulaci�n cobros
 * @author drodriguez
 *
 */
public interface ICollectionAnnulmentView extends ICollectionActionView{
	/**
	 * Pide al usuario la confirmaci�n para porceder con la anulaci�n de un cobro
	 * @param selectedReceipts
	 * @param annulmentCallback
	 */
	public void showAnnulmentConfirmation(List<CoopReceipt> selectedReceipts, OnCollectionAnnulmentCallback annulmentCallback);
}
