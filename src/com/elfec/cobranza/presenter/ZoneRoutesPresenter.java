package com.elfec.cobranza.presenter;

import java.util.List;

import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.Zone;
import com.elfec.cobranza.presenter.views.IZoneRoutesView;

public class ZoneRoutesPresenter {

	private IZoneRoutesView view;

	public ZoneRoutesPresenter(IZoneRoutesView view) {
		this.view = view;
	}
	
	/**
	 * Carga las rutas de la zona según su id remoto
	 * @param zoneRemoteId
	 */
	public void loadZoneRoutes(final int zoneRemoteId)
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {	
				List<Route> zoneRoutes = Zone.findByRemoteId(zoneRemoteId).getZoneRoutes();
				view.setZoneRoutes(zoneRoutes);
			}
		});
		thread.start();
	}
}
