package com.elfec.cobranza.view;

import java.util.List;

import org.joda.time.DateTime;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.alertdialogpro.ProgressDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.presenter.DataExchangePresenter;
import com.elfec.cobranza.presenter.views.IDataExchangeView;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

public class DataExchange extends Activity implements IDataExchangeView {

	public static final String IMEI = "IMEI";
	private long lastClickTime = 0;
	private DataExchangePresenter presenter;
	private ProgressDialogPro waitingDialog;
	
	/**
	 * Indica si la actividad fue destruida o no
	 */
	private boolean isDestroyed;
	
	private static final int TIME_BETWEEN_CLICKS  = 600;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_exchange);
		OracleDatabaseConnector.initializeContext(this);
		presenter = new DataExchangePresenter(this);
		((TextView) findViewById(R.id.txt_device_imei)).setText(getIntent().getExtras().getString(IMEI));
		presenter.setFields();
		isDestroyed = false;
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
	protected void onDestroy()
	{
		super.onDestroy();
		isDestroyed = true;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		((TextView) findViewById(R.id.txt_date)).setText(TextFormater.capitalize(DateTime.now().toString("dd MMMM yyyy")));
	}
	
	public void btnDownloadDataClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS){
			Intent i = new Intent(DataExchange.this, ZoneListActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnUploadDataClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS){
			new AlertDialogPro.Builder(this).setTitle(R.string.title_exportation_confirm)
			.setIcon(R.drawable.export_to_server_d)
			.setMessage(R.string.msg_exportation_confirm)
			.setNegativeButton(R.string.btn_cancel, null)
			.setPositiveButton(R.string.btn_ok, new OnClickListener() {					
				@Override
				public void onClick(DialogInterface dialog, int which) {
					presenter.handleUpload();
				}
			}).show();
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnMainMenuClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS){
			Intent i = new Intent(DataExchange.this, MainMenu.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
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
					new AlertDialogPro.Builder(DataExchange.this).setTitle(titleId)
					.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
					.setPositiveButton(R.string.btn_ok, null).show();
				}
			});
		}
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

	@Override
	public void showWaiting() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {			
				waitingDialog = new ProgressDialogPro(DataExchange.this, R.style.Theme_FlavoredMaterialLight);
				waitingDialog.setMessage(getResources().getString(R.string.msg_export_validation));
				waitingDialog.setCancelable(false);
				waitingDialog.setIndeterminate(true);
				waitingDialog.setIcon(R.drawable.export_to_server_d);
				waitingDialog.setTitle(R.string.title_upload_waiting);
				waitingDialog.setProgressDrawable(getResources().getDrawable(R.drawable.progress_horizontal_cobranza));
				waitingDialog.setProgressStyle(ProgressDialogPro.STYLE_HORIZONTAL);
				waitingDialog.setCanceledOnTouchOutside(false);
				waitingDialog.show();
			}
		});
	}

	@Override
	public void updateWaiting(final int strId, final int totalData) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(waitingDialog!=null)
				{
					waitingDialog.setIndeterminate(false);
					waitingDialog.setMax(totalData);
					waitingDialog.setMessage(getResources().getString(strId));
				}
			}
		});
	}

	@Override
	public void hideWaiting() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(waitingDialog!=null)
					waitingDialog.dismiss();
			}
		});
	}

	@Override
	public void showExportationErrors(List<Exception> errors) {
		showErrors(R.string.title_exportation_errors,errors);
	}

	@Override
	public void updateProgress(final int dataCount, int totalData) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(waitingDialog!=null)
					waitingDialog.setProgress(dataCount);
			}
		});
	}

	@Override
	public void processSuccessfulDataExportation(final String username) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				Toast.makeText(DataExchange.this, Html.fromHtml("Se descargó la información al servidor exitosamente! Se cerró "
						+ "la sesión de <b>"+username+"</b>!"), Toast.LENGTH_LONG).show();
				 finish();//go back to the previous Activity
				 overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
			}
		});
	}

	@Override
	public void updateWaiting(final int strId) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(waitingDialog!=null)
				{
					waitingDialog.setIndeterminate(true);
					waitingDialog.setMessage(getResources().getString(strId));
				}
			}
		});
	}
	
	//#endregion
}
