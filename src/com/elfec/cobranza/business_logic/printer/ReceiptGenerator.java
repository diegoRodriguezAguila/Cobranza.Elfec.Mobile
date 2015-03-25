package com.elfec.cobranza.business_logic.printer;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.WordUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.elfec.cobranza.business_logic.ConceptManager;
import com.elfec.cobranza.helpers.text_format.AccountFormatter;
import com.elfec.cobranza.helpers.utils.AmountsCounter;
import com.elfec.cobranza.model.Category;
import com.elfec.cobranza.model.Concept;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.SupplyStatus;
import com.elfec.cobranza.model.printer.CPCLCommand;
import com.elfec.cobranza.model.printer.PrintConcept;
import com.elfec.cobranza.model.printer.CPCLCommand.Justify;
import com.elfec.cobranza.model.printer.CPCLCommand.Unit;

/**
 * Clase que se encarga de generar el comando de impresión de una factura
 * @author drodriguez
 *
 */
public class ReceiptGenerator {
	/**
	 * Define el espacio entre lineas de textos multilinea, en CM
	 */
	private static final double SP_FACTOR = 0.37;
	/**
	 * Define el tamaño máximo de caracteres que puede ocupar una línea de texto<br>
	 * Se lo utiliza para formatear la dirección.
	 */
	private static final int WRAP_LIMIT = 30;
	/**
	 * El limite de tamaño de un concepto
	 */
	private static final int CONCEPT_WRAP_LIMIT = 34;
	
	/**
	 * El espacio extra en la información de la factura
	 */
	private static double rcptDataExtraSpacing;
	
	/**
	 * El espacio de los conceptos
	 */
	private static double conceptsSpacing; 
	
	/**
	 * Genera el comando cpcl de impresión del recibo, sin las imagenes de header ni footer
	 * @param receipt
	 * @return
	 */
	public static CPCLCommand generateCommand(CoopReceipt receipt)
	{
		rcptDataExtraSpacing = 0;
		conceptsSpacing = 0;
		CPCLCommand command = new CPCLCommand(200, 400, 11.5).inUnit(Unit.IN_CENTIMETERS );
		assignHeaderData(command, receipt);
		assignReceiptData(command, receipt);
		assignReceiptDetails(command, receipt);
		command.print();
		return command;
	}

	/**
	 * Asigna la información de la factura a la cabecera en el comando de la impresora
	 * @param command
	 */
	private static void assignHeaderData(CPCLCommand command, CoopReceipt receipt)
	{
		command.justify(Justify.CENTER)
		.text("TAHOMA15.CPF", 0, 0, 0, 0.049, 0.076, "FACTURA ORIGINAL")
		.setFont("TAHOMA11.CPF")
		.multilineText(0.44, 0, 0, 0.7, "NIT: 1023213028", "FACTURA No.: "+receipt.getReceiptNumber(),
				"AUTORIZACIÓN: "+receipt.getAuthorizationNumber())
		.setFont("TAHOMA8P.CPF")
	    .text( 0, 0, 2.1, 0.025, 0.025, "Actividad Económica:")
		.text( 0, 0, 2.45, "Venta de Energía Eléctrica")
		.justify(Justify.LEFT)
		.box(0.4, 0.65, 10.05, 3.1, 0.02);
	}
	
	/**
	 * Asigna la información de la factura al segundo sector de la factura en el comando de la impresora
	 * @param command
	 */
	private static void assignReceiptData(CPCLCommand command, CoopReceipt receipt)
	{
		assignReceiptLeftData(command, receipt);
		assignReceiptRightData(command, receipt);
		command.justify(Justify.LEFT)
		.box(0.4, 3.08, 10.05, 7.1+rcptDataExtraSpacing, 0.02);
	}
	

	/**
	 * Asigna la información de la columna izquierda de la factura al segundo sector de la factura en el comando de la impresora
	 * @param command
	 */
	private static void assignReceiptLeftData(CPCLCommand command, CoopReceipt receipt)
	{
		String clientName = wrapName(Supply.findSupplyByNUSOrAccount(receipt.getSupplyId(), receipt.getSupplyNumber()).getClientName());
		String clientAddress = wrapAddress("DIRECCIÓN: "+receipt.getClientAddress());
		double extraSpacing = ((clientName.split("\r\n").length-1)*SP_FACTOR);
		command.justify(Justify.LEFT, 3)
		.setFont("TAHOMA8P.CPF")
		.text(0, 0.6, 3.3, 0.03, 0.03, "FECHA EMISIÓN:")
		.text(0, 3.3, 3.3, receipt.getIssueDate().toString("dd/MM/yyyy"))
		.text("TAHOMA11.CPF", 0, 0.6, 3.65, 0.035, 0.035, "NUS:")
		.text("TAHOMA11.CPF", 0, 1.6, 3.65, 0.035, 0.035, ""+receipt.getSupplyId())
		.text(0, 0.6, 4.15, 0.03, 0.03, "CUENTA:")
		.text(0, 2, 4.15, AccountFormatter.formatAccountNumber(receipt.getSupplyNumber()))
		.text(0, 0.6, 4.5, 0.03, 0.03, "NOMBRE:")
		.multilineText(SP_FACTOR, 0, 2.05, 4.5, clientName)
		.multilineText(SP_FACTOR, 0, 0.6, 4.85+extraSpacing, "NIT/CI: "+receipt.getNIT(), 
				clientAddress,
				"CATEGORÍA: "+Category.getFullCategoryDesc(receipt.getCategoryId()),
				"MEDIDOR: "+receipt.getMeterNumber());
		extraSpacing = (extraSpacing + ((clientAddress.split("\r\n").length-1)*SP_FACTOR))-(2*SP_FACTOR);
		rcptDataExtraSpacing = extraSpacing>0?extraSpacing:0;
	}
	
