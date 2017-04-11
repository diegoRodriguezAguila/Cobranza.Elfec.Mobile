package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.events.DatePickListener;
import com.elfec.cobranza.presenter.services.BluetoothDevicePickerPresenter.OnBluetoothDevicePicked;

/**
 * Abstracci�n de la vista del men� principal
 * @author drodriguez
 *
 */
public interface IMainMenuView {
	
	/**
	 * Muestra al usuario un dialogo de selecci�n de una fecha simple
	 * @param title
	 * @param iconId
	 * @param listener
	 */
	public void showSingleDatePicker(String title, int iconId, DatePickListener listener);
	/**
	 * Muestra al usuario un dialogo de selecci�n de un rango de fechas
	 * @param title
	 * @param iconId
	 * @param listener
	 */
	public void showDateRangePicker(String title, int iconId, DatePickListener listener);
	/**
	 * Pide al usuario que seleccione un dispositivo bluetooth para la impresi�n
	 * @param callback
	 */
	public void showBluetoothPrintDialog(OnBluetoothDevicePicked callback);
	/**
	 * Muestra errores al conectarse al bluetooth
	 * @param errors
	 */
	public void showBluetoothErrors(List<Exception> errors);
	/**
	 * Muestra errores al imprimir la factura
	 * @param errors
	 */
	public void showPrintErrors(List<Exception> errors);
}
