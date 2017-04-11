package com.elfec.cobranza.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.presenter.DataExchangePresenter;
import com.elfec.cobranza.presenter.services.WipeAllDataServicePresenter.WipeConfirmationListener;
import com.elfec.cobranza.presenter.views.IDataExchangeView;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;
import com.elfec.cobranza.view.services.ProgressDialogService;
import com.elfec.cobranza.view.services.WipeAllDataDialogService;

import org.joda.time.DateTime;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DataExchange extends AppCompatActivity implements IDataExchangeView {

	public static final String IMEI = "IMEI";
	private long lastClickTime = 0;
	private DataExchangePresenter presenter;
	private ProgressDialogService waitingDialog;

	/**
	 * Indica si la actividad fue destruida o no
	 */
	private boolean isDestroyed;

	private static final int TIME_BETWEEN_CLICKS = 600;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_exchange);
		OracleDatabaseConnector.initializeContext(this);
		presenter = new DataExchangePresenter(this);
		((TextView) findViewById(R.id.txt_device_imei)).setText(getIntent()
				.getExtras().getString(IMEI));
		presenter.setFields();
		isDestroyed = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data_exchange, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int idItem = item.getItemId();
		switch (idItem) {
		case (R.id.menu_wipe_all_data): {
			new WipeAllDataDialogService(this, new WipeConfirmationListener() {
				@Override
				public void onWipeConfirmed() {
					presenter.processDataWipe();
				}
			}).show();
			return true;
		}
		default: {
			return true;
		}
		}
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	public void onBackPressed() {
		presenter.closeCurrentSession();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isDestroyed = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		((TextView) findViewById(R.id.txt_date)).setText(TextFormater
				.capitalize(DateTime.now().toString("dd MMMM yyyy")));
	}

	public void btnDownloadDataClick(View view) {
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS) {
			Intent i = new Intent(DataExchange.this, ZoneListActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
		}
		lastClickTime = SystemClock.elapsedRealtime();
	}

	public void btnUploadDataClick(View view) {
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.title_exportation_confirm)
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

	public void btnMainMenuClick(View view) {
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS) {
			presenter.validateMainMenuOption();
		}
		lastClickTime = SystemClock.elapsedRealtime();
	}

	/**
	 * Muestra errores con el titulo indicado
	 * 
	 * @param titleId
	 * @param errors
	 */
	public void showErrors(final int titleId, final List<Exception> errors) {
		if (!isDestroyed && errors.size() > 0) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					new AlertDialog.Builder(DataExchange.this)
							.setTitle(titleId)
							.setMessage(
									MessageListFormatter
											.fotmatHTMLFromErrors(errors))
							.setPositiveButton(R.string.btn_ok, null).show();
				}
			});
		}
	}

	/**
	 * Notifica al usuario un mensaje
	 * 
	 * @param msgId
	 */
	public void notifyUser(final int msgId) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(DataExchange.this, msgId, Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	// #region Interface Methods

	@Override
	public void setCurrentUser(String username) {
		((TextView) findViewById(R.id.txt_username_welcome)).setText(Html
				.fromHtml("Bienvenido <b>" + username + "</b>!"));
	}

	@Override
	public void setCashdeskNumber(int cashdeskNumber) {
		((TextView) findViewById(R.id.txt_cashdesk_number)).setText(""
				+ cashdeskNumber);
	}

	@Override
	public void notifySessionClosed(final String username) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(
						DataExchange.this,
						Html.fromHtml("Se finalizó la sesión de <b>" + username
								+ "</b>!"), Toast.LENGTH_LONG).show();
				finish();// go back to the previous Activity
				overridePendingTransition(R.anim.slide_right_in,
						R.anim.slide_right_out);
			}
		});
	}

	@Override
	public void showWaiting() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				waitingDialog = new ProgressDialogService(DataExchange.this);
				waitingDialog.setMessage(getResources().getString(
						R.string.msg_export_validation));
				waitingDialog.setCancelable(false);
				waitingDialog.setIndeterminate(true);
				waitingDialog.setIcon(R.drawable.export_to_server_d);
				waitingDialog.setTitle(R.string.title_upload_waiting);
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
				if (waitingDialog != null) {
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
				if (waitingDialog != null)
					waitingDialog.dismiss();
			}
		});
	}

	@Override
	public void showExportationErrors(List<Exception> errors) {
		showErrors(R.string.title_exportation_errors, errors);
	}

	@Override
	public void updateProgress(final int dataCount, int totalData) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (waitingDialog != null)
					waitingDialog.setProgress(dataCount);
			}
		});
	}

	@Override
	public void notifySuccessfulDataExportation() {
		notifyUser(R.string.msg_exportation_finished_successfully);
	}

	@Override
	public void updateWaiting(final int strId) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (waitingDialog != null) {
					waitingDialog.setIndeterminate(true);
					waitingDialog.setMessage(getResources().getString(strId));
				}
			}
		});
	}

	@Override
	public String getIMEI() {
		return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
	}

	@Override
	public void showWipingDataWait() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				waitingDialog = new ProgressDialogService(DataExchange.this);
				waitingDialog.setMessage(getResources().getString(
						R.string.msg_unlocking_remote_routes));
				waitingDialog.setCancelable(false);
				waitingDialog.setIcon(R.drawable.wipe_all_data_d);
				waitingDialog.setTitle(R.string.title_wipe_all_data);
				waitingDialog.setCanceledOnTouchOutside(false);
				waitingDialog.show();
			}
		});
	}

	@Override
	public void showWipeAllDataErrors(List<Exception> errors) {
		showErrors(R.string.title_wipe_all_data_errors, errors);
	}

	@Override
	public void notifySuccessfulDataWipe() {
		notifyUser(R.string.msg_all_data_wiped_successfully);
	}

	@Override
	public void goToMainMenu() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(DataExchange.this, MainMenu.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_left_out);
			}
		});
	}

	@Override
	public void showMainMenuAccessLocked() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DataExchange.this);
				builder.setTitle(R.string.title_main_menu_locked)
						.setIcon(R.drawable.main_menu_locked)
						.setMessage(
								Html.fromHtml(getString(R.string.msg_main_menu_locked)))
						.setPositiveButton(R.string.btn_ok, null).show();
			}
		});
	}

	// #endregion
}
