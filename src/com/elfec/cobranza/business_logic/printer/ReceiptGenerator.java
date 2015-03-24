package com.elfec.cobranza.business_logic.printer;

import java.util.Locale;

import org.apache.commons.lang.WordUtils;
import org.joda.time.DateTime;

import com.elfec.cobranza.helpers.text_format.AccountFormatter;
import com.elfec.cobranza.model.Category;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.SupplyStatus;
import com.elfec.cobranza.model.printer.CPCLCommand;
import com.elfec.cobranza.model.printer.CPCLCommand.Justify;
import com.elfec.cobranza.model.printer.CPCLCommand.Unit;

/**
 * Clase que se encarga de generar el comando de impresi�n de una factura
 * @author drodriguez
 *
 */
public class ReceiptGenerator {
	/**
	 * Define el tama�o m�ximo de caracteres que puede ocupar una l�nea de texto<br>
	 * Se lo utiliza para formatear la direcci�n.
	 */
	private static final int WRAP_LIMIT = 30;
	
	/**
	 * Genera el comando cpcl de impresi�n del recibo
	 * @param receipt
	 * @return
	 */
	public static CPCLCommand generateCommand(CoopReceipt receipt)
	{
		CPCLCommand command = new CPCLCommand(200, 400, 7.9).inUnit(Unit.IN_CENTIMETERS );
		assignHeaderData(command, receipt);
		assignReceiptData(command, receipt);
		command.print();
		return command;
	}
	
	/**
	 * Asigna la informaci�n de la factura a la cabecera en el comando de la impresora
	 * @param command
	 */
	private static void assignHeaderData(CPCLCommand command, CoopReceipt receipt)
	{
		command.justify(Justify.CENTER)
		.text("TAHOMA15.CPF", 0, 0, 0, 0.049, 0.076, "FACTURA ORIGINAL")
		.setFont("TAHOMA11.CPF")
		.multilineText(0.44, 0, 0, 0.7, "NIT: 1023213028", "FACTURA No.: "+receipt.getReceiptNumber(),
				"AUTORIZACI�N: "+receipt.getAuthorizationNumber())
		.setFont("TAHOMA8P.CPF")
	    .text( 0, 0, 2.1, 0.025, 0.025, "Actividad Econ�mica:")
		.text( 0, 0, 2.45, "Venta de Energ�a El�ctrica");
	}
	
	/**
	 * Asigna la informaci�n de la factura al segundo sector de la factura en el comando de la impresora
	 * @param command
	 */
	private static void assignReceiptData(CPCLCommand command, CoopReceipt receipt)
	{
		assignReceiptLeftData(command, receipt);
		assignReceiptRightData(command, receipt);
	}
	

	/**
	 * Asigna la informaci�n de la columna izquierda de la factura al segundo sector de la factura en el comando de la impresora
	 * @param command
	 */
	private static void assignReceiptLeftData(CPCLCommand command, CoopReceipt receipt)
	{
		SupplyStatus powerSupplyStatus = receipt.getPowerSupplyStatus();
		command.justify(Justify.LEFT, 3)
		.setFont("TAHOMA8P.CPF")
		.text(0, 0.3, 3.3, 0.03, 0.03, "FECHA EMISI�N:")
		.text(0, 3.1, 3.3, receipt.getIssueDate().toString("dd/MM/yyyy"))
		.text("TAHOMA11.CPF", 0, 0.3, 3.65, 0.035, 0.035, "NUS:")
		.text("TAHOMA11.CPF", 0, 1.4, 3.65, 0.035, 0.035, ""+receipt.getSupplyId())
		.text(0, 0.3, 4.15, 0.03, 0.03, "CUENTA:")
		.text(0, 1.8, 4.15, AccountFormatter.formatAccountNumber(receipt.getSupplyNumber()))
		.text(0, 0.3, 4.5, 0.03, 0.03, "NOMBRE:")
		.text(0, 1.85, 4.5, Supply.findSupplyByNUSOrAccount(receipt.getSupplyId(), receipt.getSupplyNumber()).getClientName())
		.multilineText(0.37, 0, 0.35, 4.85, "NIT/CI: "+receipt.getNIT(), 
				wrapAddress("DIRECCI�N: "+receipt.getClientAddress()),
				"CATEGOR�A: "+Category.getFullCategoryDesc(receipt.getCategoryId()),
				"MEDIDOR: "+receipt.getMeterNumber(),
				"POTENCIA: "+(powerSupplyStatus==null?0:powerSupplyStatus.getBilledConsume()));
	}
	
	/**
	 * Asigna la informaci�n de la columna derecha de la factura al 
	 * segundo sector de la factura en el comando de la impresora
	 * @param command
	 */
	private static void assignReceiptRightData(CPCLCommand command, CoopReceipt receipt)
	{
		DateTime period = new DateTime(receipt.getYear(), receipt.getPeriodNumber(),1,0,0);
		command.justify(Justify.LEFT, 4.6)
		.setFont("TAHOMA8P.CPF")
		.multilineText(0.37, 0, 5.4, 3.3, 
				"CONSUMO (kWh): "+receipt.getSupplyStatus().getBilledConsume(),
				"PERIODO: "+period.toString("MMM/yyyy").toUpperCase(Locale.getDefault()),
				"DE: "+receipt.getSupplyStatus().getLastReadingDate().toString("dd/MM/yyyy")
				+"  A: "+receipt.getSupplyStatus().getDate().toString("dd/MM/yyyy"),
				"LECTURA ANTERIOR: "+receipt.getSupplyStatus().getLastReading(),
				"LECTURA ACTUAL: "+receipt.getSupplyStatus().getReading(),
				"FECHA PAGO: "+(DateTime.now().toString("dd/MM/yyyy hh:mm")),
				"VENCIMIENTO: "+receipt.getExpirationDate().toString("dd/MM/yyyy"),
				"DIAS MOROSIDAD: 23",
				"PR�XIMA EMISI�N: 06/01/2015");
	}
	
	/**
	 * Si es necesario parte la direcci�n en pedazos para que se impriman de forma
	 * subsecuente
	 * @param fullAddress
	 * @return
	 */
	private static String wrapAddress(String fullAddress)
	{
		return WordUtils.wrap(fullAddress, WRAP_LIMIT);
	}
}
