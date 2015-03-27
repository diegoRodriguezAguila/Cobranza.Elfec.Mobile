package com.elfec.cobranza.view.services.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.elfec.cobranza.model.events.BluetoothStateListener;

/**
 * Se encarga de monitorear los cambios en el hardware de bluetooth
 * @author drodriguez
 *
 */
public class BluetoothStateMonitor {
	private Context context;
	private BroadcastReceiver mReceiver;
	public BluetoothStateMonitor(Context context, final BluetoothStateListener listener)
	{
		this.context = context;
		mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		    	if(listener!=null)
		    	{
		    		final String action = intent.getAction();

		            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
		                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
		                                                     BluetoothAdapter.ERROR);
		                switch (state) {
		                case BluetoothAdapter.STATE_OFF:
		                	listener.onBluetoothTurnedOff();
		                    break;		                
		                case BluetoothAdapter.STATE_ON:
		                	listener.onBluetoothTurnedOn();
		                    break;
		                }
		            }
		    	}
		    	else context.unregisterReceiver(mReceiver);
		    }
		};
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		context.registerReceiver(mReceiver, filter);
	}
	
	/**
	 * Remueve el listenere, para dejar de recibir los eventos
	 */
	public void removeListener()
	{
		context.unregisterReceiver(mReceiver);
	}
}
