package com.elfec.cobranza.presenter;

import java.util.List;

import com.elfec.cobranza.R;
import com.elfec.cobranza.business_logic.BankAccountManager;
import com.elfec.cobranza.business_logic.CalculationBaseManager;
import com.elfec.cobranza.business_logic.CategoryManager;
import com.elfec.cobranza.business_logic.ConceptManager;
import com.elfec.cobranza.business_logic.CoopReceiptManager;
import com.elfec.cobranza.business_logic.ReceiptConceptManager;
import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.business_logic.SupplyCategoryTypeManager;
import com.elfec.cobranza.business_logic.SupplyStatusManager;
import com.elfec.cobranza.business_logic.ZonesManager;
import com.elfec.cobranza.helpers.PreferencesManager;
import com.elfec.cobranza.helpers.security.AES;
import com.elfec.cobranza.helpers.text_format.ObjectListToSQL;
import com.elfec.cobranza.helpers.text_format.ObjectListToSQL.AttributePicker;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.Zone;
import com.elfec.cobranza.model.interfaces.ImportCaller;
import com.elfec.cobranza.model.interfaces.OnceRequiredDataImportCaller;
import com.elfec.cobranza.presenter.views.IZoneRoutesView;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

public class ZoneRoutesPresenter {

	private IZoneRoutesView view;
	private String selectedRoutesString;
	private String coopReceiptIdsString;
	private String username;
	private String password;
	private int cashdeskNumber;
	private int zoneRemoteId;

	public ZoneRoutesPresenter(IZoneRoutesView view) {
		this.view = view;
	}
	
