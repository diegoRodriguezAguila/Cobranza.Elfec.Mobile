package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.events.OnRoutesImportConfirmed;

/**
 * Abstracción de una vista de las rutas de una zona
 * @author drodriguez
 *
 */
public interface IZoneRoutesView {

	/**
	 * Hace que la vista muestre la lista de rutas de una zona
	 * @param zoneRoutes
	 */
	public void setZoneRoutes(List<Route> zoneRoutes);
	/**
	 * Indica al usuario que debe esperar
	 */
	public void showWaiting();
	/**
	 * Actualiza el mensaje de espera
	 * @param messageResource
	 * @param replaceAll si es que debe reemplazar el o los mensajes actuales por ese
	 */
	public void addWaitingMessage(int messageResource, boolean replaceAll);
	/**
	 * Indica dejar de mostrar un mensaje específico de espera
	 * @param messageResource
	 */
	public void deleteWaitingMessage(int messageResource);
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
	 * Obtiene el IMEI del dispositivo
	 * @return
	 */
	public String getIMEI();
	/**
	 * Advierte al usuario que existen rutas que fueron cargadas en otros dispositivos y pide
	 * su confirmación para continuar
	 * @param lockedRoutes
	 * @param noMoreRoutes indica si es que hay mas rutas para descargar aparte de las que
	 * se omitirán en caso de continuar
	 */
	public void warnLockedRoutes(List<Route> lockedRoutes, OnRoutesImportConfirmed callback, boolean noMoreRoutes);
	/**
	 * Indica al usuario que la importación fué exitosa
	 */
	public void successfullyImportation();
}
