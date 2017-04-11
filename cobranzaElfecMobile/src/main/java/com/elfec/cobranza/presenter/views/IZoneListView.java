package com.elfec.cobranza.presenter.views;

import com.elfec.cobranza.model.Zone;

import java.util.List;

/**
 * Abstracción de una vista de lista de zonas
 * @author drodriguez
 *
 */
public interface IZoneListView {

	/**
	 * Asigna las zonas que se mostrarán en la vista
	 * @param zones
	 */
	public void setZones(List<Zone> zones);
}
