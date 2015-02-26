package com.elfec.cobranza.view;

import org.joda.time.DateTime;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.presenter.DataFlowPresenter;
import com.elfec.cobranza.presenter.views.IDataFlowView;

public class DataFlow extends Activity implements IDataFlowView {

	private DataFlowPresenter presenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_flow);
		presenter = new DataFlowPresenter(this);
		((TextView) findViewById(R.id.txt_device_imei)).setText(getIntent().getExtras().getString("IMEI"));
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
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		((TextView) findViewById(R.id.txt_date)).setText(TextFormater.capitalize(DateTime.now().toString("dd MMM yyyy")));
	}
	
	public void btnDownloadDataClick(View view)
	{
		Intent i = new Intent(DataFlow.this, ZoneListActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
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
	
	//#endregion
}