	/**
	 * Asigna la información de la columna derecha de la factura al 
	 * segundo sector de la factura en el comando de la impresora
	 * @param command
	 */
	private static void assignReceiptRightData(CPCLCommand command, CoopReceipt receipt)
	{
		SupplyStatus powerSupplyStatus = receipt.getPowerSupplyStatus();
		DateTime period = new DateTime(receipt.getYear(), receipt.getPeriodNumber(),1,0,0);
		int daysPastDue = Days.daysBetween(receipt.getExpirationDate(), DateTime.now()).getDays();
		command.justify(Justify.LEFT, 4.6)
		.setFont("TAHOMA8P.CPF")
		.multilineText(SP_FACTOR, 0, 5.6, 3.3, 
				"CONSUMO (kWh): "+receipt.getSupplyStatus().getBilledConsume(),
				"POTENCIA: "+(powerSupplyStatus==null?0:powerSupplyStatus.getBilledConsume()),
				"PERIODO: "+period.toString("MMM/yyyy").toUpperCase(Locale.getDefault()),
				"DE: "+receipt.getSupplyStatus().getLastReadingDate().toString("dd/MM/yyyy")
				+"  A: "+receipt.getSupplyStatus().getDate().toString("dd/MM/yyyy"),
				"LECTURA ANTERIOR: "+receipt.getSupplyStatus().getLastReading(),
				"LECTURA ACTUAL: "+receipt.getSupplyStatus().getReading(),
				"FECHA PAGO: "+(DateTime.now().toString("dd/MM/yyyy hh:mm")),
				"VENCIMIENTO: "+receipt.getExpirationDate().toString("dd/MM/yyyy"),
				"DIAS MOROSIDAD: "+(daysPastDue<0?0:daysPastDue),
				"PRÓXIMA EMISIÓN: "+receipt.getIssueDate().plusDays(33).toString("dd/MM/yyyy"));
	}
	
	/**
	 * Asigna los concetos y sus importes de la factura
	 * @param command
	 * @param receipt
	 */
	private static void assignReceiptDetails(CPCLCommand command, CoopReceipt receipt) 
	{
		command.justify(Justify.LEFT, 6.5)
		.setFont("TAHOMA8P.CPF")
		.text(0, 1, 7.3+rcptDataExtraSpacing, 0.03, 0.03, "DETALLE")
		.justify(Justify.RIGHT, 9)
		.text(0, 7, 7.3+rcptDataExtraSpacing, 0.03, 0.03, "IMPORTE");
		assignTotalConsumeConcepts(command, receipt);
		assignTotalSupplyConcepts(command, receipt);
		//assignTotalReceiptConcepts(command, receipt);
	}
	
	/**
	 * Muestra los conceptos de TOTAL CONSUMO
	 * @param command
	 * @param receipt
	 */
	private static void assignTotalConsumeConcepts(CPCLCommand command,
			CoopReceipt receipt) {
		double startY = 7.75+rcptDataExtraSpacing;
		List<PrintConcept> concepts =  ConceptManager.getAllTotalConsumeConcepts(receipt.getReceiptId());
		int size = concepts.size();
		String[] conceptDescs = new String[size];
		String[] conceptAmounts =  new String[size];
		int totalExtraRows = 0, extraRows;
		for (int i = 0; i < size; i++) {
			conceptDescs[i] = wrapConcept(concepts.get(i).getDescription());
			extraRows = conceptDescs[i].split("\r\n").length-1;
			totalExtraRows+=extraRows;
			conceptAmounts[i] = AmountsCounter.formatBigDecimal(concepts.get(i).getAmount());
			for (int j = 0; j < extraRows; j++) {
				conceptAmounts[i] = conceptAmounts[i]+"\r\n   ";
			}
		}
		conceptsSpacing = (size+totalExtraRows)*SP_FACTOR + startY;
		command.justify(Justify.LEFT, 6.5)
		.multilineText(SP_FACTOR, 0, 1, startY, conceptDescs)
		.line(1, conceptsSpacing, 6.5, conceptsSpacing, 0.02)
		.justify(Justify.RIGHT, 9)
		.multilineText(SP_FACTOR, 0, 7.2, startY, conceptAmounts)
		.justify(Justify.LEFT)
		.line(7.2, conceptsSpacing, 9, conceptsSpacing, 0.02);
	}
	
