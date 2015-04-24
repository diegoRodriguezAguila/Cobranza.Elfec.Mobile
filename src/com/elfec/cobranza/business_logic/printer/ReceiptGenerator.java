package com.elfec.cobranza.business_logic.printer;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.WordUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.elfec.cobranza.business_logic.ConceptManager;
import com.elfec.cobranza.business_logic.PrinterImagesManager;
import com.elfec.cobranza.business_logic.SessionManager;
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
import com.elfec.cobranza.model.printer.CPCLCommand.QRQuality;
import com.elfec.cobranza.model.printer.CPCLCommand.Unit;
import com.elfec.cobranza.settings.ParameterSettingsManager;
import com.elfec.cobranza.settings.ParameterSettingsManager.ParamKey;

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
	 * Define el tamaño máximo de caracteres que puede ocupar una línea de texto
	 */
	private static final int WRAP_LIMIT = 30;
	/**
	 * El limite de tamaño de un concepto
	 */
	private static final int CONCEPT_WRAP_LIMIT = 34;
	/**
	 * Define el tamaño máximo de caracteres que puede ocupar una línea de texto del literal de la factura
	 */
	private static final int LITERAL_WRAP_LIMIT = 66;
	/**
	 * Define el tamaño máximo de caracteres que puede ocupar una línea de texto del footer
	 */
	private static final int FOOTER_WRAP_LIMIT = 42;
	/**
	 * El NIT de Elfec
	 */
	private static final String ELFEC_NIT = "1023213028";
	
	/**
	 * El espacio extra en la información de la factura
	 */
	private static double rcptDataExtraSpacing;
	
	/**
	 * El tamaño de la factura en cm
	 */
	private static double receiptHeight;
	
	/**
	 * Booleano que indica si es que se debe usar el nuevo formato
	 */
	private static boolean isNewFormat;
	
	
	/**
	 * Genera el comando cpcl de impresión del recibo
	 * @param receipt
	 * @return
	 */
	public static CPCLCommand generateCommand(CoopReceipt receipt, long internalControlCode)
	{
		rcptDataExtraSpacing = 0;
		receiptHeight = 0;
		isNewFormat = (Days.daysBetween(
				ParameterSettingsManager.getParameter(ParamKey.SFV_DATE).getDateTimeValue(), 
					DateTime.now()).getDays()>=0);
		CPCLCommand command = new CPCLCommand(200, 400, 11.5).inUnit(Unit.IN_CENTIMETERS );
		assignHeaderData(command, receipt);
		assignReceiptData(command, receipt);
		assignReceiptDetails(command, receipt);
		assignFooterData(command, receipt, internalControlCode);
		command.setLabelHeight(receiptHeight+0.8);
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
		.image(0, receiptHeight, PrinterImagesManager.HEADER_IMAGE_IN_PRINTER_NAME)
		.text("TAHOMA15.CPF", 0, 0, receiptHeight+=4.1, 0.049, 0.076, "FACTURA ORIGINAL");
		if(!isNewFormat)
			command.text("TAHOMA8P.CPF", 0, 0, receiptHeight+=0.75, receipt.getAuthorizationDescription());
		double boxStartY = receiptHeight += (isNewFormat?0.75:SP_FACTOR);
		command.setFont("TAHOMA11.CPF")
		.multilineText(0.44, 0, 0, receiptHeight+=0.15, "NIT: "+ELFEC_NIT, "FACTURA No.: "+receipt.getReceiptNumber(),
				"AUTORIZACIÓN: "+receipt.getAuthorizationNumber())
		.setFont("TAHOMA8P.CPF")
	    .text( 0, 0, receiptHeight+=1.5, 0.025, 0.025, "Actividad Económica:")
		.text( 0, 0, receiptHeight+=0.35, "Venta de Energía Eléctrica")
		.justify(Justify.LEFT)
		.box(0.4, boxStartY, 10.05, receiptHeight+=0.55, 0.02);
	}
	
	/**
	 * Asigna la información de la factura al segundo sector de la factura en el comando de la impresora
	 * @param command
	 */
	private static void assignReceiptData(CPCLCommand command, CoopReceipt receipt)
	{
		double boxStartY = receiptHeight-0.02;
		double startY = receiptHeight+=0.15;
		assignReceiptRightData(command, receipt, startY);
		assignReceiptLeftData(command, receipt);
		receiptHeight = Math.max((startY+3.8), (receiptHeight+0.15))+rcptDataExtraSpacing;
		command.justify(Justify.LEFT)
		.box(0.4, boxStartY, 10.05, receiptHeight, 0.02); 
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
		.text(0, 0.6, receiptHeight, 0.03, 0.03, "FECHA EMISIÓN:")
		.text(0, 3.3, receiptHeight, receipt.getIssueDate().toString("dd/MM/yyyy"))
		.text("TAHOMA11.CPF", 0, 0.6, receiptHeight+=0.35, 0.035, 0.035, "NUS:")
		.text("TAHOMA11.CPF", 0, 1.6, receiptHeight, 0.035, 0.035, ""+receipt.getSupplyId())
		.text(0, 0.6, receiptHeight+=0.5, 0.03, 0.03, "CUENTA:")
		.text(0, 2, receiptHeight, AccountFormatter.formatAccountNumber(receipt.getSupplyNumber()))
		.text(0, 0.6, receiptHeight+=0.35, 0.03, 0.03, "NOMBRE:")
		.multilineText(SP_FACTOR, 0, 2.05, receiptHeight, clientName)
		.multilineText(SP_FACTOR, 0, 0.6, receiptHeight+=(0.35+extraSpacing), "NIT/CI: "+receipt.getNIT(), 
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
	private static void assignReceiptRightData(CPCLCommand command, CoopReceipt receipt, double startY)
	{
		SupplyStatus powerSupplyStatus = receipt.getPowerSupplyStatus();
		DateTime period = new DateTime(receipt.getYear(), receipt.getPeriodNumber(),1,0,0);
		int daysPastDue = Days.daysBetween(receipt.getExpirationDate(), DateTime.now()).getDays();
		
		command.justify(Justify.LEFT, 4.6)
		.setFont("TAHOMA8P.CPF")
		.multilineText(SP_FACTOR, 0, 5.6, startY, 
				"CONSUMO (kWh): "+receipt.getSupplyStatus().getBilledConsume(),
				"POTENCIA: "+(powerSupplyStatus==null?0:powerSupplyStatus.getBilledConsume()),
				"PERIODO: "+period.toString("MMM/yyyy").toUpperCase(Locale.getDefault()),
				"DE: "+receipt.getSupplyStatus().getLastReadingDate().toString("dd/MM/yyyy")
				+"  A: "+receipt.getSupplyStatus().getDate().toString("dd/MM/yyyy"),
				"LECTURA ANTERIOR: "+receipt.getSupplyStatus().getLastReading(),
				"LECTURA ACTUAL: "+receipt.getSupplyStatus().getReading(),
				"FECHA PAGO: "+(DateTime.now().toString("dd/MM/yyyy HH:mm")),
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
		double boxStartY = receiptHeight-0.02;
		receiptHeight+=0.15;
		command.justify(Justify.LEFT, 6.7)
		.setFont("TAHOMA8P.CPF")
		.text(0, 1, receiptHeight, 0.03, 0.03, "DETALLE")
		.justify(Justify.RIGHT, 9)
		.text(0, 7, receiptHeight, 0.03, 0.03, "IMPORTE");
		receiptHeight+=0.45;
		assignTotalConsumeConcepts(command, receipt);
		receiptHeight+=0.1;
		assignTotalSupplyConcepts(command, receipt);
		receiptHeight+=0.1;
		assignTotalReceiptConcepts(command, receipt);
		command.justify(Justify.LEFT).box(0.4, boxStartY, 10.05, receiptHeight+=0.15, 0.02);
	}
	
	/**
	 * Muestra los conceptos de TOTAL CONSUMO
	 * @param command
	 * @param receipt
	 */
	private static void assignTotalConsumeConcepts(CPCLCommand command,
			CoopReceipt receipt) {
		List<PrintConcept> concepts =  ConceptManager.getAllTotalConsumeConcepts(receipt.getReceiptId());
		int size = concepts.size();
		String[] conceptDescs = new String[size];
		String[] conceptAmounts =  new String[size];
		int totalExtraRows = getPrintableWrappedConceptArrays(concepts, conceptDescs, conceptAmounts);
		double startY = receiptHeight;
		receiptHeight += (size+totalExtraRows)*SP_FACTOR;
		command.justify(Justify.LEFT, 6.7)
		.multilineText(SP_FACTOR, 0, 1, startY, conceptDescs)
		.line(1, receiptHeight, 6.7, receiptHeight, 0.02)
		.justify(Justify.RIGHT, 9)
		.multilineText(SP_FACTOR, 0, 7.4, startY, conceptAmounts)
		.justify(Justify.LEFT)
		.line(7.4, receiptHeight, 9, receiptHeight, 0.02);
	}
	
	/**
	 * Muestra los conceptos de TOTAL SUMINISTRO
	 * @param command
	 * @param receipt
	 */
	private static void assignTotalSupplyConcepts(CPCLCommand command,
			CoopReceipt receipt) {
		List<PrintConcept> concepts =  ConceptManager.getAllTotalSupplyConcepts(receipt.getReceiptId());
		int size = concepts.size();
		String[] conceptDescs = new String[size];
		String[] conceptAmounts =  new String[size];
		int totalExtraRows = getPrintableWrappedConceptArrays(concepts, conceptDescs, conceptAmounts);
		double startY = receiptHeight;
		receiptHeight += (size+totalExtraRows)*SP_FACTOR;
		command.justify(Justify.LEFT, 6.7)
		.multilineText(SP_FACTOR, 0, 1, startY, conceptDescs)
		.line(1, receiptHeight, 6.7, receiptHeight, 0.02)
		.justify(Justify.RIGHT, 9)
		.multilineText(SP_FACTOR, 0, 7.4, startY, conceptAmounts)
		.justify(Justify.LEFT)
		.line(7.4, receiptHeight, 9, receiptHeight, 0.02);
	}
	
	/**
	 * Muestra los conceptos de TOTAL FACTURA
	 * @param command
	 * @param receipt
	 */
	private static void assignTotalReceiptConcepts(CPCLCommand command,
			CoopReceipt receipt) {
		List<PrintConcept> concepts = ConceptManager.getAllTotalReceiptConcepts(receipt);
		int size = concepts.size();
		String[] conceptDescs = new String[size];
		String[] conceptAmounts =  new String[size];
		int totalExtraRows = getPrintableWrappedConceptArrays(concepts, conceptDescs, conceptAmounts);
		double startY = receiptHeight;
		command.justify(Justify.LEFT, 6.7)
		.multilineText(SP_FACTOR, 0, 1, receiptHeight, conceptDescs[0])
		.justify(Justify.RIGHT, 9)
		.multilineText(SP_FACTOR, 0, 7.4, receiptHeight, conceptAmounts[0]);
		
		double spacing = (conceptDescs[0].split("\r\n").length)*SP_FACTOR;	
		command.justify(Justify.LEFT, 6.7)
		.setBold(0.03).setSpacing(0.03)
		.multilineText(SP_FACTOR, 0, 1, receiptHeight+=spacing, conceptDescs[1])
		.justify(Justify.RIGHT, 9)
		.multilineText(SP_FACTOR, 0, 7.4, receiptHeight, conceptAmounts[1])
		.setBold(0).setSpacing(0);
		
		spacing = (conceptDescs[1].split("\r\n").length)*SP_FACTOR;
		receiptHeight+=spacing;
		assignFineBonusConcepts(command, receipt);
		
		command.justify(Justify.LEFT, 6.7)
		.multilineText(SP_FACTOR, 0, 1, receiptHeight, conceptDescs[2])
		.justify(Justify.RIGHT, 9)
		.multilineText(SP_FACTOR, 0, 7.4, receiptHeight, conceptAmounts[2]);
		spacing = (conceptDescs[2].split("\r\n").length)*SP_FACTOR;
		receiptHeight = startY+((size+totalExtraRows)*SP_FACTOR)+spacing;	
	}
	
	/**
	 * Muestra la lista de conceptos de devoluciones y de bonificaciones multas
	 * @param command
	 * @param receipt
	 */
	private static void assignFineBonusConcepts(CPCLCommand command,
			CoopReceipt receipt) {
		List<PrintConcept> fineBonusConcepts =  Concept.getFineBonusConcepts(receipt.getReceiptId());
		int size = fineBonusConcepts.size();
		if(size>0)
		{
			String[] conceptDescs = new String[size];
			String[] conceptAmounts =  new String[size];
			int totalExtraRows = getPrintableWrappedConceptArrays(fineBonusConcepts, conceptDescs, conceptAmounts);
			command.justify(Justify.LEFT, 6.7)
			.multilineText(SP_FACTOR, 0, 1, receiptHeight, conceptDescs)
			.justify(Justify.RIGHT, 9)
			.multilineText(SP_FACTOR, 0, 7.4, receiptHeight, conceptAmounts);
			receiptHeight += (size+totalExtraRows)*SP_FACTOR;
		}
	}
	
	/**
	 * Asigna la información del final de la factura
	 * @param command
	 * @param receipt
	 */
	private static void assignFooterData(CPCLCommand command,
			CoopReceipt receipt, long internalControlCode) {
		String selectedMsg =  wrapFooterContent(
				ParameterSettingsManager.getParameter(isNewFormat?ParamKey.NEW_MSG:ParamKey.OLD_MSG).getStringValue());
		String enterpriseMsg = wrapFooterContent(
				ParameterSettingsManager.getParameter(ParamKey.ENTERPRISE_MSG).getStringValue());
		String literal = wrapLiteral(receipt.getLiteral()+" Bolivianos");
		String authDesc = wrapFooterContent(receipt.getAuthorizationDescription());
		command.justify(Justify.LEFT, 8)
		.setFont("TAHOMA8P.CPF")
		.text(0, 0.6, receiptHeight+=0.15, 0.03, 0.03, "Son:")
		.multilineText(SP_FACTOR, 0, 1.4, receiptHeight, literal);
		int spaces = (literal.split("\r\n").length);
		command.multilineText(SP_FACTOR, 0, 0.6, receiptHeight+=((SP_FACTOR*spaces)+0.1), 
				("CÓDIGO DE CONTROL: "+receipt.getControlCode()),
				("FECHA LÍMITE DE EMISIÓN: "+receipt.getAuthExpirationDate().toString("dd/MM/yyyy")))
				.text(0, 7.3, receiptHeight, 0.03, 0.03, "CAJA/"+SessionManager.getLoggedCashdeskNumber()+":"+internalControlCode)
				.justify(Justify.CENTER, 7.3)
				.multilineText(SP_FACTOR, 0, 0.6, receiptHeight+=((SP_FACTOR*2)+0.05), enterpriseMsg);
		spaces = (enterpriseMsg.split("\r\n").length);
		command.multilineText(SP_FACTOR, 0, 0.6, receiptHeight+=((SP_FACTOR*spaces)+0.3), selectedMsg);
		spaces = (selectedMsg.split("\r\n").length);
		if(isNewFormat)
			command.multilineText(SP_FACTOR, 0, 0.6, receiptHeight+=((SP_FACTOR*spaces)+0.2), authDesc);
		receiptHeight+=(isNewFormat?(authDesc.split("\r\n").length*SP_FACTOR):(SP_FACTOR*spaces));
		generateQR(command, receipt);
		command.justify(Justify.CENTER).image(0, receiptHeight+=0.3, PrinterImagesManager.FOOTER_IMAGE_IN_PRINTER_NAME);
		receiptHeight+=5.6;
	}

	/**
	 * Genera el código QR de la factura
	 * @param command
	 * @param receipt
	 */
	private static void generateQR(CPCLCommand command, CoopReceipt receipt) {
		String textToCode = ELFEC_NIT+"|"+receipt.getReceiptNumber()+"|"+
				receipt.getAuthorizationNumber()+"|"+receipt.getIssueDate().toString("dd/MM/yyyy")+"|"+
				AmountsCounter.formatBigDecimal(receipt.getTotalAmount())+"|"+
				AmountsCounter.formatBigDecimal(Concept.getSubjectToTaxCreditConcept(receipt.getReceiptId()).getAmount())+"|"+
				receipt.getControlCode()+"|"+receipt.getNIT()+"|0|0|"+
				AmountsCounter.formatBigDecimal(Concept.getNotSubjectToTaxCreditConcept(receipt.getReceiptId()).getAmount())+"|0";
		command.QR(5, 7.5, receiptHeight-2.5, QRQuality.M, textToCode);
	}

	/**
	 * Wrapea la cadena del literal tomando en cuenta los 7.3 de limite que se tiene
	 * subsecuente
	 * @param literal
	 * @return la cadena con lineas de salto respentando el LITERAL_WRAP_LIMIT
	 */
	private static String wrapLiteral(String literal)
	{
		return WordUtils.wrap(literal, LITERAL_WRAP_LIMIT).replace("\n", "\r\n");
	}
	
	/**
	 * Wrapea la cadena del footer tomando en cuenta los 7.3 de limite que se tiene
	 * subsecuente
	 * @param footerMsg
	 * @return la cadena con lineas de salto respentando el WRAP_LIMIT
	 */
	private static String wrapFooterContent(String footerMsg)
	{
		return WordUtils.wrap(footerMsg, FOOTER_WRAP_LIMIT).replace("\n", "\r\n");
	}
	
	/**
	 * Si es necesario parte la descripcion del concepto en pedazos para que se impriman de forma
	 * subsecuente
	 * @param conceptDesc
	 * @return la cadena con lineas de salto respentando el CONCEPT_WRAP_LIMIT
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
	
	/**
	 * Arma los arrays imprimibles tomando en cuenta el wrap del texto del tamaño limite
	 * @param concepts la lista de conceptos de la que se quieren armar sus arrays imprimibles
	 * @param conceptDescs el array de descripciones con las filas formateadas
	 * @param conceptAmounts el array de importes con las filas formateadas
	 * @return la cantidad de filas extra que se aumentaron
	 */
	private static int getPrintableWrappedConceptArrays(List<PrintConcept> concepts, String[] conceptDescs, String[] conceptAmounts)
	{
		int size = concepts.size();
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
		return totalExtraRows;
	}
}
