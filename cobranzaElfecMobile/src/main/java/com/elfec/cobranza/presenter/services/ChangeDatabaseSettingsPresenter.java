package com.elfec.cobranza.presenter.services;

import com.elfec.cobranza.business_logic.FieldValidator;
import com.elfec.cobranza.model.enums.ConnectionParam;
import com.elfec.cobranza.model.results.ManagerProcessResult;
import com.elfec.cobranza.presenter.views.IChangeDatabaseSettingsDialog;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;
import com.elfec.cobranza.settings.PreferencesManager;
import com.elfec.cobranza.settings.remote_data_access.OracleDatabaseSettings;

import java.util.List;
import java.util.Map;

public class ChangeDatabaseSettingsPresenter {
	private IChangeDatabaseSettingsDialog view;
	
	private Map<String, String> settings;

	public ChangeDatabaseSettingsPresenter(IChangeDatabaseSettingsDialog view) {
		this.view = view;
	}
	
	/**
	 * Procesa el cambio de configuración en la conexión de base de datos
	 */
	public void processDatabaseSettings() {
		boolean isIpValid = validateIpField();
		boolean isPortValid = validatePortField();
		boolean isServiceValid = validateServiceField();
		boolean isRoleValid = validateRoleField();
		if(isIpValid && isPortValid && isServiceValid && isRoleValid)
		{
			startThreadedSettingsSaver();
		}
		else view.notifyErrorsInFields();
	}
	
	/**
	 * Carga las configuraciones de base de datos actuales
	 */
	public void loadCurrentSettings()
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				settings = OracleDatabaseSettings.getConnectionSettings(PreferencesManager.getApplicationContext());
				view.setIp(settings.get(ConnectionParam.IP.toString()));
				view.setPort(settings.get(ConnectionParam.PORT.toString()));
				view.setService(settings.get(ConnectionParam.SERVICE.toString()));
				view.setRole(settings.get(ConnectionParam.ROLE.toString()));
			}
		}).start();
	}
	
	/**
	 * Inicia el proceso de guardado de las nuevas configuraciones
	 */
	private void startThreadedSettingsSaver() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				if(settings!=null)
				{
					String password = view.getRolePassword();
					password = password.isEmpty()?settings.get(ConnectionParam.PASSWORD.toString()):password;
					settings.put(ConnectionParam.IP.toString(), view.getIp());
					settings.put(ConnectionParam.PORT.toString(), view.getPort());
					settings.put(ConnectionParam.SERVICE.toString(), view.getService());
					settings.put(ConnectionParam.ROLE.toString(), view.getRole());
					settings.put(ConnectionParam.PASSWORD.toString(), password);
					ManagerProcessResult result = OracleDatabaseSettings.overwriteConnectionSettings(PreferencesManager.getApplicationContext(), 
							settings);
					view.showSaveErrors(result.getErrors());
					if(!result.hasErrors())
					{
						OracleDatabaseConnector.disposeInstance();
						view.notifySettingsSavedSuccesfully();
					}
				}
			}
		}).start();
	}

	/**
	 * Valida el campo de la IP, si hay errores se le mostraran al usuario
	 */
	public boolean validateIpField()
	{
		List<String> validationErrors = FieldValidator.validate("ip", false, view.getIp(), view.getIpValidationRules());
		view.setIpFieldErrors(validationErrors);
		return validationErrors.size()==0;
	}
	
	/**
	 * Valida el campo del puerto, si hay errores se le mostraran al usuario
	 */
	public boolean validatePortField()
	{
		List<String> validationErrors = FieldValidator.validate("puerto", true, view.getPort(), view.getPortValidationRules());
		view.setPortFieldErrors(validationErrors);
		return validationErrors.size()==0;
	}
	
	/**
	 * Valida el campo del servicio, si hay errores se le mostraran al usuario
	 */
	public boolean validateServiceField()
	{
		List<String> validationErrors = FieldValidator.validate("nombre del servicio", true, view.getService(), view.getServiceValidationRules());
		view.setServiceFieldErrors(validationErrors);
		return validationErrors.size()==0;
	}
	
	/**
	 * Valida el campo del rol, si hay errores se le mostraran al usuario
	 */
	public boolean validateRoleField()
	{
		List<String> validationErrors = FieldValidator.validate("rol", true, view.getRole(), view.getRoleValidationRules());
		view.setRoleFieldErrors(validationErrors);
		return validationErrors.size()==0;
	}
	
}
