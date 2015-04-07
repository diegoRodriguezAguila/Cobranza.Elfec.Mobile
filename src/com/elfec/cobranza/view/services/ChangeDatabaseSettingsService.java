package com.elfec.cobranza.view.services;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.presenter.services.ChangeDatabaseSettingsPresenter;
import com.elfec.cobranza.presenter.views.IChangeDatabaseSettingsDialog;

import de.keyboardsurfer.android.widget.crouton.Crouton;

/**
 * Provee de un servicio de dialogo para cambiar las configuraciones de la base de datos
 * @author drodriguez
 *
 */
public class ChangeDatabaseSettingsService implements IChangeDatabaseSettingsDialog {
	
	private AlertDialogPro.Builder dialogBuilder;
	private AlertDialogPro dialog;
	private Context context;
	private Handler mHandler;
	private de.keyboardsurfer.android.widget.crouton.Style croutonStyle;
	
	private ChangeDatabaseSettingsPresenter presenter;
	
	//components
	private View dialogView;
	private TextView txtIp;
	private TextView txtPort;
	private TextView txtService;
	private TextView txtRole;
	private TextView txtRolePassword;

	@SuppressLint("InflateParams")
	public ChangeDatabaseSettingsService(Context context)
	{
		presenter = new ChangeDatabaseSettingsPresenter(this);
		this.context = context;
		mHandler = new Handler(Looper.getMainLooper());
		croutonStyle =  new de.keyboardsurfer.android.widget.crouton.Style.Builder().setFontName("fonts/segoe_ui_semilight.ttf").setTextSize(16)
				.setBackgroundColorValue(context.getResources().getColor(R.color.cobranza_color)).build();
		dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.dialog_change_database_settings, null);
		txtIp = (TextView) dialogView.findViewById(R.id.txt_ip);
		txtPort = (TextView) dialogView.findViewById(R.id.txt_port);
		txtService = (TextView) dialogView.findViewById(R.id.txt_service);
		txtRole = (TextView) dialogView.findViewById(R.id.txt_role);
		txtRolePassword = (TextView) dialogView.findViewById(R.id.txt_role_password);
		
		setFocusChangedListeners();
		presenter.loadCurrentSettings();
		
		dialogBuilder = new AlertDialogPro.Builder(context);
		dialogBuilder.setTitle(R.string.title_change_database_settings).setIcon(R.drawable.config_server_d)
			.setView(dialogView)
			.setNegativeButton(R.string.btn_cancel, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					hideKeyboard();
				}
			})
			.setPositiveButton(R.string.btn_save, null);
	}
	
	/**
	 * Asigna los focus changed listeners a los campos
	 */
	private void setFocusChangedListeners() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				txtIp.setOnFocusChangeListener(new OnFocusChangeListener() {		
					@Override
					public void onFocusChange(View v, boolean gotFocus) {
						if(!gotFocus)
						{
							presenter.validateIpField();
						}
					}
				});
				txtPort.setOnFocusChangeListener(new OnFocusChangeListener() {		
					@Override
					public void onFocusChange(View v, boolean gotFocus) {
						if(!gotFocus)
						{
							presenter.validatePortField();
						}
					}
				});
				txtService.setOnFocusChangeListener(new OnFocusChangeListener() {		
					@Override
					public void onFocusChange(View v, boolean gotFocus) {
						if(!gotFocus)
						{
							presenter.validateServiceField();
						}
					}
				});
				txtRole.setOnFocusChangeListener(new OnFocusChangeListener() {		
					@Override
					public void onFocusChange(View v, boolean gotFocus) {
						if(!gotFocus)
						{
							presenter.validateRoleField();
						}
					}
				});
			}
		}).start();
	}

	/**
	 * Muestra el diálogo construido
	 */
	public void show()
	{
		dialog = dialogBuilder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.getButton(AlertDialogPro.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
	      {            
	          @Override
	          public void onClick(View v) {
	        	  presenter.processDatabaseSettings();
	          }
	      });
	}	
	
	/**
	 * Esconde el teclado
	 */
	private void hideKeyboard() {   
	    // Check if no view has focus:
	    View view = dialogView.findFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
	/**
	 * Muestra errores en el campo especificado
	 * @param field
	 * @param errors
	 */
	private void setFieldErrors(final TextView txtField, final List<String> errors)
	{
		mHandler.post(new Runnable() {		
			@Override
			public void run() {
				if(errors.size()>0)
					txtField.setError(MessageListFormatter.fotmatHTMLFromStringList(errors));
				else txtField.setError(null);
			}
		});
	}
	
	//#region Interface Methods

	@Override
	public String getIp() {
		return txtIp.getText().toString();
	}

	@Override
	public void setIp(final String ip) {
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				txtIp.setText(ip);
			}
		});
	}

	@Override
	public String getPort() {
		return txtPort.getText().toString();
	}

	@Override
	public void setPort(final String port) {
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				txtPort.setText(port);
			}
		});
	}

	@Override
	public String getService() {
		return txtService.getText().toString();
	}

	@Override
	public void setService(final String service) {
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				txtService.setText(service);
			}
		});
	}

	@Override
	public String getRole() {
		return txtRole.getText().toString();
	}

	@Override
	public void setRole(final String role) {
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				txtRole.setText(role);
			}
		});	
	}

	@Override
	public String getRolePassword() {
		return txtRolePassword.getText().toString().trim();
	}

	@Override
	public String getIpValidationRules() {
		return txtIp.getTag().toString();
	}

	@Override
	public void setIpFieldErrors(List<String> errors) {
		setFieldErrors(txtIp, errors);
	}

	@Override
	public String getPortValidationRules() {
		return txtPort.getTag().toString();
	}

	@Override
	public void setPortFieldErrors(List<String> errors) {
		setFieldErrors(txtPort, errors);
	}

	@Override
	public String getServiceValidationRules() {
		return txtService.getTag().toString();
	}

	@Override
	public void setServiceFieldErrors(List<String> errors) {
		setFieldErrors(txtService, errors);
	}

	@Override
	public String getRoleValidationRules() {
		return txtRole.getTag().toString();
	}

	@Override
	public void setRoleFieldErrors(List<String> errors) {
		setFieldErrors(txtRole, errors);
	}

	@Override
	public void notifyErrorsInFields() {
		Crouton.clearCroutonsForActivity((Activity)context);
		Crouton.makeText((Activity)context, R.string.errors_in_fields, croutonStyle, (ViewGroup) dialogView).show();
	}

	@Override
	public void notifySettingsSavedSuccesfully() {
		mHandler.post(new Runnable() {		
			@Override
			public void run() {
				dialog.dismiss();
				Toast.makeText(context, R.string.msg_settings_saved_successully, Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void showSaveErrors(final List<Exception> errors) {
		if(errors.size()>0)
			mHandler.post(new Runnable() {				
				@Override
				public void run() {
					Crouton.clearCroutonsForActivity((Activity)context);
					Crouton.makeText((Activity)context, 
							MessageListFormatter.fotmatHTMLFromErrors(errors), 
							croutonStyle, (ViewGroup) dialogView).show();
				}
			});
	}
	
	//#endregion
}
