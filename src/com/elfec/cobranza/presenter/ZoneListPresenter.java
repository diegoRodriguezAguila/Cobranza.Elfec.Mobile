package com.elfec.cobranza.presenter;

import java.util.List;

import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.Zone;
import com.elfec.cobranza.presenter.views.IZoneListView;

public class ZoneListPresenter {

	private IZoneListView view;

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
				User usr = User.findByUserName(SessionManager.getLoggedInUsername());
				List<Zone> zones = usr.getAssignedZones();
				view.setZones(zones);
			}
		});
		thread.start();
	}
	
}
