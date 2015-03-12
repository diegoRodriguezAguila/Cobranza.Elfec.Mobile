package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.PaymentCollectionPresenter;

/**
 * Abstracción de la vista de pagar cobros
 * @author drodriguez
 *
 */
public interface IPaymentCollectionView {

	/**
	 * Obtiene el presenter de la vista
	 * @return su presenter
	 */
	public PaymentCollectionPresenter getPresenter();
	/**
	 * Esconde el mensaje de que no se realizó ninguna búsqueda
	 */
	public void hideNoSearchedSupplies();
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
}
