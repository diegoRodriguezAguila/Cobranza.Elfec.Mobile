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
import com.elfec.cobranza.model.enums.DataExchangeStatus;
import com.elfec.cobranza.model.events.DataExportListener;
import com.elfec.cobranza.model.results.ManagerProcessResult;
import com.elfec.cobranza.presenter.views.IDataExchangeView;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;
import com.elfec.cobranza.settings.PreferencesManager;

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
	 * Valida si es que se puede exportar la informaci�n al servidor y
	 * en el caso de que todo sea correcto la exporta
	 */
	public void handleUpload()
	{
		new Thread(new Runnable() {		
			@Override
			public void run() {
				view.showWaiting();
				setUserAndPasswordVariables();
				ManagerProcessResult result = DataExportManager.validateExportation();
				result = enableRole(result);
				result = exportWSCollections(result);/* NECESARIO primero llamar a ws 
				para que se asignen los n�meros de transacci�n remotos */
				result = exportCollectionPayments(result);				
				result = unlockRemoteRoutes(result, DataExchangeStatus.EXPORTED);
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
	 * Asigna las variables de usuario y password
	 */
	public void setUserAndPasswordVariables() {
		username = SessionManager.getLoggedInUsername();
		User user = User.findByUserName(username);
		password = AES.decrypt(user.generateUserKey(), user.getEncryptedPassword());
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
	 * @param unlockType el tipo de desbloqueo si por eliminaci�n o descarga, no se 
	 * puede enviar {@link DataExchangeStatus}.IMPORTED da excepci�n
	 * @return resultado del manejador
	 */
	protected ManagerProcessResult unlockRemoteRoutes(
			ManagerProcessResult result,  DataExchangeStatus unlockType) {
		if(!result.hasErrors())
		{
			result = ZonesManager.setRemoteZoneRoutesUnlocked(username, password, view.getIMEI(), 
					unlockType, new DataExportListener() {			
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
	 * Invoca a los metodos necesarios para la exportaci�n de COBROS
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
	 * Invoca a los metodos necesarios para la exportaci�n de COB_WS
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
	 * Llama a los metodos necesarios para eliminar toda la informaci�n
	 * @param result
	 * @return result
	 */
	protected ManagerProcessResult wipeAllData(ManagerProcessResult result) {
		if(!result.hasErrors())
		{
			view.updateWaiting(R.string.msg_wiping_all_data);
			result = DataExportManager.wipeAllData();
			OracleDatabaseConnector.disposeInstance();
		}
		return result;
	}
	
	/**
	 * Se utiliza en la opci�n de eliminaci�n de datos
	 * toma las acciones necesarias de la interfaz para realizarlo
	 */
	public void processDataWipe()
	{
		new Thread(new Runnable() {		
			@Override
			public void run() {
				view.showWipingDataWait();
				setUserAndPasswordVariables();
				ManagerProcessResult result = new ManagerProcessResult();
				result = enableRole(result);
				result = unlockRemoteRoutes(result, DataExchangeStatus.DELETED);
				result = wipeAllData(result);
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
	
	/**
	 * Valida si es que se puede ingresar al men� principal
	 */
	public void validateMainMenuOption()
	{
		new Thread(new Runnable() {		
			@Override
			public void run() {
				if(PreferencesManager.instance().isAllOnceReqDataImported())
					view.goToMainMenu();
				else view.showMainMenuAccessLocked();
			}
		}).start();
	}
}
