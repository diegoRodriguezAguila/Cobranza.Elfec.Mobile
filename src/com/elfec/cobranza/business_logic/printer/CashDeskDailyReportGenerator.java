package com.elfec.cobranza.business_logic.printer;

import org.joda.time.DateTime;

import com.elfec.cobranza.business_logic.CollectionManager;
import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.helpers.utils.AmountsCounter;
import com.elfec.cobranza.model.PeriodBankAccount;
import com.elfec.cobranza.model.printer.CPCLCommand.Justify;
import com.elfec.cobranza.model.printer.CashDeskDailyResume;
import com.elfec.cobranza.model.printer.CashDeskResume;

/**
 * Clase que se encarga de generar el comando de impresión del reporte de resumen DIARIO de caja
 * @author drodriguez
 *
 */
public class CashDeskDailyReportGenerator extends BaseReportGenerator{

	/**
	 * El nombre del reporte
	 */
	private final String REPORT_NAME = "REPORTE RESUMEN DIARIO";
	
	/**
	 * El código del reporte
	 */
	private final String REPORT_CODE = "GAF-714-01";

	private final String REP_DATE = "FECHA: %s";
	
	protected final String CASH_DESK = "CAJA: %d %s";
	protected final String PERIOD = "PERIODO: %d";
	/**
	 * Correlativo inicial
	 */
	private final String CCI_INIT = "Correlativo Inicial: %d";
	/**
	 * Correlativo final
	 */
	private final String CCI_END = "Correlativo Final: %d";
	
	private CashDeskDailyResume cashDeskDailyResume;
	
	private DateTime date;
	
	public CashDeskDailyReportGenerator(DateTime date)
	{
		super();
		this.date = date;
	}
	
	/**
	 * Asigna la parte de la cabecera del reporte
	 */
	protected void assignHeader() {
		super.assignHeader();
		command.text(0, receiptHeight+=0.5, String.format(REP_DATE, date.toString("dd/MM/yyyy")))
		.text(0, receiptHeight+=0.4, String.format(CASH_DESK, SessionManager.getLoggedCashdeskNumber(),
				SessionManager.getLoggedCashdeskDesc()))
		.text(0, receiptHeight+=0.4, String.format(PERIOD, 
				PeriodBankAccount.findByCashdeskNumberAndDate(SessionManager.getLoggedCashdeskNumber()).getPeriodNumber()));
	}
	
	@Override
	protected void assignReceipts() {
		cashDeskDailyResume = CollectionManager.generateDailyResume(date);
		double conceptCol=1.4, countCol = 6, amountCol = 9.4;
		double startYFirstBox = (receiptHeight+=0.6) - 0.15;
		command.setBold(0.025).setSpacing(0.025).setFont("TAHOMA8P.CPF")
		.justify(Justify.LEFT, conceptCol).text(1, receiptHeight, "Concepto")
		.justify(Justify.RIGHT, countCol).text(conceptCol+1.6, receiptHeight, "Cantidad")
		.justify(Justify.RIGHT, amountCol).text(countCol, receiptHeight, "Importe")
		.setBold(0).setSpacing(0);
		double endYFirstBox = (receiptHeight+=0.1) + 0.3;
		for (CashDeskResume resume : cashDeskDailyResume.getCashDeskResumes())
		{
			command.justify(Justify.LEFT, conceptCol).text(1, receiptHeight+=SP_FACTOR, resume.getConcept())
			.justify(Justify.RIGHT, countCol).text(conceptCol+1.6, receiptHeight, AmountsCounter.formatInteger(resume.getCollectionPaymentsNum()))
			.justify(Justify.RIGHT, amountCol).text(countCol, receiptHeight, AmountsCounter.formatBigDecimal(resume.getAmount()));
		}
		command.justify(Justify.LEFT)
		.box(0.85, startYFirstBox, amountCol+0.15, endYFirstBox, 0.02)
		.box(0.85, endYFirstBox-0.02, amountCol+0.15, receiptHeight+=(0.15+SP_FACTOR) , 0.02)
		.text(0.4, receiptHeight+=0.4, "DIFERENCIA").line(2.1, receiptHeight+=0.25, 5.1,receiptHeight, 0.02)
		.text(0.4, receiptHeight+=0.4, "INGRESO EFECTIVO A CAJA").line(4, receiptHeight+=0.25, 7, receiptHeight, 0.02);
	}
	
	/**
	 * Asigna la parte del pie del reporte
	 */
	@Override
	protected void assignFooter() {
		command.setFont("TAHOMA8P.CPF")
		.setBold(0.025).setSpacing(0.025)
		.justify(Justify.LEFT)
		.text(0.4, receiptHeight+=0.4, String.format(CCI_INIT, cashDeskDailyResume.getInternalControlCodeStart()))
		.justify(Justify.RIGHT, 10.05)
		.text(0, receiptHeight, String.format(CCI_END, cashDeskDailyResume.getInternalControlCodeEnd()))
		.setBold(0).setSpacing(0);
		super.assignFooter();
	}

	@Override
	public String getReportName() {
		return REPORT_NAME;
	}

	@Override
	protected String getReportCode() {
		return REPORT_CODE;
	}
}
