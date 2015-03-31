package com.elfec.cobranza.business_logic.printer;

import com.elfec.cobranza.model.printer.CPCLCommand;

/**
 * Interfaz que define genéricamente un reporte
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
	 * Genera el comando cpcl para impresión
	 * @return comando cpcl
	 */
	public CPCLCommand generateCommand();
}
