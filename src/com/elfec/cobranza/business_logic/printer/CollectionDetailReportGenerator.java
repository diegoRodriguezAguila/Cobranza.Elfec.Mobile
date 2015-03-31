package com.elfec.cobranza.business_logic.printer;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.PrinterImagesManager;
import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.helpers.text_format.AttributePicker;
import com.elfec.cobranza.helpers.utils.AmountsCounter;
import com.elfec.cobranza.model.CollectionPayment;
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
	 * Espacio entre lineas en cm
	 */
	private final double SP_FACTOR = 0.37;
	/**
	 * El nombre del reporte
	 */
	private final String REPORT_NAME = "REPORTE DETALLE DE COBRANZA";
	
	/**
	 * El código del reporte
	 */
	private final String REPORT_CODE = "GAF-714-03";
	
	private final String FROM_DATES = "DE FECHA: %s A: %s";
	
	private final String CASH_DESK = "CAJA: %d %s";
	
	private final String TOTAL_NUM = "Cantidad de facturas cobradas: %d";
	private final String TOTAL_AMOUNT = "Total cobrado: %s";

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
	
	/**
	 * Los pagos realizados
	 */
	private List<CollectionPayment> collectionPayments;
	
	public CollectionDetailReportGenerator(DateTime startDate, DateTime endDate)
	{
		receiptHeight = 0;
		command = new CPCLCommand(200, 400, 5).inUnit(Unit.IN_CENTIMETERS );
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
		assignReceipts();
		assignFooter();
		command.setLabelHeight(receiptHeight+1.2);
		command.print();
		return command;
	}

	/**
	 * Asigna la parte de la cabecera del reporte
	 */
	private void assignHeader() {
		command.justify(Justify.LEFT)
		.image(0.35, receiptHeight, PrinterImagesManager.REP_LOGO_IN_PRINTER_NAME)
		.justify(Justify.RIGHT, 10).setFont("TAHOMA8P.CPF")
		.text(0, 7, receiptHeight, REPORT_CODE)
		.justify(Justify.CENTER).text("TAHOMA11.CPF", 0, 0, receiptHeight+=0.8, 0.035, 0.035, REPORT_NAME)
		.text(0, receiptHeight+=0.5, String.format(FROM_DATES,
				startDate.toString("dd/MM/yyyy"), endDate.toString("dd/MM/yyyy")))
		.text(0, receiptHeight+=0.4, String.format(CASH_DESK, SessionManager.getLoggedCashdeskNumber(),
				SessionManager.getLoggedCashdeskDesc()));
	}
	
	/**
	 * Asigna la parte de las facturas
	 */
	private void assignReceipts() {
		collectionPayments = CollectionPayment.getValidCollectionPayments(startDate, endDate);
		double nusCol=1.2, accCol = nusCol+1.6, dateCol = accCol+2.4,
				receiptCol = dateCol+1.6, periodCol = receiptCol+1.5, amountCol = periodCol+1.7;
		double startYFirstBox = (receiptHeight+=0.6) - 0.15;
		command.setBold(0.025).setSpacing(0.025)
		.justify(Justify.RIGHT, nusCol).text(0.4, receiptHeight, "NUS")
		.justify(Justify.RIGHT, accCol).text(nusCol, receiptHeight, "Cuenta")
		.justify(Justify.CENTER, dateCol).text(accCol+0.35, receiptHeight, "Fecha")
		.justify(Justify.RIGHT, receiptCol).text(dateCol, receiptHeight, "Factura")
		.justify(Justify.RIGHT, periodCol).text(receiptCol, receiptHeight, "Periodo")
		.justify(Justify.RIGHT, amountCol).text(periodCol, receiptHeight, "Importe")
		.setBold(0).setSpacing(0).setFont("TAHOMA6P.CPF");
		DateTime period;
		double endYFirstBox = (receiptHeight+=0.1) + 0.3;
		for (CollectionPayment payment : collectionPayments)
		{
			period = new DateTime(payment.getYear(), payment.getPeriodNumber(),1,0,0);
			command.justify(Justify.RIGHT, nusCol).text(0.4, receiptHeight+=SP_FACTOR, ""+payment.getSupplyId())
			.justify(Justify.RIGHT, accCol).text(nusCol, receiptHeight, payment.getSupplyNumber())
			.justify(Justify.RIGHT, dateCol).text(accCol, receiptHeight, payment.getPaymentDate().toString("dd/MM/yyyy  HH:mm:ss"))
			.justify(Justify.RIGHT, receiptCol).text(dateCol, receiptHeight, ""+payment.getReceiptNumber()+" - "+payment.getId())
			.justify(Justify.RIGHT, periodCol).text(receiptCol, receiptHeight, period.toString("MM/yyyy"))
			.justify(Justify.RIGHT, amountCol).text(periodCol, receiptHeight, AmountsCounter.formatBigDecimal(payment.getAmount()));
		}
		command.justify(Justify.LEFT)
		.box(0.35, startYFirstBox, amountCol+0.15, endYFirstBox, 0.02)
		.box(0.35, endYFirstBox-0.02, amountCol+0.15, receiptHeight+=(0.15+SP_FACTOR) , 0.02);
	}
	
	/**
	 * Asigna la parte de abajo del reporte
	 */
	private void assignFooter() {
		command.setFont("TAHOMA8P.CPF")
		.setBold(0.025).setSpacing(0.025)
		.justify(Justify.LEFT)
		.text(0.4, receiptHeight+=0.2, String.format(TOTAL_NUM, collectionPayments.size()))
		.justify(Justify.RIGHT, 10.05)
		.text(0, receiptHeight, String.format(TOTAL_AMOUNT, 
				AmountsCounter.formatBigDecimal(
				AmountsCounter.countTotalAmount(collectionPayments, new AttributePicker<BigDecimal, CollectionPayment>(){
					@Override
					public BigDecimal pickAttribute(CollectionPayment collectionPayment) {
						return collectionPayment.getAmount();
					}}))))
		.setBold(0).setSpacing(0);
	}

	@Override
	public String getReportName() {
		return REPORT_NAME;
	}
}
