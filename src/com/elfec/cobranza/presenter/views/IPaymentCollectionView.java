package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.PaymentCollectionPresenter;

/**
 * Abstracci�n de la vista de pagar cobros
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
}
