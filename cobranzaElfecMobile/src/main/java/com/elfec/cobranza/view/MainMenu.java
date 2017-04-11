package com.elfec.cobranza.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.model.events.DatePickListener;
import com.elfec.cobranza.presenter.MainMenuPresenter;
import com.elfec.cobranza.presenter.services.BluetoothDevicePickerPresenter.OnBluetoothDevicePicked;
import com.elfec.cobranza.presenter.views.IMainMenuView;
import com.elfec.cobranza.view.adapters.collection.CollectionAdapterFactory;
import com.elfec.cobranza.view.services.BluetoothDevicePickerService;
import com.elfec.cobranza.view.services.DatesPickerService;
import com.elfec.cobranza.view.services.bluetooth.BluetoothStateMonitor;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenu extends Activity implements IMainMenuView {

	private long lastClickTime = 0;
	private MainMenuPresenter presenter;
	
	/**
	 * Monitor en los cambios de estado del bluetooth
	 */
	private BluetoothStateMonitor bluetoothStateMonitor;
	
	/**
	 * Indica si la actividad fue destruida o no
	 */
	private boolean isDestroyed;
	
	private static final int TIME_BETWEEN_CLICKS  = 600;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		presenter = new MainMenuPresenter(this);	
		bluetoothStateMonitor = new BluetoothStateMonitor(this, presenter);
		isDestroyed = false;
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
	protected void onDestroy()
	{
		super.onDestroy();
		isDestroyed = true;
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
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}
	
	public void btnCollectionPaymentClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS){
			Intent i = new Intent(MainMenu.this, CollectionAction.class);
			i.putExtra(CollectionAction.COLLECTION_TYPE, CollectionAdapterFactory.COLLECTION_PAYMENT);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnCollectionAnnulmentClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS){
			Intent i = new Intent(MainMenu.this, CollectionAction.class);
			i.putExtra(CollectionAction.COLLECTION_TYPE, CollectionAdapterFactory.COLLECTION_ANNULMENT);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnCollectionDetailsClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS){
			presenter.processCollectionDetailsReport();
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnAnnuledsReportClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS){
			presenter.processAnnuledsReport();
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnDailySummaryClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS){
			presenter.processDailySummaryReport();
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnCashDeskSummaryClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS){
			presenter.processCashDeskSummaryReport();
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	/**
	 * Muestra errores con el titulo indicado
	 * @param titleId
	 * @param errors
	 */
	public void showErrors(final int titleId, final List<Exception> errors)
	{
		if(!isDestroyed && errors.size()>0)
		{
			runOnUiThread(new Runnable() {			
				@Override
				public void run() {
					new AlertDialog.Builder(MainMenu.this).setTitle(titleId)
					.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
					.setPositiveButton(R.string.btn_ok, null).show();
				}
			});
		}
	}

	//#region Interface Methods
	
	@Override
	public void showBluetoothErrors(List<Exception> errors) {
		showErrors(R.string.title_bluetooth_errors, errors);
	}

	@Override
	public void showPrintErrors(final List<Exception> errors) {
		showErrors(R.string.title_print_errors, errors);
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
	public void showDateRangePicker(String title, int iconId,
			DatePickListener listener) {
		new DatesPickerService(MainMenu.this, title, iconId, listener, true).show();
	}

	@Override
	public void showSingleDatePicker(String title, int iconId,
			DatePickListener listener) {
		new DatesPickerService(MainMenu.this, title, iconId, listener, false).show();
	}
	
	//#endregion


}
