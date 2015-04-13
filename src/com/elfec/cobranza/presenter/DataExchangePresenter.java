package com.elfec.cobranza.presenter;

import com.elfec.cobranza.R;
import com.elfec.cobranza.business_logic.CollectionManager;
import com.elfec.cobranza.business_logic.DataExportManager;
import com.elfec.cobranza.business_logic.RoleAccessManager;
import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.business_logic.WSCollectionManager;
import com.elfec.cobranza.business_logic.ZonesManager;
import com.elfec.cobranza.helpers.security.AES;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.events.DataExportListener;
import com.elfec.cobranza.model.results.ManagerProcessResult;
import com.elfec.cobranza.presenter.views.IDataExchangeView;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

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
				ManagerProcessResult result = DataExportManager.validateExportation();
				result = enableRole(result);
				result = exportCollectionPayments(result);
				result = exportWSCollections(result);
				result = unlockRemoteRoutes(result);
				result = wipeAllData(result);
				view.hideWaiting();
				view.showExportationErrors(result.getErrors());
				if(!result.hasErrors())
				{
					view.notifySuccessfulDataExportation();
					 closeCurrentSession();
				}
			}
		}).start();
	}
	
	/**
	 * Invoca a los metodos necesarios para habilitar el rol MOVIL_COBRANZA
	 * @param result
	 * @return
	 */
	protected ManagerProcessResult enableRole(
			ManagerProcessResult result) {
		if(!result.hasErrors())
		{
			result = RoleAccessManager.enableMobileCollectionRole(username, password);
		}
		return result;
	}

	/**
	 * Invoca a los metodos necesarios para desbloquear
	 * remotamente las rutas descargadas
	 * @param result
	 * @return
	 */
	protected ManagerProcessResult unlockRemoteRoutes(
			ManagerProcessResult result) {
		if(!result.hasErrors())
		{
			result = ZonesManager.setRemoteZoneRoutesUnlocked(username, password, view.getIMEI(), new DataExportListener() {			
				@Override
				public void onExporting(int exportCount, int totalElements) {
					view.updateProgress(exportCount, totalElements);
				}			
				@Override
				public void onExportInitialized(int totalElements) {
					view.updateWaiting(R.string.msg_unlocking_remote_routes, totalElements);
				}
				
				@Override
				public void onExportFinalized() {}
			});
		}
		return result;
	}

	/**
	 * Invoca a los metodos necesarios para la exportación de COBROS
	 */
	protected ManagerProcessResult exportCollectionPayments(ManagerProcessResult result) {
		if(!result.hasErrors())
		{
			result = CollectionManager.exportAllCollectionPayments(username, password, new DataExportListener() {			
				@Override
				public void onExporting(int exportCount, int totalElements) {
					view.updateProgress(exportCount, totalElements);
				}			
				@Override
				public void onExportInitialized(int totalElements) {
					view.updateWaiting(R.string.msg_uploading_collection_payments, totalElements);
				}
				
				@Override
				public void onExportFinalized() {}
			});
		}
		return result;
	}
	
	/**
	 * Invoca a los metodos necesarios para la exportación de COB_WS
	 */
	protected ManagerProcessResult exportWSCollections(ManagerProcessResult result) {
		if(!result.hasErrors())
		{
			result = WSCollectionManager.exportAllWSCollections(username, password, new DataExportListener() {			
				@Override
				public void onExporting(int exportCount, int totalElements) {
					view.updateProgress(exportCount, totalElements);
				}			
				@Override
				public void onExportInitialized(int totalElements) {
					view.updateWaiting(R.string.msg_uploading_ws_collections, totalElements);
				}
				
				@Override
				public void onExportFinalized() {}
			});
		}
		return result;
	}
	
	/**
	 * Llama a los metodos necesarios para eliminar toda la información
	 * @param result
	 * @return result
	 */
	protected ManagerProcessResult wipeAllData(ManagerProcessResult result) {
		if(result==null || !result.hasErrors())
		{
			view.updateWaiting(R.string.msg_wiping_all_data);
			result = DataExportManager.wipeAllData();
			OracleDatabaseConnector.disposeInstance();
		}
		return result;
	}
	
	/**
	 * Se utiliza en la opción de eliminación de datos
	 * toma las acciones necesarias de la interfaz para realizarlo
	 */
	public void processDataWipe()
	{
		new Thread(new Runnable() {		
			@Override
			public void run() {
				view.showWipingDataWait();
				ManagerProcessResult result = wipeAllData(null);
				view.hideWaiting();
				view.showWipeAllDataErrors(result.getErrors());
				if(!result.hasErrors())
				{
					view.notifySuccessfulDataWipe();
					closeCurrentSession();
				}
			}
		}).start();	
	}
}
