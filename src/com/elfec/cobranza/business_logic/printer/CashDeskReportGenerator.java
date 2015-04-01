package com.elfec.cobranza.business_logic.printer;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.helpers.text_format.AttributePicker;
import com.elfec.cobranza.helpers.utils.AmountsCounter;
import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.model.printer.CashDeskResume;
import com.elfec.cobranza.model.printer.CPCLCommand.Justify;

/**
 * Clase que se encarga de generar el comando de impresión del reporte de resumen de caja
 * @author drodriguez
 *
 */
public class CashDeskReportGenerator extends DateRangeReportGenerator {

	/**
	 * El nombre del reporte
	 */
	private final String REPORT_NAME = "REPORTE RESUMEN DE CAJA";
	
	/**
	 * El código del reporte
	 */
	private final String REPORT_CODE = "Sin código.";
	
	private final String TOTAL_NUM = "Cantidad de facturas cobradas: %d";
	private final String TOTAL_AMOUNT = "Total cobrado: %s";
	
	private List<CashDeskResume> cashDeskResumes; 
	
	public CashDeskReportGenerator(DateTime startDate, DateTime endDate) {
		super(startDate, endDate);
	}

	@Override
	protected void assignReceipts() {
		cashDeskResumes = CollectionPayment.getEffectiveCollectionsRangedCashDeskResume(startDate, endDate, SessionManager.getLoggedCashdeskNumber());
		double dateCol=2.8, countCol = 6, amountCol = 9.4;
		double startYFirstBox = (receiptHeight+=0.6) - 0.15;
		command.setBold(0.025).setSpacing(0.025).setFont("TAHOMA8P.CPF")
		.justify(Justify.RIGHT, dateCol).text(1, receiptHeight, "Fecha")
		.justify(Justify.RIGHT, countCol).text(dateCol, receiptHeight, "Cantidad")
		.justify(Justify.RIGHT, amountCol).text(countCol, receiptHeight, "Importe")
		.setBold(0).setSpacing(0);
		double endYFirstBox = (receiptHeight+=0.1) + 0.3;
		for (CashDeskResume resume : cashDeskResumes)
		{
			command.justify(Justify.RIGHT, dateCol).text(1, receiptHeight+=SP_FACTOR, resume.getDate().toString("dd/MM/yyyy"))
			.justify(Justify.RIGHT, countCol).text(dateCol, receiptHeight, AmountsCounter.formatInteger(resume.getCollectionPaymentsNum()))
			.justify(Justify.RIGHT, amountCol).text(countCol, receiptHeight, AmountsCounter.formatBigDecimal(resume.getAmount()));
		}
		command.justify(Justify.LEFT)
		.box(0.85, startYFirstBox, amountCol+0.15, endYFirstBox, 0.02)
		.box(0.85, endYFirstBox-0.02, amountCol+0.15, receiptHeight+=(0.15+SP_FACTOR) , 0.02);
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
		return AmountsCounter.countQuantities(cashDeskResumes, new AttributePicker<Integer, CashDeskResume>(){
			@Override
			public Integer pickAttribute(CashDeskResume cashDeskResume) {
				return cashDeskResume.getCollectionPaymentsNum();
		}});
	}

	@Override
	protected BigDecimal getTotalReceiptsAmount() {
		return AmountsCounter.countTotalAmount(cashDeskResumes, new AttributePicker<BigDecimal, CashDeskResume>(){
				@Override
				public BigDecimal pickAttribute(CashDeskResume cashDeskResume) {
					return cashDeskResume.getAmount();
			}});

	}

}
