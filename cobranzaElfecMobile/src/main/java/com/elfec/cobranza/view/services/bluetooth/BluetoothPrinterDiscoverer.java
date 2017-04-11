package com.elfec.cobranza.view.services.bluetooth;

import com.elfec.cobranza.model.printer.ZebraPrinterExt;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Se encarga de descubrir impresoras zebra bluetooth
 * @author drodriguez
 *
 */
public class BluetoothPrinterDiscoverer {

	public static void findPrinters(Context context, final DiscoveryHandler discoveryHandler) {
		
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		    	if(discoveryHandler!=null)
		    	{
			        String action = intent.getAction();
			        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			            if( device.getBluetoothClass().getDeviceClass()==ZebraPrinterExt.ZEBRA_BLUETOOTH_PRINTER)
			            	discoveryHandler.foundPrinter(new DiscoveredPrinterBluetooth(device.getAddress(), device.getName()));
			        }
			        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			            discoveryHandler.discoveryFinished();
			            context.unregisterReceiver(this);
			        }
		    	}
		    }
		};
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(mReceiver, filter);
		
		IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		context.registerReceiver(mReceiver, filter2);
		
		BluetoothAdapter.getDefaultAdapter().startDiscovery();
	}
	
	
}
