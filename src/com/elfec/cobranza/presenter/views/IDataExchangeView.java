package com.elfec.cobranza.presenter.views;

import java.util.List;

public interface IDataExchangeView {

	/**
	 * Muestra al usuario actual
	 * @param username
	 */
	public void setCurrentUser(String username);
	/**
	 * Asigna el numero de caja del usuario
	 * @param cashdeskNumber
	 */
	public void setCashdeskNumber(int cashdeskNumber);
	/**
	 * Notifica al usuario que se cerró la sesión
	 * @param username
	 */
	public void notifySessionClosed(String username);
	/**
	 * Indica al usuario que debe esperar
	 */
	public void showWaiting();
	/**
	 * Acualiza el mensaje de espera del usuario
	 * @param strId
	 * @param totalData
	 */
	public void updateWaiting(int strId, int totalData);
	/**
	 * Acualiza el mensaje de la barra de progreso
	 * @param dataCount
	 * @param totalData
	 */
	public void updateProgress(int dataCount, int totalData);
	/**
	 * Borra el mensaje de espera
	 */
	public void hideWaiting();
	/**
	 * Muestra al usuario errores en la exportación de datos
	 * @param errors
	 */
	public void showExportationErrors(List<Exception> errors);
	
	/**
	 * Informa al usuario que se completó exitosamente la descarga
	 */
	public void showSuccessfulDataExportation();
}
