package com.elfec.cobranza.presenter;

import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.presenter.views.IDataFlowView;

public class DataExchangePresenter {

	private IDataFlowView view;
	
	public DataExchangePresenter(IDataFlowView view)
	{
		this.view = view;		
	}
	
	/**
	 * Asigna los campos del usuario y el numero de caja
	 */
	public void setFields()
	{
		this.view.setCurrentUser(SessionManager.getLoggedInUsername());
		this.view.setCashdeskNumber(SessionManager.getLoggedCashdeskNumber());
	}
	
	/**
	 * Llama a los metodos de control de sesion para finalizarla
	 */
	public void closeCurrentSession()
	{
		Thread thread = new Thread(new Runnable() {		
			@Override
			public void run() {
				String username = SessionManager.getLoggedInUsername();
				SessionManager.finishSession();
				view.notifySessionClosed(username);
			}
		});
		thread.start();
	}
}
