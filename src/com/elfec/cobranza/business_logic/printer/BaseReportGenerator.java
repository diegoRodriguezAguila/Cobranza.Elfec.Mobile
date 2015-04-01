package com.elfec.cobranza.business_logic.printer;

import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.PrinterImagesManager;
import com.elfec.cobranza.model.printer.CPCLCommand;
import com.elfec.cobranza.model.printer.CPCLCommand.Justify;
import com.elfec.cobranza.model.printer.CPCLCommand.Unit;

public abstract class BaseReportGenerator implements IReportGenerator{
	/**
	 * Espacio entre lineas en cm
	 */
	protected final double SP_FACTOR = 0.37;

	/**
	 * El tamaño mínimo del reporte
	 */
	protected final double MIN_SIZE = 15;

	/**
	 * El tamaño del reporte en cm
	 */
	protected double receiptHeight;
	
	/**
	 * El comando que se está generando
	 */
	protected CPCLCommand command;
	
	public BaseReportGenerator()
	{
		receiptHeight = 0;
		command = new CPCLCommand(200, 400, 5).inUnit(Unit.IN_CENTIMETERS );
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
		assignReceipts();
		assignFooter();
		receiptHeight+=1.2;
		command.setLabelHeight(Math.max(receiptHeight, MIN_SIZE));
		command.print();
		return command;
	}

	/**
	 * Asigna la parte de la cabecera del reporte
	 */
	protected void assignHeader() {
		command.justify(Justify.LEFT)
		.image(0.35, receiptHeight, PrinterImagesManager.REP_LOGO_IN_PRINTER_NAME)
		.justify(Justify.RIGHT, 10).setFont("TAHOMA8P.CPF")
		.text(0, 7, receiptHeight, getReportCode())
		.justify(Justify.CENTER).text("TAHOMA11.CPF", 0, 0, receiptHeight+=0.8, 0.035, 0.035, getReportName());
	}
	
	/**
	 * Asigna la parte de abajo del reporte
	 */
	protected void assignFooter() {
		command.setFont("TAHOMA8P.CPF")
		.justify(Justify.CENTER, 4.8).text(0, receiptHeight+=0.8, "Preparado por:")
		.justify(Justify.CENTER, 10).text(5.2, receiptHeight, "Recibido por:")
		.justify(Justify.LEFT).line(1, receiptHeight+=2.5, 4,receiptHeight, 0.02)
		.line(6, receiptHeight, 9, receiptHeight, 0.02)
		.text("TAHOMA6P.CPF", 0, 0.4, receiptHeight+=0.8, "FECHA: "+DateTime.now().toString("dd/MM/yyyy HH:mm:ss"));
	}
	
	/**
	 * Asigna la parte de las facturas
	 */
	protected abstract void assignReceipts();
	
	/**
	 * El nombre del reporte
	 */
	public abstract String getReportName();
	/**
	 * El código del reporte
	 */
	protected abstract String getReportCode();

}
