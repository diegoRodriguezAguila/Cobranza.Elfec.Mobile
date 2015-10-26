package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.Zone;

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
