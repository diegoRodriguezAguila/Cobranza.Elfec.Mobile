package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter.OnCollectionAnnulmentCallback;
import com.elfec.cobranza.presenter.CollectionPaymentPresenter.OnPaymentConfirmedCallback;
import com.elfec.cobranza.presenter.services.BluetoothDevicePickerPresenter.OnBluetoothDevicePicked;

/**
 * Abstracción de la vista de pagar cobros
 * @author drodriguez
 *
 */
public interface ICollectionPaymentView extends ICollectionActionView {

	/**
	 * Muestra errores al imprimir la factura
	 * @param errors
	 */
	public void showPrintErrors(List<Exception> errors);
	/**
	 * Muestra errores al conectarse al bluetooth
	 * @param errors
	 */
	public void showBluetoothErrors(List<Exception> errors);
	/**
	 * Pide al usuario la confirmación para proceder con un pago
	 * @param selectedReceipts
	 * @param paymentCallback
	 */
	public void showPaymentConfirmation(List<CoopReceipt> selectedReceipts, OnPaymentConfirmedCallback paymentCallback);
	/**
	 * Pide al usuario que seleccione un dispositivo bluetooth para la impresión
	 * @param callback
	 */
	public void showBluetoothPrintDialog(OnBluetoothDevicePicked callback);
	/**
	 * Pide al usuario la confirmación para porceder con la anulación de un cobro
	 * @param selectedReceipts
	 * @param annulmentCallback
	 */
	public void showAnnulmentConfirmation(List<CoopReceipt> selectedReceipts, OnCollectionAnnulmentCallback annulmentCallback);
	/**
	 * Informa al usuario que se debe reconectar al suministro 
	 */
	public void showReconnectionMessage(String supplyNumber);
	
}
