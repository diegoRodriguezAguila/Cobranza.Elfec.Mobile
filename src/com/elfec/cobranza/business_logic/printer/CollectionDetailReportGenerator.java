package com.elfec.cobranza.business_logic.printer;

import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.PrinterImagesManager;
import com.elfec.cobranza.model.printer.CPCLCommand;
import com.elfec.cobranza.model.printer.CPCLCommand.Justify;
import com.elfec.cobranza.model.printer.CPCLCommand.Unit;

/**
 * Clase que se encarga de generar el comando de impresión del reporte
 * de detalle de cobranzas
 * @author drodriguez
 *
 */
public class CollectionDetailReportGenerator implements IReportGenerator {
	
	/**
	 * El nombre del reporte
	 */
	private static final String REPORT_NAME = "REPORTE DETALLE DE COBRANZA";
	
	/**
	 * El código del reporte
	 */
	private static final String REPORT_CODE = "GAF-714-03";

	/**
	 * El tamaño del reporte en cm
	 */
	private double receiptHeight;
	
	/**
	 * El comando que se está generando
	 */
	private CPCLCommand command;
	
	/**
	 * Fecha de inicio del reporte
	 */
	private DateTime startDate;
	/**
	 * Fecha de fin del reporte
	 */
	private DateTime endDate;
	
	public CollectionDetailReportGenerator(DateTime startDate, DateTime endDate)
	{
		receiptHeight = 0;
		command = new CPCLCommand(200, 400, 3).inUnit(Unit.IN_CENTIMETERS );
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/**
	 * Genera el comando cpcl de impresión del reporte
	 * @param receipt
	 * @return el comando cpcl generado
	 */
	@Override
	public CPCLCommand generateCommand()
	{
		assignHeader();
		command.setLabelHeight(receiptHeight+0.8);
		command.print();
		return command;
	}

	private void assignHeader() {
		command.justify(Justify.LEFT)
		.image(0.4, receiptHeight, PrinterImagesManager.REP_LOGO_IN_PRINTER_NAME)
		.justify(Justify.RIGHT, 10.05).setFont("TAHOMA8P.CPF")
		.text(0, 7, receiptHeight, REPORT_CODE)
		.justify(Justify.CENTER).text("TAHOMA11.CPF", 0, 0, receiptHeight+=0.4, 0.035, 0.035, REPORT_NAME);
	}

	@Override
	public String getReportName() {
		return REPORT_NAME;
	}
}
