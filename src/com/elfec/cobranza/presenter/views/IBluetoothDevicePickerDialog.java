package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;

/**
 * Abstracción del dialogo de selección de dispositivos
 * @author drodriguez
 *
 */
public interface IBluetoothDevicePickerDialog {

	/**
	 * Muestra al usuario las impresoras apareadas
	 * @param printers
	 */
	public void showPairedBluetoothPrinters(List<DiscoveredPrinterBluetooth> printers);
	/**
	 * Muestra al usuario una impresora que se descubrió en rango, si
	 * ya había impresoras la agrega a la lista que se muestra actualmente
	 * @param printer
	 */
	public void showDiscoveredBluetoothPrinter(DiscoveredPrinterBluetooth printer);
	/**
	 * Invoca al bluetooth discoverer del sdk de zebra
	 * @param discoveryHandler
	 */
	public void invokeBluetoothDiscoverer(DiscoveryHandler discoveryHandler)  throws ConnectionException;
	/**
	 * Esconde la información de que se están buscando impresoras
	 */
	public void hideDiscoveringPrinters();
}
