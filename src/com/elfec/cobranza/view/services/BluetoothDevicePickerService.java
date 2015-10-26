package com.elfec.cobranza.view.services;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.presenter.services.BluetoothDevicePickerPresenter;
import com.elfec.cobranza.presenter.services.BluetoothDevicePickerPresenter.OnBluetoothDevicePicked;
import com.elfec.cobranza.presenter.views.IBluetoothDevicePickerDialog;
import com.elfec.cobranza.view.adapters.BluetoothPrinterAdapter;
import com.elfec.cobranza.view.services.bluetooth.BluetoothPrinterDiscoverer;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;

/**
 * Provee el servicio de dialogo para la selección de un dispositivo de bluetooth
 * @author drodriguez
 *
 */
public class BluetoothDevicePickerService implements IBluetoothDevicePickerDialog {
	public static final int REQUEST_ENABLE_BT = 1;
	private BluetoothDevicePickerPresenter presenter;
	
	private AlertDialogPro.Builder dialogBuilder;
	private AlertDialogPro dialog; 
	private Handler mHandler;
	private Context context;
	
	//components
	private TextView lblPairedPrinters;
	private ListView listPairedDevices;
	private LinearLayout layoutDiscoveringDevices;
	private TextView lblDiscoveredPrinters;
	private ListView listDiscoveredDevices;

	
	/**
	 * Inicializa la clase
	 * @param context tiene que ser una activity dado que se invoca al intent de encendido de bluetooth
	 * @param bluetoothDevicePickedCallback
	 */
	@SuppressLint("InflateParams")
	public BluetoothDevicePickerService(Context context, OnBluetoothDevicePicked bluetoothDevicePickedCallback, boolean cancelable)
	{
		presenter = new BluetoothDevicePickerPresenter(this, bluetoothDevicePickedCallback);
		mHandler = new Handler(Looper.getMainLooper());
		this.context = context;
		View rootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.dialog_bluetooth_device_picker, null);	
		listPairedDevices = (ListView) rootView.findViewById(R.id.list_paired_devices);
		layoutDiscoveringDevices = (LinearLayout) rootView.findViewById(R.id.layout_discovering_devices);
		listDiscoveredDevices = (ListView) rootView.findViewById(R.id.list_discovered_devices);
		lblDiscoveredPrinters = (TextView) rootView.findViewById(R.id.lbl_discovered_printers);
		lblPairedPrinters = (TextView) rootView.findViewById(R.id.lbl_paired_printers);
		setOnItemClickListeners();
		
		presenter.loadPairedDevices();
		presenter.searchBluetoothDevices();
		
		dialogBuilder = new AlertDialogPro.Builder(context);
		dialogBuilder.setTitle(R.string.title_bluetooth_device_picker).setIcon(R.drawable.printer_picker)
			.setView(rootView)
			.setNegativeButton(R.string.btn_cancel, null)
			.setCancelable(cancelable);
	}
	
	/**
	 * Pone los item click listeners a las dos listas
	 */
	private void setOnItemClickListeners() {
		OnItemClickListener itemCL = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				dialog.dismiss();
				DiscoveredPrinterBluetooth selectedPrinter = (DiscoveredPrinterBluetooth)adapter.getItemAtPosition(position);
				Toast.makeText(context, Html.fromHtml("Impresora <b>"+selectedPrinter.friendlyName+"</b> seleccionada!"), Toast.LENGTH_SHORT).show();
				presenter.processSelectedDevice(selectedPrinter);
			}
		};
		listPairedDevices.setOnItemClickListener(itemCL);
		listDiscoveredDevices.setOnItemClickListener(itemCL);
	}

	/**
	 * Muestra el diálogo construido
	 */
	public void show()
	{
		dialog = dialogBuilder.create();
		dialog.show();
	}	
	
	//#region Interface Methods

	@Override
	public void showPairedBluetoothPrinters(
			final List<DiscoveredPrinterBluetooth> printers) {
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				if(printers.size()>0)
					listPairedDevices.setAdapter(new BluetoothPrinterAdapter(context, R.layout.bluetooth_device_list_item, printers));
				else lblPairedPrinters.setText(R.string.lbl_no_paired_printers);
			}
		});
	}

	@Override
	public void invokeBluetoothDiscoverer(DiscoveryHandler discoveryHandler){
		BluetoothPrinterDiscoverer.findPrinters(context, discoveryHandler);
	}

	@Override
	public void showDiscoveredBluetoothPrinter(
			final DiscoveredPrinterBluetooth printer) {
		mHandler.post(new Runnable() {		
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				if(listDiscoveredDevices.getAdapter()==null)
					listDiscoveredDevices.setAdapter(new BluetoothPrinterAdapter(context, R.layout.bluetooth_device_list_item, new ArrayList<DiscoveredPrinterBluetooth>()));
				((ArrayAdapter<DiscoveredPrinterBluetooth>)listDiscoveredDevices.getAdapter()).add(printer);
			}
		});
	}

	@Override
	public void hideDiscoveringPrinters() {
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				layoutDiscoveringDevices.setVisibility(View.GONE);
				if(listDiscoveredDevices.getAdapter()==null) //no se encontró ningún dispositivo
					lblDiscoveredPrinters.setText(R.string.lbl_no_devices_found);
			}
		});
	}
	
	//#endregion

}
