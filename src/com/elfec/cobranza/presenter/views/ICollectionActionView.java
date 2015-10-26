package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.CollectionActionPresenter;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter.OnCollectionAnnulmentCallback;
import com.elfec.cobranza.presenter.adapter_interfaces.ICollectionBaseAdapter;

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
	 * Muestra el mensaje de que no se realiz� ninguna b�squeda
	 */
	public void showNoSearchedSupplies();
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
	public void setCollectionAdapter(ICollectionBaseAdapter collectionAdapter);
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
	 * Pide al usuario la confirmaci�n para porceder con la anulaci�n de un cobro
	 * @param selectedReceipts
	 * @param annulmentCallback
	 */
	public void showAnnulmentConfirmation(List<CoopReceipt> selectedReceipts, OnCollectionAnnulmentCallback annulmentCallback);
}
