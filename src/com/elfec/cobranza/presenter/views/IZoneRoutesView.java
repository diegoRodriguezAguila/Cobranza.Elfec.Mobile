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
}
