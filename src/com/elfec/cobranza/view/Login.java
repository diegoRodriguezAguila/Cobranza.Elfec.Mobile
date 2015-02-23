package com.elfec.cobranza.view;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.elfec.cobranza.R;
import com.elfec.cobranza.presenter.LoginPresenter;
import com.elfec.cobranza.presenter.views.ILoginView;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

public class Login extends Activity implements ILoginView {

	private LoginPresenter presenter;
	
	private de.keyboardsurfer.android.widget.crouton.Style croutonStyle;
	
	private EditText txtUsername;
	private EditText txtPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		presenter = new LoginPresenter(this);
		txtUsername = (EditText) findViewById(R.id.txt_username);
		txtPassword = (EditText) findViewById(R.id.txt_password);
		OracleDatabaseConnector.initializeContext(this);
		croutonStyle =  new de.keyboardsurfer.android.widget.crouton.Style.Builder().setFontName("fonts/segoe_ui_semilight.ttf").setTextSize(16)
				.setBackgroundColorValue(getResources().getColor(R.color.cobranza_color)).build();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	
	public void btnLoginClick(View view)
	{
		presenter.login();
	}
	
	
	//#region Interface Methods

	@Override
	public String getUsername() {
		return txtUsername.getText().toString().toLowerCase(Locale.getDefault());
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
		txtPassword.setText(null);
	}
	
	//#endregion
}
