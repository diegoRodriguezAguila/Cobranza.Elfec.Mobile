package com.elfec.cobranza.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
/**
 * Almacena las rutas 
 * @author drodriguez
 *
 */
@Table(name = "Routes")
public class Route extends Model {
	@Column(name = "Zone", notNull=true)
	private Zone zone;
	
	@Column(name = "RouteRemoteId", notNull=true)
	private int routeRemoteId;
	
	@Column(name = "Description")
	private String description;

	public Route() {
		super();
	}
	
	public Route(Zone zone, int routeRemoteId, String description) {
		super();
		this.zone = zone;
		this.routeRemoteId = routeRemoteId;
		this.description = description;
	}

	//#region Getters y Setters
	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public int getRouteRemoteId() {
		return routeRemoteId;
	}

	public void setRouteRemoteId(int routeRemoteId) {
		this.routeRemoteId = routeRemoteId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	//#endregion
	
}