	/**
	 * Carga las rutas de la zona según su id remoto
	 * @param zoneRemoteId
	 */
	public void loadZoneRoutes(final int zoneRemoteId)
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {	
				ZoneRoutesPresenter.this.zoneRemoteId = zoneRemoteId;
				List<Route> zoneRoutes = Zone.findByRemoteId(zoneRemoteId).getZoneRoutes();
				view.setZoneRoutes(zoneRoutes);
			}
		});
		thread.start();
	}
	
	/**
	 * Invoca a los metodos necesarios para importar la información de las rutas
	 * @param selectedRoutes
	 */
	public void importRoutesData(final List<Route> selectedRoutes)
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {	
				view.showWaiting();
				initializeDataImport(selectedRoutes);				
				DataAccessResult<?> result = new DataAccessResult<Boolean>();
				result = importAllOnceRequiredData(result);				
				result = importRoutesData(result);		
				OracleDatabaseConnector.disposeInstance();
				boolean hasErrors = result.hasErrors();
				if(!hasErrors)	
				{
					ZonesManager.setZoneRoutesLoaded(selectedRoutes);
					loadZoneRoutes(zoneRemoteId);
				}
				view.hideWaiting();
				if(!hasErrors)	
					view.successfullyImportation();
			}
		});
		thread.start();
	}
	
	/**
	 * Inicializa los atributos para la importación de datos
	 * @param selectedRoutes
	 */
	private void initializeDataImport(final List<Route> selectedRoutes) {
		selectedRoutesString = ObjectListToSQL.convertToSQL(selectedRoutes, new AttributePicker<Route>() {
			@Override
			public String pickString(Route route) {
				return ""+route.getRouteRemoteId();
			}});
		username = SessionManager.getLoggedInUsername();
		cashdeskNumber = SessionManager.getLoggedCashdeskNumber();
		User user = User.findByUserName(username);
		password = AES.decrypt(user.generateUserKey(), user.getEncryptedPassword());
	}
	
	/**
	 * Importa la información que solo se requiere una vez
	 * @return
	 */
	private DataAccessResult<?> importAllOnceRequiredData(DataAccessResult<?> result) {
		if(!PreferencesManager.instance().isAllOnceReqDataImported())
		{
			result = importData(result, R.string.msg_downloading_supply_category_types, new OnceRequiredDataImportCaller(){
				@Override
				public DataAccessResult<?> callImport() {
					return SupplyCategoryTypeManager.importSupplyCategoryTypes(username, password);
				}
				@Override
				public boolean isAlreadyImported() {
					return PreferencesManager.instance().isSupplyCategoryTypesImported();
				}
				@Override
				public void setImportationResult(boolean successfullyImport) {
					PreferencesManager.instance().setSupplyCategoryTypesImported(successfullyImport);
				}});
			result = importData(result, R.string.msg_downloading_cpt_calculation_bases, new OnceRequiredDataImportCaller() {			
				@Override
				public DataAccessResult<?> callImport() {
					return CalculationBaseManager.importConceptCalculationBases(username, password);
				}			
				@Override
				public boolean isAlreadyImported() {
					return PreferencesManager.instance().isConceptCalculationBasesImported();
				}
				@Override
				public void setImportationResult(boolean successfullyImport) {
					PreferencesManager.instance().setConceptCalculationBasesImported(successfullyImport);
				}
			});
			result = importData(result, R.string.msg_downloading_prnt_calculation_bases, new OnceRequiredDataImportCaller() {			
				@Override
				public DataAccessResult<?> callImport() {
					return CalculationBaseManager.importPrintCalculationBases(username, password);
				}			
				@Override
				public boolean isAlreadyImported() {
					return PreferencesManager.instance().isPrintCalculationBasesImported();
				}
				@Override
				public void setImportationResult(boolean successfullyImport) {
					PreferencesManager.instance().setPrintCalculationBasesImported(successfullyImport);
				}
			});
			result = importData(result, R.string.msg_downloading_categories, new OnceRequiredDataImportCaller() {			
				@Override
				public DataAccessResult<?> callImport() {
					return CategoryManager.importCategories(username, password);
				}			
				@Override
				public boolean isAlreadyImported() {
					return PreferencesManager.instance().isCategoriesImported();
				}
				@Override
				public void setImportationResult(boolean successfullyImport) {
					PreferencesManager.instance().setCategoriesImported(successfullyImport);
				}
			});
			result = importData(result, R.string.msg_downloading_concepts, new OnceRequiredDataImportCaller() {			
				@Override
				public DataAccessResult<?> callImport() {
					return ConceptManager.importConcepts(username, password);
				}			
				@Override
				public boolean isAlreadyImported() {
					return PreferencesManager.instance().isConceptsImported();
				}
				@Override
				public void setImportationResult(boolean successfullyImport) {
					PreferencesManager.instance().setConceptsImported(successfullyImport);
				}
			});
			result = importData(result, R.string.msg_downloading_bank_accounts, new OnceRequiredDataImportCaller() {			
				@Override
				public DataAccessResult<?> callImport() {
					return BankAccountManager.importBankAccounts(username, password, cashdeskNumber);
				}			
				@Override
				public boolean isAlreadyImported() {
					return PreferencesManager.instance().isBankAccountsImported();
				}
				@Override
				public void setImportationResult(boolean successfullyImport) {
					PreferencesManager.instance().setBankAccountsImported(successfullyImport);
				}
			});
			result = importData(result, R.string.msg_downloading_period_bank_accounts, new OnceRequiredDataImportCaller() {			
				@Override
				public DataAccessResult<?> callImport() {
					return BankAccountManager.importPeriodBankAccounts(username, password, cashdeskNumber);
				}			
				@Override
				public boolean isAlreadyImported() {
					return PreferencesManager.instance().isPeriodBankAccountsImported();
				}
				@Override
				public void setImportationResult(boolean successfullyImport) {
					PreferencesManager.instance().setPeriodBankAccountsImported(successfullyImport);
				}
			});
			PreferencesManager.instance().setAllOnceReqDataImported(!result.hasErrors());
		}
		return result;
	}
	
	/**
	 * Importa toda la información de la ruta actual
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DataAccessResult<?> importRoutesData(DataAccessResult<?> result) {
		DataAccessResult<List<CoopReceipt>> receiptsResult = (DataAccessResult<List<CoopReceipt>>) importData(result, R.string.msg_downloading_coop_receipts, new ImportCaller() {			
			@Override
			public DataAccessResult<?> callImport() {
				return CoopReceiptManager.importCoopReceipts(username, password, selectedRoutesString);
			}
		});
		if(receiptsResult.getResult().size()==0)
			receiptsResult.addError(new Exception("La(s) ruta(s) seleccionada(s) no tiene(n) ninguna factura disponible!"));//TODO crear propia excepcion
		coopReceiptIdsString = ObjectListToSQL.convertToSQL(receiptsResult.getResult(), "IDCBTE", new AttributePicker<CoopReceipt>(){
			@Override
			public String pickString(CoopReceipt object) {
				return ""+object.getReceiptId();
			}});
		result = importData(receiptsResult, R.string.msg_downloading_supply_status, new ImportCaller() {
			
			@Override
			public DataAccessResult<?> callImport() {
				return SupplyStatusManager.importSupplyStatuses(username, password, coopReceiptIdsString);
			}
		});
		result = importData(result, R.string.msg_downloading_receipt_concepts, new ImportCaller() {			
			@Override
			public DataAccessResult<?> callImport() {
				return ReceiptConceptManager.importCoopReceipts(username, password, coopReceiptIdsString);
			}
		});
		result = importData(result, R.string.msg_downloading_fine_bonusess, new ImportCaller() {			
			@Override
			public DataAccessResult<?> callImport() {
				return ReceiptConceptManager.importCoopReceipts(username, password, coopReceiptIdsString);
			}
		});
		return result;
	}
	
	/**
	 * Llama a los métodos para realizar una importación y también actualiza el mensaje de espera 
	 * y muestra los errores en caso de haber sucedido
	 * @param lastResult
	 * @param loadingMessageResId
	 * @param caller
	 * @return
	 */
	private DataAccessResult<?> importData(DataAccessResult<?> lastResult, int loadingMessageResId, ImportCaller caller )
	{
		boolean alreadyImported = false;
		boolean isOnceReqData = (caller instanceof OnceRequiredDataImportCaller);
		if(isOnceReqData)
			alreadyImported = ((OnceRequiredDataImportCaller)caller).isAlreadyImported();
		if(!lastResult.hasErrors() && !alreadyImported)
		{
			view.updateWaitingMessage(loadingMessageResId);
			DataAccessResult<?> result = caller.callImport();
			view.showImportErrors(result.getErrors());
			if(isOnceReqData)
				((OnceRequiredDataImportCaller)caller).setImportationResult(!result.hasErrors());
			return result;
		}
		return lastResult;
	}
}
