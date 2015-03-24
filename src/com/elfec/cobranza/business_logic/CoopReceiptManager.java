package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import android.graphics.Bitmap;

import com.elfec.cobranza.business_logic.DataImporter.ImportSpecs;
import com.elfec.cobranza.business_logic.printer.ReceiptGenerator;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.ManagerProcessResult;
import com.elfec.cobranza.model.printer.ZebraPrinterExt;
import com.elfec.cobranza.remote_data_access.CoopReceiptRDA;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;

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
			Bitmap header = ReceiptImagesManager.getHeaderImage();
			//printer.printImage(new ZebraImageAndroid(header), 36, 0, 770, 318, false);		
			printer.sendCommand(ReceiptGenerator.generateCommand(receipt));
			if(header!=null)
				header.recycle();
			conn.close();
			//header.recycle();
		} catch (ConnectionException e) {
			e.printStackTrace();
			result.addError(e);
		} catch (ZebraPrinterLanguageUnknownException e) {
			e.printStackTrace();
			result.addError(e);
		}
		return result;
	}
}
