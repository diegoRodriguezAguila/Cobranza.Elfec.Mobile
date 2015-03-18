package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.CollectionActionPresenter;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter.OnCollectionAnnulmentCallback;
import com.elfec.cobranza.presenter.CollectionPaymentPresenter.OnPaymentConfirmedCallback;
import com.elfec.cobranza.view.adapters.collection.CollectionBaseAdapter;

/**
 * Abstracci�n de la vista de pagar cobros
 * @author drodriguez
 *
 */
public interface ICollectionActionView {

	/**
	 * Obtiene el presenter de la vista
	 * @return su presenter
	 */
	public CollectionActionPresenter getPresenter();
	/**
	 * Esconde el mensaje de que no se realiz� ninguna b�squeda
	 */
	public void hideNoSearchedSupplies();
	/**
	 * Muestra un mensaje de espera de b�squeda
	 */
	public void showSearchingMessage();
	/**
	 * Oculta el mensaje de espera de b�squeda
	 */
	public void hideSearchingMessage();
	/**
	 * Muestra los errores que podr�an haber ocurrido durante la b�squeda
	 * @param errors
	 */
	public void showSearchErrors(List<Exception> errors);
	/**
	 * Muestra la informaci�n del suministro
	 * @param supply
	 */
	public void showSupplyInfo(Supply supply);
	/**
	 * Muestra las facturas del suministro
	 * @param receipts
	 */
	public void showReceipts(List<CoopReceipt> receipts);
	/**
	 * Asigna el adapter de cobranza
	 * @param collectionAdapter
	 */
	public void setCollectionAdapter(CollectionBaseAdapter collectionAdapter);
	/**
	 * Informa al usuario que se complet� la acci�n exitosamente
	 */
	public void informActionSuccessfullyFinished();
	/**
	 * Muestra los errores ocurridos en la acci�n
	 * @param errors
	 */
	public void showActionErrors(List<Exception> errors);
	/**
	 * Pide al usuario la confirmaci�n para proceder con un pago
	 * @param selectedReceipts
	 * @param paymentCallback
	 */
	public void showPaymentConfirmation(List<CoopReceipt> selectedReceipts, OnPaymentConfirmedCallback paymentCallback);
	/**
	 * Pide al usuario la confirmaci�n para porceder con la anulaci�n de un cobro
	 * @param selectedReceipts
	 * @param annulmentCallback
	 */
	public void showAnnulmentConfirmation(List<CoopReceipt> selectedReceipts, OnCollectionAnnulmentCallback annulmentCallback);
}
