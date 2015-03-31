package com.elfec.cobranza.business_logic.printer;

import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.SessionManager;

/**
 * Genera reportes que dependen de un rango de fechas
 * @author drodriguez
 *
 */
public abstract class DateRangeReportGenerator extends BaseReportGenerator {
	
	protected final String FROM_DATES = "DE FECHA: %s A: %s";
	
	protected final String CASH_DESK = "CAJA: %d %s";
	
	/**
	 * Fecha de inicio del reporte
	 */
	protected DateTime startDate;
	/**
	 * Fecha de fin del reporte
	 */
	protected DateTime endDate;
	
	public DateRangeReportGenerator(DateTime startDate, DateTime endDate)
	{
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * Asigna la parte de la cabecera del reporte
	 */
	@Override
	protected void assignHeader() {
		super.assignHeader();
		command.text(0, receiptHeight+=0.5, String.format(FROM_DATES,
				startDate.toString("dd/MM/yyyy"), endDate.toString("dd/MM/yyyy")))
		.text(0, receiptHeight+=0.4, String.format(CASH_DESK, SessionManager.getLoggedCashdeskNumber(),
				SessionManager.getLoggedCashdeskDesc()));
	}
}
