package com.elfec.cobranza.presenter;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;

import com.elfec.cobranza.business_logic.CollectionManager;
import com.elfec.cobranza.business_logic.CoopReceiptManager;
import com.elfec.cobranza.business_logic.SupplyManager;
import com.elfec.cobranza.helpers.PreferencesManager;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.ManagerProcessResult;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.events.BluetoothStateListener;
import com.elfec.cobranza.presenter.services.BluetoothDevicePickerPresenter.OnBluetoothDevicePicked;
import com.elfec.cobranza.presenter.views.ICollectionActionView;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;

public class CollectionPaymentPresenter extends CollectionActionPresenter implements BluetoothStateListener {

	/**
	 * Interfaz que sirve para llamarse cuando un pago se confirma por el usuario
	 * @author drodriguez
	 *
	 */
	public interface OnPaymentConfirmedCallback
	{
		public void paymentConfirmed();
	}
	
	private List<Runnable> bluetoothActionsQueue;
	
	public CollectionPaymentPresenter(ICollectionActionView view) {
		super(view);
		bluetoothActionsQueue = new ArrayList<Runnable>();
		BluetoothAdapter.getDefaultAdapter().enable();
	}
	
	@Override
	public void loadSupplyReceipts(Supply supply) {
		currentSupply = supply;
		view.showReceipts(SupplyManager.getPendingReceipts(supply));
	}

	@Override
	public void processAction(final List<CoopReceipt> selectedReceipts) {
		view.showPaymentConfirmation(selectedReceipts, new OnPaymentConfirmedCallback() {			
			@Override
			public void paymentConfirmed() {
				new Thread(new Runnable() {			
					@Override
					public void run() {
						DataAccessResult<List<Long>> result = CollectionManager.payCollections(selectedReceipts);
						loadSupplyReceipts(currentSupply);
						if(!result.hasErrors())
						{
							view.informActionSuccessfullyFinished();
							printReceipts(result.getResult(), selectedReceipts);
						}
						view.showActionErrors(result.getErrors());
					}
				}).start();
			}
		});	
	}
	
	/**
	 * Invoca a los métodos necesarios para realizar la impresión de las facturas
	 * @param controlCodes
	 * @param RegisteredReceipts
	 */
	private void printReceipts(final List<Long> controlCodes, final List<CoopReceipt> registeredReceipts) {
		final OnBluetoothDevicePicked callback = new OnBluetoothDevicePicked() {			
			@Override
			public void bluetoothDevicePicked(DiscoveredPrinterBluetooth device) {
				PreferencesManager.instance().setDefaultPrinter(device);
				ManagerProcessResult result = CoopReceiptManager.printReceipts(controlCodes, registeredReceipts, device);
				view.showPrintErrors(result.getErrors());
			}
		};
		Runnable runnable = new Runnable() {			
			@Override
			public void run() {
				DiscoveredPrinterBluetooth defaultPrinter = PreferencesManager.instance().getDefaultPrinter();
				if(defaultPrinter==null || defaultPrinter.getConnection()==null)
					view.showBluetoothPrintDialog(callback);
				else callback.bluetoothDevicePicked(defaultPrinter);
			}
		};
		if(BluetoothAdapter.getDefaultAdapter().isEnabled())
			runnable.run();
		else 
		{
			bluetoothActionsQueue.add(runnable);
			BluetoothAdapter.getDefaultAdapter().enable();
		}
	}
		
	
	//#region BluetoothStateListener

	@Override
	public void onBluetoothTurnedOn() {
		for(Runnable runnable : bluetoothActionsQueue)
		{
			runnable.run();
		}
		bluetoothActionsQueue.clear();
	}

	@Override
	public void onBluetoothTurnedOff() {
		BluetoothAdapter.getDefaultAdapter().enable();
	}
	
	//#endregion

}
