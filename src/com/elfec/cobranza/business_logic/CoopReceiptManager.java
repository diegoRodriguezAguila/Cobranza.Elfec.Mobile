package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.business_logic.printer.ReceiptGenerator;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.downloaders.DataImporter;
import com.elfec.cobranza.model.downloaders.DataImporter.ImportSpecs;
import com.elfec.cobranza.model.printer.ZebraPrinterExt;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.model.results.ManagerProcessResult;
import com.elfec.cobranza.remote_data_access.CoopReceiptRDA;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;
import com.zebra.sdk.settings.SettingsException;

/**
 * Se encarga de las operaciones de negocio de <b>CBTES_COOP</b> 
 * @author drodriguez
 *
 */
public class CoopReceiptManager {
	/**
	 * Importa los CBTES_COOP de oracle y los guarda
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataAccessResult<List<CoopReceipt>> importCoopReceipts(final String username, final String password, final String routes)
	{
		return DataImporter.importData(new ImportSpecs<CoopReceipt, List<CoopReceipt>>() {
			@Override
			public List<CoopReceipt> requestData() throws ConnectException,
					SQLException {
				return CoopReceiptRDA.requestCoopReceipts(username, password, routes);
			}

			@Override
			public List<CoopReceipt> resultHandle(List<CoopReceipt> importList) {
				return importList;
			}
			
		});
	}
	
	/**
	 * Manda a imprimir multiples facturas, se DEBE llamar de un hilo
	 * sino detendrá el hilo principal multiples veces
	 * @param internalControlCodes
	 * @param receipts
	 * @param printerDevice
	 * @return
	 */
	public static ManagerProcessResult printReceipts(List<Long> internalControlCodes, List<CoopReceipt> receipts, 
			DiscoveredPrinterBluetooth printerDevice)
	{
		ManagerProcessResult result = new ManagerProcessResult();
		int size = internalControlCodes.size();
		for (int i = 0; i < size; i++) {
			try {
				result.addErrors(printReceipt(internalControlCodes.get(i), 
						receipts.get(i), printerDevice).getErrors());
				if(!result.hasErrors())
					Thread.sleep(5000);
				else break;
			} catch (InterruptedException e) {
				e.printStackTrace();
				result.addError(e);
			}
		}
		return result;
	}
	
	/**
	 * Imprime el recibo con la impresora dada
	 * @param internalControlCode
	 * @param receipt
	 * @param printerDevice
	 */
	public static ManagerProcessResult printReceipt(long internalControlCode, CoopReceipt receipt, DiscoveredPrinterBluetooth printerDevice)
	{
		ManagerProcessResult result = new ManagerProcessResult();
		try {
			Connection conn = printerDevice.getConnection();
			conn.open();
			ZebraPrinterExt printer = new ZebraPrinterExt(ZebraPrinterFactory.getInstance(conn));
			if(printer.getPrinterControlLanguage()!=PrinterLanguage.CPCL)
				throw new ZebraPrinterLanguageUnknownException(null);
			ReceiptImagesManager.sendHeaderImageIfNecesary(printer);
			ReceiptImagesManager.sendFooterImageIfNecesary(printer);
			printer.sendCommand(ReceiptGenerator.generateCommand(receipt, internalControlCode));
			conn.close();
		} catch (ConnectionException e) {
			e.printStackTrace();
			result.addError(new ConnectionException("No se pudo establecer conexión con la impresora, "
					+ "asegurese de que esté encendida y que haya seleccionado la impresora correcta en la aplicación!"));
		} catch (ZebraPrinterLanguageUnknownException e) {
			e.printStackTrace();
			result.addError(new SettingsException("La impresora no está configurada adecuadamente para ser utilizada por la aplicación!"));
		} catch (ZebraIllegalArgumentException e) {
			e.printStackTrace();
		}
		return result;
	}
}
