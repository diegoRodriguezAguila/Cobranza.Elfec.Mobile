package com.elfec.cobranza.business_logic;

import com.elfec.cobranza.model.printer.IReportGenerator;
import com.elfec.cobranza.model.printer.ZebraPrinterExt;
import com.elfec.cobranza.model.results.ManagerProcessResult;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;
import com.zebra.sdk.settings.SettingsException;

/**
 * Se encarga de las operaciones de l�gica de negocio de los reportes
 * @author drodriguez
 *
 */
public class ReportManager {
	/**
	 * 	Manda a imprimir el reporte resultado del generador del reporte, en la impresora definida
	 * @param generator
	 * @param printerDevice
	 * @return Resultado del proceso
	 */
	public static ManagerProcessResult printReport(IReportGenerator generator, DiscoveredPrinterBluetooth printerDevice)
	{
		ManagerProcessResult result = new ManagerProcessResult();
		try {
			Connection conn = printerDevice.getConnection();
			conn.open();
			ZebraPrinterExt printer = new ZebraPrinterExt(ZebraPrinterFactory.getInstance(conn));
			if(printer.getPrinterControlLanguage()!=PrinterLanguage.CPCL)
				throw new ZebraPrinterLanguageUnknownException(null);
			PrinterImagesManager.sendReportLogoImageIfNecesary(printer);
			printer.sendCommand(generator.generateCommand());
			conn.close();
		} catch (ConnectionException e) {
			e.printStackTrace();
			result.addError(new ConnectionException("No se pudo establecer conexi�n con la impresora, "
					+ "asegurese de que est� encendida y que haya seleccionado la impresora correcta en la aplicaci�n!"));
		} catch (ZebraPrinterLanguageUnknownException e) {
			e.printStackTrace();
			result.addError(new SettingsException("La impresora no est� configurada adecuadamente para ser utilizada por la aplicaci�n!"));
		} catch (ZebraIllegalArgumentException e) {
			e.printStackTrace();
		}
		return result;
	}
}
