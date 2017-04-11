package com.elfec.cobranza.business_logic.printer;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.helpers.utils.AmountsCounter;
import com.elfec.cobranza.model.printer.CPCLCommand.Justify;

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
	
	/**
	 * Asigna la parte del pie del reporte
	 */
	@Override
	protected void assignFooter() {
		command.setFont("TAHOMA8P.CPF")
		.setBold(0.025).setSpacing(0.025)
		.justify(Justify.LEFT)
		.text(0.4, receiptHeight+=0.2, String.format(getTotalNum(), getTotalReceiptsNum()))
		.justify(Justify.RIGHT, 10.05)
		.text(0, receiptHeight, String.format(getTotalAmountNum(), 
				AmountsCounter.formatBigDecimal(getTotalReceiptsAmount())))
		.setBold(0).setSpacing(0);
		super.assignFooter();
	}
	
	/**
	 * El mensaje que se muestra para cantidades totales
	 */
	protected abstract String getTotalNum();
	/**
	 * La cantidad total de facturas del reporte
	 */
	protected abstract int getTotalReceiptsNum();
	
	/**
	 * El mensaje que se muestra para montos totales
	 */
	protected abstract String getTotalAmountNum();
	/**
	 * El monto total de las facturas
	 */
	protected abstract BigDecimal getTotalReceiptsAmount();
}
