package com.elfec.cobranza.presenter;

import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.presenter.views.IDataFlowView;

public class DataFlowPresenter {

	private IDataFlowView view;
	
	public DataFlowPresenter(IDataFlowView view)
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
}
