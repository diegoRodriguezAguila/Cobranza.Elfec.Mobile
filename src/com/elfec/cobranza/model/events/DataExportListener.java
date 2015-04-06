package com.elfec.cobranza.model.events;

/**
 * Clase que informa de los eventos que ocurren durante la exportaci�n de datos
 * @author drodriguez
 *
 */
public interface DataExportListener {

	/**
	 * Se ejecuta el momento en que se obtuvieron todos los datos para exportar
	 * y se esta por empezar la exportaci�n
	 * @param totalElements
	 */
	public void onExportInitializing(int totalElements);
	
	/**
	 * Se ejecuta cada que se exporta una fila
	 * @param exportCount
	 * @param totalElements
	 */
	public void onExporting(int exportCount, int totalElements);
	
	/**
	 * Se ejecuta el momento en que se finaliza la exportaci�n, ya sea exitosa o fallidamente
	 * y se esta por empezar la exportaci�n
	 * @param totalElements
	 */
	public void onExportFinalized();
}
