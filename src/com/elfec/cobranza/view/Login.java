package com.elfec.cobranza.view;

import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.alertdialogpro.AlertDialogPro;
import com.alertdialogpro.ProgressDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.presenter.LoginPresenter;
import com.elfec.cobranza.presenter.views.ILoginView;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;
import de.keyboardsurfer.android.widget.crouton.Crouton;

public class Login extends Activity implements ILoginView {

	private long lastClickTime = 0;
	private LoginPresenter presenter;
	
	private de.keyboardsurfer.android.widget.crouton.Style croutonStyle;
	
	private EditText txtUsername;
	private EditText txtPassword;
	private ProgressDialogPro waitingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		presenter = new LoginPresenter(this);
		txtUsername = (EditText) findViewById(R.id.txt_username);
		txtPassword = (EditText) findViewById(R.id.txt_password);
		OracleDatabaseConnector.initializeContext(this);
		getActionBar().setTitle(R.string.title_activity_login);
		croutonStyle =  new de.keyboardsurfer.android.widget.crouton.Style.Builder().setFontName("fonts/segoe_ui_semilight.ttf").setTextSize(16)
				.setBackgroundColorValue(getResources().getColor(R.color.cobranza_color)).build();
		setOnFocusChangedListeners();
		//TEST PRUPOUSES
		txtUsername.setText("ototora");
		txtPassword.setText("E1206");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	
	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	
	public void btnLoginClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			presenter.login();
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	 /**
     * Asigna los onFocusChange listeners del txtUsername y el txtPassword
     */
    public void setOnFocusChangedListeners()
    {
    	Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				txtUsername.setOnFocusChangeListener(new OnFocusChangeListener() {		
					@Override
					public void onFocusChange(View v, boolean gotFocus) {
						if(!gotFocus)
						{
							presenter.validateUsernameField();
						}
					}
				});
				txtPassword.setOnFocusChangeListener(new OnFocusChangeListener() {		
					@Override
					public void onFocusChange(View v, boolean gotFocus) {
						if(!gotFocus)
						{
							presenter.validatePasswordField();
						}
					}
				});
			}
		});
    	thread.start();
    }
	
	
	//#region Interface Methods

	@Override
	public String getUsername() {
		return txtUsername.getText().toString().toUpperCase(Locale.getDefault());
	}

	@Override
	public String getPassword() {
		return txtPassword.getText().toString();
	}
	
	@Override
	public String getIMEI() {
		return ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	@Override
	public void clearPassword() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				txtPassword.setText(null);
			}
		});
	}

	@Override
	public String getUsernameValidationRules() {
		return txtUsername.getTag().toString();
	}

	@Override
	public String getPasswordValidationRules() {
		return txtPassword.getTag().toString();
	}

	@Override
	public void setUsernameFieldErrors(List<String> errors) {
		if(errors.size()>0)
			txtUsername.setError(MessageListFormatter.fotmatHTMLFromStringList(errors));
		else txtUsername.setError(null);
	}

	@Override
	public void setPasswordFieldErrors(List<String> errors) {
		if(errors.size()>0)
			txtPassword.setError(MessageListFormatter.fotmatHTMLFromStringList(errors));
		else txtPassword.setError(null);
	}

	@Override
	public void notifyErrorsInFields() {
		Crouton.clearCroutonsForActivity(this);
		Crouton.makeText(this, R.string.errors_in_fields, croutonStyle).show();
	}

	@Override
	public void showWaiting() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				waitingDialog = new ProgressDialogPro(Login.this, R.style.Theme_FlavoredMaterialLight);
				waitingDialog.setMessage(getResources().getString(R.string.msg_login_waiting));
				waitingDialog.setCancelable(false);
				waitingDialog.setCanceledOnTouchOutside(false);
				waitingDialog.show();
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
	public void goToLoadData() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				Intent i = new Intent(Login.this, DataExchange.class);
				i.putExtra(DataExchange.IMEI, getIMEI());
				startActivity(i);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
	}

	@Override
	public void showLoginErrors(final List<Exception> validationErrors) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(validationErrors.size()>0)
				{
					AlertDialogPro.Builder builder = new AlertDialogPro.Builder(Login.this);
					builder.setTitle(R.string.title_login_errors)
					.setMessage(MessageListFormatter.fotmatHTMLFromErrors(validationErrors))
					.setPositiveButton(R.string.btn_ok, null)
					.show();
				}
			}
		});
	}
	
	//#endregion
}
