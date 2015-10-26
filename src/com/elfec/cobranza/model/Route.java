package com.elfec.cobranza.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
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
	
	@Column(name = "IsLoaded")
	private boolean isLoaded;
	
	@Column(name = "LockRemoteId")
	private long lockRemoteId;

	public Route() {
		super();
	}
	
	public Route(Zone zone, int routeRemoteId, String description) {
		super();
		this.zone = zone;
		this.routeRemoteId = routeRemoteId;
		this.description = description;
		this.isLoaded = false;
	}
	
	/**
	 * Obtiene todas las rutas cargadas en el dipositivo
	 * @return Lista de rutas cargadas en el dispositivo
	 */
	public static List<Route> getAllLoadedRoutes()
	{
		return new Select().from(Route.class)
				.where("IsLoaded=1").execute();
	}
	
	/**
	 * Obtiene una ruta cargada en el dipositivo
	 * @return La primera ruta cargada
	 */
	public static Route getFirstLoadedRoute()
	{
		return new Select().from(Route.class)
				.where("IsLoaded=1").executeSingle();
	}
	
	/**
	 * Busca una ruta por su Id Remoto
	 * @param routeRemoteId
	 * @return la ruta encontrada o NULL
	 */
	public static Route findRouteByRemoteId(int routeRemoteId)
	{
		return new Select().from(Route.class)
				.where("RouteRemoteId=?",routeRemoteId).executeSingle();
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

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	public long getLockRemoteId() {
		return lockRemoteId;
	}

	public void setLockRemoteId(long lockRemoteId) {
		this.lockRemoteId = lockRemoteId;
	}
	
	
	
	//#endregion
	
}
