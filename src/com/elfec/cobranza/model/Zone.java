package com.elfec.cobranza.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
/**
 * Almacena las zonas 
 * @author drodriguez
 *
 */
@Table(name = "Zones")
public class Zone extends Model {

	@Column(name = "User", notNull=true)
	private User user;
	
	@Column(name = "ZoneRemoteId", notNull=true)
	private int zoneRemoteId;
	
	@Column(name = "Description")
	private String description;
	
	
	public Zone() {
		super();
	}	
	
	public Zone(User user, int zoneRemoteId, String description) {
		super();
		this.user = user;
		this.zoneRemoteId = zoneRemoteId;
		this.description = description;
	}
	
	//#region Getters y Setters

	
	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setZoneRemoteId(int zoneRemoteId) {
		this.zoneRemoteId = zoneRemoteId;
	}
	
	public int getZoneRemoteId() {
		return zoneRemoteId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//#endregion

	/**
	 * Obtiene las rutas de la zona
	 * @return lista de rutas de la zona
	 */
	public List<Route> getZoneRoutes()
	{
		return getMany(Route.class, "Zone");	
	}
}
