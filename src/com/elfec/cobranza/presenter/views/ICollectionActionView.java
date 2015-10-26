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
	 * Esconde el mensaje de que no se realizó ninguna búsqueda
	 */
	public void hideNoSearchedSupplies();
	/**
	 * Muestra el mensaje de que no se realizó ninguna búsqueda
	 */
	public void showNoSearchedSupplies();
	/**
	 * Muestra un mensaje de espera de búsqueda
	 */
	public void showSearchingMessage();
	/**
	 * Oculta el mensaje de espera de búsqueda
	 */
	public void hideSearchingMessage();
	/**
	 * Muestra los errores que podrían haber ocurrido durante la búsqueda
	 * @param errors
	 */
	public void showSearchErrors(List<Exception> errors);
	/**
	 * Muestra la información del suministro
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
	 * Informa al usuario que se completó la acción exitosamente
	 */
	public void informActionSuccessfullyFinished();
	/**
	 * Muestra los errores ocurridos en la acción
	 * @param errors
	 */
	public void showActionErrors(List<Exception> errors);

	/**
	 * Pide al usuario la confirmación para porceder con la anulación de un cobro
	 * @param selectedReceipts
	 * @param annulmentCallback
	 */
	public void showAnnulmentConfirmation(List<CoopReceipt> selectedReceipts, OnCollectionAnnulmentCallback annulmentCallback);
}
