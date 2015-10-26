package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter.OnCollectionAnnulmentCallback;

/**
 * Abstracción de la vista de anulación cobros
 * @author drodriguez
 *
 */
public interface ICollectionAnnulmentView extends ICollectionActionView{
	/**
	 * Pide al usuario la confirmación para porceder con la anulación de un cobro
	 * @param selectedReceipts
	 * @param annulmentCallback
	 */
	public void showAnnulmentConfirmation(List<CoopReceipt> selectedReceipts, OnCollectionAnnulmentCallback annulmentCallback);
}
