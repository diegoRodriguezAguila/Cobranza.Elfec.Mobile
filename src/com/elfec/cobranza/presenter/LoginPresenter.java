package com.elfec.cobranza.presenter;

import java.util.List;

import android.os.Looper;

import com.elfec.cobranza.business_logic.ElfecUserManager;
import com.elfec.cobranza.business_logic.FieldValidator;
import com.elfec.cobranza.presenter.views.ILoginView;

public class LoginPresenter {

	private ILoginView view;

	public LoginPresenter(ILoginView view) {
		this.view = view;
	}
	
	/**
	 * Intenta logear al usuario con la información proporcionada
	 */
	public void login()
	{
		boolean usernameIsValid = validateUsernameField();
		boolean passwordIsValid = validatePasswordField();
		if(usernameIsValid && passwordIsValid)
		{
			Thread thread = new Thread(new Runnable() {				
				@Override
				public void run() {
					Looper.prepare();
					view.showWaiting();
					List<Exception> validationErrors = ElfecUserManager.validateUser(view.getUsername(), view.getPassword());
					view.hideWaiting();
					if(validationErrors.size()==0)
						view.goToMainMenu();
					else view.showLoginErrors(validationErrors);
					Looper.loop();
				}
			});
			thread.start();
		}
		else
		{
			view.notifyErrorsInFields();
		}
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
	 * Valida el campo del nombre de la contraseña, si hay errores se le mostraran al usuario
	 */
	public boolean validatePasswordField()
	{
		List<String> validationErrors = FieldValidator.validate("contraseña", false, view.getPassword(), view.getPasswordValidationRules());
		view.setPasswordFieldErrors(validationErrors);
		return validationErrors.size()==0;
	}
	
}
