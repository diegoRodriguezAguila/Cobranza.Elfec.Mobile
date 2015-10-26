package com.elfec.cobranza.presenter.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.elfec.cobranza.model.printer.ZebraPrinterExt;
import com.elfec.cobranza.presenter.views.IBluetoothDevicePickerDialog;
import com.elfec.cobranza.settings.PreferencesManager;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;


public class BluetoothDevicePickerPresenter implements DiscoveryHandler {
	
	/**
	 * Evento que se ejecuta el momento en que se selecciona un dispositivo
	 * @author drodriguez
	 *
	 */
	public interface OnBluetoothDevicePicked
	{
		/**
		 * Llamado cuando se selecciona un dispositivo
		 * @param device
		 */
		public void bluetoothDevicePicked(DiscoveredPrinterBluetooth device);
	}

	
	private IBluetoothDevicePickerDialog view;
	private OnBluetoothDevicePicked bluetoothDevicePickedCallback;
	
	private BluetoothAdapter mBluetoothAdapter;
	
	private List<DiscoveredPrinterBluetooth> devicesList;

	public BluetoothDevicePickerPresenter(IBluetoothDevicePickerDialog view, OnBluetoothDevicePicked bluetoothDevicePickedCallback) {
		this.view = view;
		this.bluetoothDevicePickedCallback = bluetoothDevicePickedCallback;
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		devicesList = new ArrayList<DiscoveredPrinterBluetooth>();
		if (!mBluetoothAdapter.isEnabled())
		    throw new IllegalStateException("Para poder seleccionar un dispositivo para imprimir debe asegurarse que tiene encendido el bluetooth!");
	}
	
	/**
	 * Carga los dispositivos que están apareados con este dispositivo
	 */
	public void loadPairedDevices()
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			    for (BluetoothDevice device : pairedDevices) 
			    {
			    	if( device.getBluetoothClass().getDeviceClass()==ZebraPrinterExt.ZEBRA_BLUETOOTH_PRINTER)
			    		devicesList.add(new DiscoveredPrinterBluetooth(device.getAddress(), device.getName()));
			    }
			    view.showPairedBluetoothPrinters(devicesList);
			}
		}).start();
	}
	/**
	 * Busca por bluetooth dispositivos disponibles
	 */
	public void searchBluetoothDevices()
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				view.invokeBluetoothDiscoverer(BluetoothDevicePickerPresenter.this);
			}
		}).start();
	}
	
	/**
	 * Procesa el dispositivo seleccionado
	 * @param printer
	 */
	public void processSelectedDevice(DiscoveredPrinterBluetooth printer)
	{
		PreferencesManager.instance().setDefaultPrinter(printer);
		if(bluetoothDevicePickedCallback!=null)
			bluetoothDevicePickedCallback.bluetoothDevicePicked(printer);
	}

	@Override
	public void discoveryError(String message) {		
	}

	@Override
	public void discoveryFinished() {
		view.hideDiscoveringPrinters();
	}

	@Override
	public void foundPrinter(DiscoveredPrinter printer) {
		if(!isInPairedDevices(printer))
			view.showDiscoveredBluetoothPrinter((DiscoveredPrinterBluetooth) printer);
	}
	
	/**
	 * Verifica si el dispostivo ya está en la lista de dispositivos vinculados
	 * @param printer
	 * @return true/false
	 */
	private boolean isInPairedDevices(DiscoveredPrinter printer)
	{
		for(DiscoveredPrinter prt : devicesList)
		{
			if(prt.address.equals(printer.address))
				return true;
		}
		return false;
	}
}
