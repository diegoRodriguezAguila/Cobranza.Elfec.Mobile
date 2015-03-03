package com.elfec.cobranza.helpers.text_format;

import java.util.List;

import com.elfec.cobranza.model.Route;

/**
 * Convierte una lista de rutas a una lista aplicable a la consulta IN de SQL
 * con el formato (1234,125534,1234)
 * @author drodriguez
 *
 */
public class RouteListToSQL {

	/**
	 * Convierte la lista de rutas a la cadena utilizable en una consulta IN
	 * @param routes
	 * @return
	 */
	public static String convertToSQL(List<Route> routes)
	{
		StringBuilder query = new StringBuilder("(");
		for(Route route : routes)
		{
			query.append(route.getRouteRemoteId()).append(",");
		}
		query.setCharAt(query.length()-1, ')');
		return query.toString();
	}
}
