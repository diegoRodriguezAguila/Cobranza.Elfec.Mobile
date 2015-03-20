package com.elfec.cobranza.presenter.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.elfec.cobranza.presenter.views.IBluetoothDevicePickerDialog;
import com.zebra.sdk.comm.ConnectionException;
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
	/**
	 * Constante para las impresoras zebras RW 420, para otro tipo de impresora es necesario ver otras constantes
	 */
	public static final int ZEBRA_BLUETOOTH_PRINTER = 1664;
	
	private IBluetoothDevicePickerDialog view;
	private OnBluetoothDevicePicked bluetoothDevicePickedCallback;
	
	private BluetoothAdapter mBluetoothAdapter;
	
	private List<DiscoveredPrinterBluetooth> devicesList;

	public BluetoothDevicePickerPresenter(IBluetoothDevicePickerDialog view, OnBluetoothDevicePicked bluetoothDevicePickedCallback) {
		this.view = view;
		this.bluetoothDevicePickedCallback = bluetoothDevicePickedCallback;
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		devicesList = new ArrayList<DiscoveredPrinterBluetooth>();
	}
	
	/**
	 * Carga los dispositivos que están apareados con este dispositivo
	 */
	public void loadPairedDevices()
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				if (!mBluetoothAdapter.isEnabled())
				    throw new IllegalStateException("Antes de llamar al servicio se tiene que asegurar que el usuario tiene encendido el bluetooth");
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			    for (BluetoothDevice device : pairedDevices) 
			    {
			    	if( device.getBluetoothClass().getDeviceClass()==ZEBRA_BLUETOOTH_PRINTER)
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
				try {
					view.invokeBluetoothDiscoverer(BluetoothDevicePickerPresenter.this);
				} catch (ConnectionException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * Procesa el dispositivo seleccionado
	 * @param printer
	 */
	public void processSelectedDevice(DiscoveredPrinterBluetooth printer)
	{
		if(bluetoothDevicePickedCallback!=null)
			bluetoothDevicePickedCallback.bluetoothDevicePicked(printer);
	}

	@Override
	public void discoveryError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoveryFinished() {
		view.hideDiscoveringPrinters();
	}

	@Override
	public void foundPrinter(DiscoveredPrinter printer) {
		if(!devicesList.contains(printer))
			view.showDiscoveredBluetoothPrinter((DiscoveredPrinterBluetooth) printer);
	}
}
