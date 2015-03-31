package com.elfec.cobranza.view;

import org.joda.time.DateTime;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.presenter.DataExchangePresenter;
import com.elfec.cobranza.presenter.views.IDataFlowView;

public class DataExchange extends Activity implements IDataFlowView {

	public static final String IMEI = "IMEI";
	private long lastClickTime = 0;
	private DataExchangePresenter presenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_exchange);
		presenter = new DataExchangePresenter(this);
		((TextView) findViewById(R.id.txt_device_imei)).setText(getIntent().getExtras().getString(IMEI));
		presenter.setFields();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data_flow, menu);
		return true;
	}
	
	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	
	@Override
	public void onBackPressed() {
		presenter.closeCurrentSession();
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		((TextView) findViewById(R.id.txt_date)).setText(TextFormater.capitalize(DateTime.now().toString("dd MMMM yyyy")));
	}
	
	public void btnDownloadDataClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			Intent i = new Intent(DataExchange.this, ZoneListActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnMainMenuClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			Intent i = new Intent(DataExchange.this, MainMenu.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	//#region Interface Methods

	@Override
	public void setCurrentUser(String username) {
		((TextView) findViewById(R.id.txt_username_welcome)).setText(Html.fromHtml("Bienvenido <b>"+username+"</b>!"));
	}

	@Override
	public void setCashdeskNumber(int cashdeskNumber) {
		((TextView) findViewById(R.id.txt_cashdesk_number)).setText(""+cashdeskNumber);
	}

	@Override
	public void notifySessionClosed(final String username) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				Toast.makeText(DataExchange.this, Html.fromHtml("Se finalizó la sesión de <b>"+username+"</b>!"), Toast.LENGTH_LONG).show();
			}
		});
	}
	
	//#endregion
}
