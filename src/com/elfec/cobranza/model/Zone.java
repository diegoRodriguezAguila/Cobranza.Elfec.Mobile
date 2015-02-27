package com.elfec.cobranza.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
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
	
	private List<Route> zoneRoutes;
	
	
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
	 * Accede a la base de datos y obtiene la zona con su id remoto 
	 * @param zoneRemoteId el id de la zona en oracle
	 * @return La zona que corresponde a ese Id, null si no existe
	 * **/
	public static Zone findByRemoteId(int zoneRemoteId) {
	    return new Select()
	        .from(Zone.class).where("ZoneRemoteId=?", zoneRemoteId)
	        .executeSingle();
	}
	
	/**
	 * Obtiene las rutas de la zona
	 * @return lista de rutas de la zona
	 */
	public List<Route> getZoneRoutes()
	{
		if(zoneRoutes==null)
			zoneRoutes = getMany(Route.class, "Zone");	
		return zoneRoutes;
	}
}
