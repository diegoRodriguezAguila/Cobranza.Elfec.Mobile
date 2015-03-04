package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.Route;

/**
 * Abstracci�n de una vista de las rutas de una zona
 * @author drodriguez
 *
 */
public interface IZoneRoutesView {

	/**
	 * Hace que la vista muestre la lsita de rutas de una zona
	 * @param zoneRoutes
	 */
	public void setZoneRoutes(List<Route> zoneRoutes);
	/**
	 * Indica al usuario que debe esperar
	 */
	public void showWaiting();
	/**
	 * Actualiza el mensaje de espera
	 * @param message
	 */
	public void updateWaitingMessage(int messageResource);
	/**
	 * Oculta el mensaje de espera
	 */
	public void hideWaiting();
	/**
	 * Muestra los errores en la importaci�n
	 * @param errors
	 */
	public void showImportErrors(List<Exception> errors);
	/**
	 * Indica al usuario que la importaci�n fu� exitosa
	 */
	public void successfullyImportation();
}
