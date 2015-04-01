package com.elfec.cobranza.view;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.model.events.DatePickListener;
import com.elfec.cobranza.presenter.MainMenuPresenter;
import com.elfec.cobranza.presenter.services.BluetoothDevicePickerPresenter.OnBluetoothDevicePicked;
import com.elfec.cobranza.presenter.views.IMainMenuView;
import com.elfec.cobranza.view.adapters.collection.CollectionAdapterFactory;
import com.elfec.cobranza.view.services.BluetoothDevicePickerService;
import com.elfec.cobranza.view.services.DateRangePickerService;
import com.elfec.cobranza.view.services.bluetooth.BluetoothStateMonitor;

public class MainMenu extends Activity implements IMainMenuView {

	private long lastClickTime = 0;
	private MainMenuPresenter presenter;
	
	/**
	 * Monitor en los cambios de estado del bluetooth
	 */
	private BluetoothStateMonitor bluetoothStateMonitor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		presenter = new MainMenuPresenter(this);	
		bluetoothStateMonitor = new BluetoothStateMonitor(this, presenter);
	}
	
	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int idItem = item.getItemId();
		switch(idItem)
		{
			case (R.id.menu_item_pick_printer):		
			{
				try
				{
					new BluetoothDevicePickerService(this, null, true).show();
				}
				catch(IllegalStateException e)
				{
					List<Exception> errors = new ArrayList<Exception>();
					errors.add(e);
					showBluetoothErrors(errors);
				}
				return true;
			}
			default:
			{
				return true;
			}
		}
	}
	
	@Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    if(bluetoothStateMonitor!=null)
	    	bluetoothStateMonitor.removeListener();
	    BluetoothAdapter.getDefaultAdapter().disable();
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}
	
	public void btnCollectionPaymentClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			Intent i = new Intent(MainMenu.this, CollectionAction.class);
			i.putExtra(CollectionAction.COLLECTION_TYPE, CollectionAdapterFactory.COLLECTION_PAYMENT);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnCollectionAnnulmentClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			Intent i = new Intent(MainMenu.this, CollectionAction.class);
			i.putExtra(CollectionAction.COLLECTION_TYPE, CollectionAdapterFactory.COLLECTION_ANNULMENT);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnCollectionDetailsClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			presenter.processCollectionDetailsReport();
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnAnnuledsReportClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			presenter.processAnnuledsReport();
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnDailySummaryClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			presenter.processDailySummaryReport();
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnCashDeskSummaryClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			presenter.processCashDeskSummaryReport();
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}

	//#region Interface Methods
	
	@Override
	public void showBluetoothErrors(final List<Exception> errors) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(errors.size()>0)
				{
					AlertDialogPro.Builder builder = new AlertDialogPro.Builder(MainMenu.this);
					builder.setTitle(R.string.title_bluetooth_errors)
					.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
					.setPositiveButton(R.string.btn_ok, null)
					.show();
				}
			}
		});
	}

	@Override
	public void showPrintErrors(final List<Exception> errors) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(errors.size()>0)
				{
					AlertDialogPro.Builder builder = new AlertDialogPro.Builder(MainMenu.this);
					builder.setTitle(R.string.title_print_errors)
					.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
					.setPositiveButton(R.string.btn_ok, null)
					.show();
				}
			}
		});
	}

	@Override
	public void showBluetoothPrintDialog(final OnBluetoothDevicePicked callback) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				try
				{
					new BluetoothDevicePickerService(MainMenu.this, callback, false).show();
				}
				catch(IllegalStateException e)
				{
					List<Exception> exceptions = new ArrayList<Exception>();
					exceptions.add(e);
					showPrintErrors(exceptions);
				}
			}
		});
	}

	@Override
	public void showDateRangePicker(final String title, final int iconId,
			final DatePickListener listener) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				new DateRangePickerService(MainMenu.this, title, iconId, listener).show();
			}
		});
	}
	
	//#endregion


}
