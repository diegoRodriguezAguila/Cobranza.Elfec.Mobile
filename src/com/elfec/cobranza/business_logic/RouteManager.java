package com.elfec.cobranza.business_logic;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Route;

/**
 * Se encarga de las operaciones de negocio de rutas
 * @author drodriguez
 *
 */
public class RouteManager {
	
	/**
	 * Verifica que se tenga al menos una ruta cargada al dispositivo
	 * @return
	 */
	public static boolean isOneRouteLoaded()
	{
		return Route.getFirstLoadedRoute()!=null;
	}

	/**
	 * Valida si es que la ruta tiene alguna factura y por ende debe marcarse como cargada y/o bloqueada
	 * @param route
	 * @return true si es que la ruta tiene alguna factura
	 */
	public static boolean hasRouteReceipts(Route route)
	{
		return CoopReceipt.findRouteReceipts(route.getRouteRemoteId()).size()>0;
	}

}
