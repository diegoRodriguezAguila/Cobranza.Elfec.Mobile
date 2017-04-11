package com.elfec.cobranza.presenter;

import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.Zone;
import com.elfec.cobranza.presenter.views.IZoneListView;

import java.util.List;

public class ZoneListPresenter {

	private IZoneListView view;
	private Zone[] directAccessZones;

	public ZoneListPresenter(IZoneListView view) {
		this.view = view;
	}
	
	/**
	 * Carga las zonas asignadas al usuario
	 */
	public void loadUserAssignedZones()
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {	
				List<Zone> zones =  User.findByUserName(SessionManager.getLoggedInUsername()).getAssignedZones();
				directAccessZones = new Zone[zones.size()];
				directAccessZones = zones.toArray(directAccessZones);
				view.setZones(zones);
			}
		});
		thread.start();
	}
	
	/**
	 * Obtiene el id remoto de la zona en la posición especificada
	 * @param position
	 * @return
	 */
	public int getZoneRemoteId(int position)
	{
		return directAccessZones[position].getZoneRemoteId();
	}
	
}