	/**
	 * Muestra los conceptos de TOTAL SUMINISTRO
	 * @param command
	 * @param receipt
	 */
	private static void assignTotalSupplyConcepts(CPCLCommand command,
			CoopReceipt receipt) {
		double startY = 0.1+conceptsSpacing;
		List<PrintConcept> concepts =  ConceptManager.getAllTotalSupplyConcepts(receipt.getReceiptId());
		int size = concepts.size();
		String[] conceptDescs = new String[size];
		String[] conceptAmounts =  new String[size];
		int totalExtraRows = 0, extraRows;
		for (int i = 0; i < size; i++) {
			conceptDescs[i] = wrapConcept(concepts.get(i).getDescription()+" PARA VER SI ENTRA UNA DESCRIPCIÓN MUCHO MAS LARGA DE LO NORMAL");
			extraRows = conceptDescs[i].split("\r\n").length-1;
			totalExtraRows+=extraRows;
			conceptAmounts[i] = AmountsCounter.formatBigDecimal(concepts.get(i).getAmount());
			for (int j = 0; j < extraRows; j++) {
				conceptAmounts[i] = conceptAmounts[i]+"\r\n   ";
			}
		}
		conceptsSpacing = (size+totalExtraRows)*SP_FACTOR + startY;
		command.justify(Justify.LEFT, 6.5)
		.multilineText(SP_FACTOR, 0, 1, startY, conceptDescs)
		.line(1, conceptsSpacing, 6.5, conceptsSpacing, 0.02)
		.justify(Justify.RIGHT, 9)
		.multilineText(SP_FACTOR, 0, 7.2, startY, conceptAmounts)
		.justify(Justify.LEFT)
		.line(7.2, conceptsSpacing, 9, conceptsSpacing, 0.02);
	}
	
	/**
	 * Muestra los conceptos de TOTAL FACTURA
	 * @param command
	 * @param receipt
	 */
	private static void assignTotalReceiptConcepts(CPCLCommand command,
			CoopReceipt receipt) {
		double startY = 0.1+conceptsSpacing;
		PrintConcept receiptTotalCpt = Concept.getTotalReceiptConcept(receipt.getReceiptId());
		PrintConcept subjectToTaxCreditCpt = Concept.getSubjectToTaxCreditConcept(receipt.getReceiptId());
		PrintConcept totalDueCpt = new PrintConcept("TOTAL A PAGAR", receipt.getTotalAmount());
		List<PrintConcept> fineBonusConcepts =  Concept.getFineBonusConcepts(receipt.getReceiptId());
		int size = fineBonusConcepts.size();
		String[] conceptDescs = new String[size];
		String[] conceptAmounts =  new String[size];
		for (int i = 0; i < size; i++) {
			conceptDescs[i] = wrapConcept(fineBonusConcepts.get(i).getDescription());
			conceptAmounts[i] = AmountsCounter.formatBigDecimal(fineBonusConcepts.get(i).getAmount());
		}
		conceptsSpacing = size*SP_FACTOR + startY;
		command.justify(Justify.LEFT, 6.5)
		.multilineText(SP_FACTOR, 0, 1, startY, conceptDescs)
		.line(1, conceptsSpacing, 6.5, conceptsSpacing, 0.02)
		.justify(Justify.RIGHT, 9)
		.multilineText(SP_FACTOR, 0, 7.2, startY, conceptAmounts)
		.justify(Justify.LEFT)
		.line(7.2, conceptsSpacing, 9, conceptsSpacing, 0.02);
	}

	/**
	 * Si es necesario parte la descripcion del concepto en pedazos para que se impriman de forma
	 * subsecuente
	 * @param name
	 * @return la cadena con lineas de salto respentando el WRAP_LIMIT
	 */
	private static String wrapConcept(String conceptDesc)
	{
		return WordUtils.wrap(conceptDesc, CONCEPT_WRAP_LIMIT).replace("\n", "\r\n");
	}
	
	/**
	 * Si es necesario parte el nombre en pedazos para que se impriman de forma
	 * subsecuente
	 * @param name
	 * @return la cadena con lineas de salto respentando el WRAP_LIMIT
	 */
	private static String wrapName(String name)
	{
		return WordUtils.wrap(name, WRAP_LIMIT-9).replace("\n", "\r\n");
	}
	
	/**
	 * Si es necesario parte la dirección en pedazos para que se impriman de forma
	 * subsecuente
	 * @param fullAddress
	 * @return la cadena con lineas de salto respentando el WRAP_LIMIT
	 */
	private static String wrapAddress(String fullAddress)
	{
		return WordUtils.wrap(fullAddress, WRAP_LIMIT).replace("\n", "\r\n");
	}
}
