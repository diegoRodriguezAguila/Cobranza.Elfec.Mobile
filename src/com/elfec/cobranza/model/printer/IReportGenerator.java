package com.elfec.cobranza.model.printer;


/**
 * Interfaz que define gen�ricamente un reporte
 * @author drodriguez
 *
 */
public interface IReportGenerator {
	/**
	 * Obtiene el nombre del reporte
	 * @return Nombre del reporte
	 */
	public String getReportName();
	/**
	 * Genera el comando cpcl para impresi�n
	 * @return comando cpcl
	 */
	public CPCLCommand generateCommand();
}
