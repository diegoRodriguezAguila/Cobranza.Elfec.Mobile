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
	 * Obtiene el IMEI del dispositivo
	 * @return
	 */
	public String getIMEI();
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
	 * Indica al usuario que debe esperar a que se eliminen los datos
	 */
	public void showWipingDataWait();
	/**
	 * Acualiza el mensaje de espera del usuario
	 * @param strId
	 */
	public void updateWaiting(int strId);
	/**
	 * Acualiza el mensaje de espera del usuario a un mensaje de carga con porcentaje total de datos
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
	 * Muestra al usuario errores en la eliminación de todos los datos
	 * @param errors
	 */
	public void showWipeAllDataErrors(List<Exception> errors);
	
	/**
	 * Informa al usuario que se completó exitosamente la descarga
	 */
	public void notifySuccessfulDataExportation();
	
	/**
	 * Informa al usuario que se eliminaron los datos exitosamente
	 */
	public void notifySuccessfulDataWipe();
	/**
	 * Lleva al menú principal
	 */
	public void goToMainMenu();
	/**
	 * Indica al usuario que se bloqueó el acceso al menú principal
	 * por no haber cargado los datos
	 */
	public void showMainMenuAccessLocked();
}
