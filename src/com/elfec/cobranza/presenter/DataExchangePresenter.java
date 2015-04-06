package com.elfec.cobranza.presenter;

import com.elfec.cobranza.business_logic.CollectionManager;
import com.elfec.cobranza.business_logic.DataExportManager;
import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.helpers.security.AES;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.events.DataExportListener;
import com.elfec.cobranza.model.results.ManagerProcessResult;
import com.elfec.cobranza.presenter.views.IDataExchangeView;

public class DataExchangePresenter {

	private IDataExchangeView view;
	private String username;
	private String password;
	
	public DataExchangePresenter(IDataExchangeView view)
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
		new Thread(new Runnable() {		
			@Override
			public void run() {
				String username = SessionManager.getLoggedInUsername();
				SessionManager.finishSession();
				view.notifySessionClosed(username);
			}
		}).start();
	}
	
	/**
	 * Valida si es que se puede exportar la información al servidor y
	 * en el caso de que todo sea correcto la exporta
	 */
	public void handleUpload()
	{
		new Thread(new Runnable() {		
			@Override
			public void run() {
				view.showWaiting();
				username = SessionManager.getLoggedInUsername();
				User user = User.findByUserName(username);
				password = AES.decrypt(user.generateUserKey(), user.getEncryptedPassword());
				ManagerProcessResult validationResult = DataExportManager.validateExportation();
				if(validationResult.hasErrors())
				{
					exportCollectionPayments();
				}
				else view.hideWaiting();
				view.showExportValidationErrors(validationResult.getErrors());
			}
		}).start();
	}

	/**
	 * Invoca a los metodos necesarios para la exportación de COBROS
	 */
	protected void exportCollectionPayments() {
		CollectionManager.exportAllCollectionPayments(username, password, new DataExportListener() {			
			@Override
			public void onExporting(int exportCount, int totalElements) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void onExportInitializing(int totalElements) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onExportFinalized() {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
