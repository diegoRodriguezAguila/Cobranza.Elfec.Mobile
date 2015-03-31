package com.elfec.cobranza.business_logic.printer;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.helpers.text_format.AttributePicker;
import com.elfec.cobranza.helpers.text_format.ObjectListToSQL;
import com.elfec.cobranza.helpers.utils.AmountsCounter;
import com.elfec.cobranza.model.AnnulmentReason;
import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.model.printer.CPCLCommand.Justify;

/**
 * Clase que se encarga de generar el comando de impresión del reporte
 * de cobros anulados
 * @author drodriguez
 *
 */
public class CollectionAnnulmentRerportGenerator extends DateRangeReportGenerator {
	/**
	 * El nombre del reporte
	 */
	private final String REPORT_NAME = "REPORTE DE ANULACION DE COBRANZA";
	
	/**
	 * El código del reporte
	 */
	private final String REPORT_CODE = "GAF-714-02";
	private final String TOTAL_NUM = "Cantidad anuladas: %d";
	private final String TOTAL_AMOUNT = "Total anulado: %s";
	
	/**
	 * Los cobros anulados
	 */
	private List<CollectionPayment> collectionAnnulments;
	
	
	public CollectionAnnulmentRerportGenerator(DateTime startDate,
			DateTime endDate) {
		super(startDate, endDate);
	}

	@Override
	protected void assignReceipts() {
		collectionAnnulments = CollectionPayment.getAnnuledCollectionPayments(startDate, endDate, SessionManager.getLoggedCashdeskNumber());
		double nusCol=1.2, dateCol = 3.3, annulDateCol = 5.45, 
				receiptCol = 6.9, periodCol = 7.9, amountCol = 9.25, reasonCol = 10.1;
		double startYFirstBox = (receiptHeight+=0.6) - 0.15;
		command.setBold(0.025).setSpacing(0.025)
		.justify(Justify.RIGHT, nusCol).text(0.25, receiptHeight, "NUS")
		.justify(Justify.CENTER, dateCol).text(nusCol, receiptHeight, "Fecha")
		.justify(Justify.CENTER, annulDateCol).text(dateCol, receiptHeight, "Fecha anul.")
		.justify(Justify.RIGHT, receiptCol).text(annulDateCol, receiptHeight, "Factura")
		.justify(Justify.RIGHT, periodCol).text(receiptCol, receiptHeight, "Per.")
		.justify(Justify.RIGHT, amountCol).text(periodCol, receiptHeight, "Importe")
		.justify(Justify.RIGHT, reasonCol).text(amountCol, receiptHeight, "Mot.")
		.setBold(0).setSpacing(0).setFont("TAHOMA6P.CPF");
		DateTime period;
		double endYFirstBox = (receiptHeight+=0.1) + 0.3;
		for (CollectionPayment payment : collectionAnnulments)
		{
			period = new DateTime(payment.getYear(), payment.getPeriodNumber(),1,0,0);
			command.justify(Justify.RIGHT, nusCol).text(0.25, receiptHeight+=SP_FACTOR, ""+payment.getSupplyId())
			.justify(Justify.RIGHT, dateCol).text(nusCol, receiptHeight, payment.getPaymentDate().toString("dd/MM/yyyy HH:mm:ss"))
			.justify(Justify.RIGHT, annulDateCol).text(dateCol, receiptHeight, payment.getAnnulmentDate().toString("dd/MM/yyyy HH:mm:ss"))
			.justify(Justify.RIGHT, receiptCol).text(annulDateCol, receiptHeight, ""+payment.getReceiptNumber()+"-"+payment.getId())
			.justify(Justify.RIGHT, periodCol).text(receiptCol, receiptHeight, period.toString("MM/yyyy"))
			.justify(Justify.RIGHT, amountCol).text(periodCol, receiptHeight, AmountsCounter.formatBigDecimal(payment.getAmount()))
			.justify(Justify.RIGHT, reasonCol-0.1).text(amountCol, receiptHeight, ""+payment.getAnnulmentReasonId());
		}
		command.justify(Justify.LEFT)
		.box(0.25, startYFirstBox, reasonCol+0.1, endYFirstBox, 0.02)
		.box(0.25, endYFirstBox-0.02, reasonCol+0.1, receiptHeight+=(0.15+SP_FACTOR) , 0.02);
	}
	
	@Override
	protected void assignFooter()
	{
		super.assignFooter();
		String inClause = ObjectListToSQL.convertToSQL(collectionAnnulments, "AnnulmentReasonRemoteId", 
				new AttributePicker<String, CollectionPayment>(){
					@Override
					public String pickAttribute(CollectionPayment annuledCollection) {
						return ""+annuledCollection.getAnnulmentReasonId();
					}});
		List<AnnulmentReason> usedAnnulments = AnnulmentReason.pickAnnulmentReasons(inClause);
		double startY = (receiptHeight+=0.65)-0.15;
		command.setFont("TAHOMA6P.CPF")
		.justify(Justify.CENTER)
		.text(0, 0, receiptHeight, 0.015, 0.015, "Motivos de Anulación")
		.justify(Justify.LEFT);

		for(AnnulmentReason reason : usedAnnulments)
		{
			String reasonDesc = WordUtils.wrap(reason.getDescription(), 75).replace("\n", "\r\n");
			int extraSpaces = reasonDesc.split("\r\n").length-1;
			command.text("TAHOMA8P.CPF", 0, 1.5, receiptHeight+=(SP_FACTOR+0.02), 0.015, 0.015, reason.getAnnulmentReasonRemoteId()+":")
			.multilineText(SP_FACTOR-0.1, 0, 2.2, receiptHeight+0.03, reasonDesc);
			receiptHeight+=(extraSpaces*(SP_FACTOR-0.1));
		}
		
		command.box(1.35, startY-=0.1, 9, receiptHeight+=SP_FACTOR, 0.02);
		receiptHeight+=0.3;
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
	protected int getTotalReceiptsNum() {
		return collectionAnnulments.size();
	}

	@Override
	protected String getTotalAmountNum() {
		return TOTAL_AMOUNT;
	}

	@Override
	protected BigDecimal getTotalReceiptsAmount() {
		return AmountsCounter.countTotalAmount(collectionAnnulments, new AttributePicker<BigDecimal, CollectionPayment>(){
			@Override
			public BigDecimal pickAttribute(CollectionPayment collectionPayment) {
				return collectionPayment.getAmount();
		}});
	}

}
