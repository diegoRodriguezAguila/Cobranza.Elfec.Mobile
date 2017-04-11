package com.elfec.cobranza.model.printer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.device.ProgressMonitor;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.ZebraImageI;
import com.zebra.sdk.printer.FieldDescriptionData;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.ZebraPrinter;

/**
 * Una extensión de una impresora zebra que añade funcionalidad utilizando el
 * principio de orientación a objetos de composición sobre herencia
 * 
 * @author drodriguez
 *
 */
@SuppressWarnings("deprecation")
public class ZebraPrinterExt implements ZebraPrinter {

	/**
	 * Constante para las impresoras zebras RW 420, para otro tipo de impresora
	 * es necesario ver otras constantes
	 */
	public static final int ZEBRA_BLUETOOTH_PRINTER = 1664;
	private ZebraPrinter printer;

	public ZebraPrinterExt(ZebraPrinter printer) {
		if (printer == null)
			throw new IllegalArgumentException("La impresora no puede ser null");
		this.printer = printer;
	}

	@Override
	public void calibrate() throws ConnectionException {
		printer.calibrate();
	}

	@Override
	public String[] retrieveFileNames() throws ConnectionException,
			ZebraIllegalArgumentException {
		return printer.retrieveFileNames();
	}

	@Override
	public String[] retrieveFileNames(String[] fileNames)
			throws ConnectionException, ZebraIllegalArgumentException {
		return printer.retrieveFileNames(fileNames);
	}

	@Override
	public void sendFileContents(String filePath) throws ConnectionException {
		printer.sendFileContents(filePath);
	}

	@Override
	public void sendFileContents(String filePath, ProgressMonitor handler)
			throws ConnectionException {
		printer.sendFileContents(filePath, handler);
	}

	@Override
	public void printImage(String image, int x, int y)
			throws ConnectionException, IOException {
		printer.printImage(image, x, y);
	}

	@Override
	public void printImage(String image, int x, int y, int width, int height,
			boolean insideFormat) throws ConnectionException, IOException {
		printer.printImage(image, x, y, width, height, insideFormat);
	}

	@Override
	public void printImage(ZebraImageI image, int x, int y, int width,
			int height, boolean insideFormat) throws ConnectionException {
		printer.printImage(image, x, y, width, height, insideFormat);
	}

	@Override
	public void storeImage(String deviceDriveAndFileName, ZebraImageI image,
			int width, int height) throws ConnectionException,
			ZebraIllegalArgumentException {
		printer.storeImage(deviceDriveAndFileName, image, width, height);
	}

	@Override
	public void storeImage(String deviceDriveAndFileName, String imageFullPath,
			int width, int height) throws ConnectionException,
			ZebraIllegalArgumentException, IOException {
		printer.storeImage(deviceDriveAndFileName, imageFullPath, width, height);
	}

	@Override
	public FieldDescriptionData[] getVariableFields(String formatString) {
		return printer.getVariableFields(formatString);
	}

	@Override
	public void printStoredFormat(String formatPathOnPrinter, String[] vars)
			throws ConnectionException {
		printer.printStoredFormat(formatPathOnPrinter, vars);
	}

	@Override
	public void printStoredFormat(String formatPathOnPrinter,
			Map<Integer, String> vars) throws ConnectionException {
		printer.printStoredFormat(formatPathOnPrinter, vars);
	}

	@Override
	public void printStoredFormat(String formatPathOnPrinter, String[] vars,
			String encoding) throws ConnectionException,
			UnsupportedEncodingException {
		printer.printStoredFormat(formatPathOnPrinter, vars, encoding);
	}

	@Override
	public void printStoredFormat(String formatPathOnPrinter,
			Map<Integer, String> vars, String encoding)
			throws ConnectionException, UnsupportedEncodingException {
		printer.printStoredFormat(formatPathOnPrinter, vars, encoding);
	}

	@Override
	public byte[] retrieveFormatFromPrinter(String formatPathOnPrinter)
			throws ConnectionException {
		return printer.retrieveFormatFromPrinter(formatPathOnPrinter);
	}

	@Override
	public void printConfigurationLabel() throws ConnectionException {
		printer.printConfigurationLabel();
	}

	@Override
	public void reset() throws ConnectionException {
		printer.reset();
	}

	@Override
	public void restoreDefaults() throws ConnectionException {
		printer.restoreDefaults();
	}

	@Override
	public void sendCommand(String command) throws ConnectionException {
		printer.getConnection().write(
				EncodingUtils.getBytes(command, "ISO-8859-1"));
	}

	public void sendCommand(CPCLCommand command) throws ConnectionException {
		printer.getConnection().write(
				EncodingUtils.getBytes(command.toString(), "ISO-8859-1"));
	}

	@Override
	public Connection getConnection() {
		return printer.getConnection();
	}

	@Override
	public PrinterStatus getCurrentStatus() throws ConnectionException {
		return printer.getCurrentStatus();
	}

	@Override
	public PrinterLanguage getPrinterControlLanguage() {
		return printer.getPrinterControlLanguage();
	}

	@Override
	public void setConnection(Connection connection) {
		printer.setConnection(connection);
	}

}
