package com.elfec.cobranza.business_logic.printer;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.helpers.text_format.AttributePicker;
import com.elfec.cobranza.helpers.utils.AmountsCounter;
import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.model.printer.CPCLCommand.Justify;

/**
 * Clase que se encarga de generar el comando de impresión del reporte
 * de detalle de cobranzas
 * @author drodriguez
 *
 */
public class CollectionDetailReportGenerator extends DateRangeReportGenerator {
	/**
	 * El nombre del reporte
	 */
	public static final String REPORT_NAME = "REPORTE DETALLE DE COBRANZA";
	
	/**
	 * El código del reporte
	 */
	private final String REPORT_CODE = "GAF-714-03";
	
	private final String TOTAL_NUM = "Cantidad de facturas cobradas: %d";
	private final String TOTAL_AMOUNT = "Total cobrado: %s";
	
	/**
	 * Los pagos realizados
	 */
	private List<CollectionPayment> collectionPayments;
	
	public CollectionDetailReportGenerator(DateTime startDate, DateTime endDate)
	{
		super(startDate, endDate);
	}
	
	/**
	 * Asigna la parte de las facturas
	 */
	@Override
	protected void assignReceipts() {
		collectionPayments = CollectionPayment.getValidCollectionPayments(startDate, endDate, SessionManager.getLoggedCashdeskNumber());
		double nusCol=1.2, accCol = nusCol+1.6, dateCol = accCol+2.4,
				receiptCol = dateCol+1.6, periodCol = receiptCol+1.5, amountCol = periodCol+1.7;
		double startYFirstBox = (receiptHeight+=0.6) - 0.15;
		command.setBold(0.025).setSpacing(0.025).setFont("TAHOMA8P.CPF")
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
	
	@Override
	public String getReportName() {
		return REPORT_NAME;
	}

	@Override
	protected String getReportCode() {
		return REPORT_CODE;
	}

	@Override
	protected String getTotalNum() {
		return TOTAL_NUM;
	}

	@Override
	protected String getTotalAmountNum() {
		return TOTAL_AMOUNT;
	}

	@Override
	protected int getTotalReceiptsNum() {
		return collectionPayments.size();
	}

	@Override
	protected BigDecimal getTotalReceiptsAmount() {
		return AmountsCounter.countTotalAmount(collectionPayments, new AttributePicker<BigDecimal, CollectionPayment>(){
				@Override
				public BigDecimal pickAttribute(CollectionPayment collectionPayment) {
					return collectionPayment.getAmount();
			}});
	}
}
