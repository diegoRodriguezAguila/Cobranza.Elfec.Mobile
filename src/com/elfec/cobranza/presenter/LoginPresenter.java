package com.elfec.cobranza.presenter;

import java.util.List;

import android.os.Looper;

import com.elfec.cobranza.business_logic.ElfecUserManager;
import com.elfec.cobranza.business_logic.FieldValidator;
import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.business_logic.ZonesManager;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.presenter.views.ILoginView;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

public class LoginPresenter {

	private ILoginView view;

	public LoginPresenter(ILoginView view) {
		this.view = view;
	}
	
	/**
	 * Intenta logear al usuario con la informaci�n proporcionada
	 */
	public void login()
	{
		boolean usernameIsValid = validateUsernameField();
		boolean passwordIsValid = validatePasswordField();
		if(usernameIsValid && passwordIsValid)
		{
			startThreadedUserValidation();
		}
		else view.notifyErrorsInFields();
	}

	/**
	 * Realiza las validaciones respectivas para poder logear al usuario, todo en un hilo
	 */
	private void startThreadedUserValidation() {
		Thread thread = new Thread(new Runnable() {				
			@Override
			public void run() {
				Looper.prepare();
				view.showWaiting();
				String password = view.getPassword();
				DataAccessResult<User> result = ElfecUserManager.validateUser(view.getUsername(), password, view.getIMEI());
				result = importUserZones(password, result);
				view.hideWaiting();
				view.clearPassword();
				if(!result.hasErrors())
				{
					view.goToLoadData();
					SessionManager.startSession(result.getResult());
					OracleDatabaseConnector.disposeInstance();
				}
				view.showLoginErrors(result.getErrors());
				Looper.loop();
			}
		});
		thread.start();
	}

	/**
	 * Llama a los m�todos necesarios para importar las zonas del usuario si es que es necesario hacerlo
	 * @param password
	 * @param result
	 * @return
	 */
	private DataAccessResult<User> importUserZones(String password,
			DataAccessResult<User> result) {
		if(!result.hasErrors() && result.isRemoteDataAccess())
		{
			result = ZonesManager.importUserZones(result.getResult(), password);
		}
		return result;
	}
	/**
	 * Valida el campo del nombre de usuario, si hay errores se le mostraran al usuario
	 */
	public boolean validateUsernameField()
	{
		List<String> validationErrors = FieldValidator.validate("usuario", true, view.getUsername(), view.getUsernameValidationRules());
		view.setUsernameFieldErrors(validationErrors);
		return validationErrors.size()==0;
	}
	
	/**
	 * Valida el campo del nombre de la contrase�a, si hay errores se le mostraran al usuario
	 */
	public boolean validatePasswordField()
	{
		List<String> validationErrors = FieldValidator.validate("contrase�a", false, view.getPassword(), view.getPasswordValidationRules());
		view.setPasswordFieldErrors(validationErrors);
		return validationErrors.size()==0;
	}
	
}
