package com.elfec.cobranza.model.events;

/**
 * Clase que informa de los eventos que ocurren durante la exportación de datos
 * @author drodriguez
 *
 */
public interface DataExportListener {

	/**
	 * Se ejecuta el momento en que se obtuvieron todos los datos para exportar
	 * y se esta por empezar la exportación
	 * @param totalElements total de datos a exportar
	 */
	public void onExportInitialized(int totalElements);
	
	/**
	 * Se ejecuta cada que se exporta una fila
	 * @param exportCount datos exportados hasta el momento
	 * @param totalElements total de datos a exportar
	 */
	public void onExporting(int exportCount, int totalElements);
	
	/**
	 * Se ejecuta el momento en que se finaliza la exportación, ya sea exitosa o fallidamente
	 * y se esta por empezar la exportación
	 * @param totalElements
	 */
	public void onExportFinalized();
}
