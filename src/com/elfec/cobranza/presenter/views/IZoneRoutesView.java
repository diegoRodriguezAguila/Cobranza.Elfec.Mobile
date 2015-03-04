package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.Route;

/**
 * Abstracción de una vista de las rutas de una zona
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
	 * Muestra los errores en la importación
	 * @param errors
	 */
	public void showImportErrors(List<Exception> errors);
	/**
	 * Indica al usuario que la importación fué exitosa
	 */
	public void successfullyImportation();
}
