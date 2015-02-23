package com.elfec.cobranza.presenter;

import com.elfec.cobranza.model.User;
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
		User localUser = User.findByUserName(view.getUsername());
		if(localUser!=null)
		{
			
		}
	}
	
}
