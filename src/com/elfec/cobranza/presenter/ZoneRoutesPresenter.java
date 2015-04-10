package com.elfec.cobranza.presenter;

import java.util.List;

import com.elfec.cobranza.R;
import com.elfec.cobranza.business_logic.AnnulmentReasonManager;
import com.elfec.cobranza.business_logic.BankAccountManager;
import com.elfec.cobranza.business_logic.CalculationBaseManager;
import com.elfec.cobranza.business_logic.CategoryManager;
import com.elfec.cobranza.business_logic.ConceptManager;
import com.elfec.cobranza.business_logic.CoopReceiptManager;
import com.elfec.cobranza.business_logic.DataExchangeControlManager;
import com.elfec.cobranza.business_logic.FineBonusManager;
import com.elfec.cobranza.business_logic.ReceiptConceptManager;
import com.elfec.cobranza.business_logic.PrinterImagesManager;
import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.business_logic.SupplyCategoryTypeManager;
import com.elfec.cobranza.business_logic.SupplyManager;
import com.elfec.cobranza.business_logic.SupplyStatusManager;
import com.elfec.cobranza.business_logic.ZonesManager;
import com.elfec.cobranza.helpers.security.AES;
import com.elfec.cobranza.helpers.text_format.AttributePicker;
import com.elfec.cobranza.helpers.text_format.ObjectListToSQL;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.Zone;
import com.elfec.cobranza.model.events.OnImportFinished;
import com.elfec.cobranza.model.events.OnRoutesImportConfirmed;
import com.elfec.cobranza.model.exceptions.RouteWithNoReceiptException;
import com.elfec.cobranza.model.interfaces.ImportCaller;
import com.elfec.cobranza.model.interfaces.OnceRequiredDataImportCaller;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.presenter.views.IZoneRoutesView;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;
import com.elfec.cobranza.settings.PreferencesManager;

public class ZoneRoutesPresenter {

	private IZoneRoutesView view;
	private  List<Route> selectedRoutes;
	private  List<Route> lockedRoutes;
	private String selectedRoutesString;
	private String coopReceiptIdsString;
	private String supplyIdsString;
	private String username;
	private String password;
	private int cashdeskNumber;
	private int zoneRemoteId;
	private boolean importFinished, supplyFinished, supplyStatusFinished, 
					receiptConceptFinished, fineBonusFinished;

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
	public void starDataImportation(final List<Route> selectedRoutes)
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {	
				view.showWaiting();
				initializeDataImport(selectedRoutes);				
				DataAccessResult<?> result = new DataAccessResult<Boolean>();
				result = importAllOnceRequiredData(result);	
				final DataAccessResult<Boolean> res = verifyRoutesDisponibility(result);
				OnRoutesImportConfirmed event = new OnRoutesImportConfirmed() {					
					@Override
					public void importConfirmed() {
						importRoutesData(res, new OnImportFinished() {					
							@Override
							public void importCallback(DataAccessResult<?> result) {
								OracleDatabaseConnector.disposeInstance();
								result = lockRoutes(result);
								setLoadedRoutes(result.hasErrors());
								view.hideWaiting();
								if(!result.hasErrors())	
									view.successfullyImportation();
							}
						});
					}
				};		
				if(res.getResult())
				{
					view.warnLockedRoutes(lockedRoutes, event, selectedRoutes.size()==0);
				}
				else event.importConfirmed();
			}
		}).start();
	}
	
	/**
	 * Verifica la disponibilidad de ser cargadas de las rutas
	 */
	protected DataAccessResult<Boolean> verifyRoutesDisponibility(DataAccessResult<?> result) {
		DataAccessResult<Boolean> funcResult = new DataAccessResult<Boolean>(true);
		if(!result.hasErrors())
		{
			view.addWaitingMessage(R.string.msg_verifying_route_availability, true);
			DataAccessResult<List<Route>> res = DataExchangeControlManager
					.filterLockedRoutes(username, password, selectedRoutes);
			lockedRoutes = res.getResult();
			view.showImportErrors(result.getErrors());
			if(!res.hasErrors())
			{
				selectedRoutes.removeAll(res.getResult());
				convertSelectedRoutesToString();			
			}
			funcResult.setResult(!res.hasErrors() && res.getResult().size()>0);
		}
		return funcResult;
	}
	
	/**
	 * Convierte las rutas seleccionadas a una cadena usable en consulta IN
	 */
	private void convertSelectedRoutesToString()
	{
		selectedRoutesString = ObjectListToSQL.convertToSQL(selectedRoutes, new AttributePicker<String, Route>() {
			@Override
			public String pickAttribute(Route route) {
				return ""+route.getRouteRemoteId();
			}});
	}

	/**
	 * Inicializa los atributos para la importación de datos
	 * @param selectedRoutes
	 */
	private void initializeDataImport(final List<Route> selectedRoutes) {
		lockedRoutes = null;
		this.selectedRoutes = selectedRoutes;		
		convertSelectedRoutesToString();
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
		PrinterImagesManager.importReceiptImages();
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
			result = importData(result, R.string.msg_downloading_annulment_reasons, new OnceRequiredDataImportCaller() {			
				@Override
				public DataAccessResult<?> callImport() {
					return AnnulmentReasonManager.importAnnulmentReasons(username, password);
				}			
				@Override
				public boolean isAlreadyImported() {
					return PreferencesManager.instance().isAnnulmentReasonsImported();
				}
				@Override
				public void setImportationResult(boolean successfullyImport) {
					PreferencesManager.instance().setAnnulmentReasonsImported(successfullyImport);
				}
			});

			PreferencesManager.instance().setAllOnceReqDataImported(!result.hasErrors());
		}
		return result;
	}
	
	/**
	 * Importa toda la información de la ruta actual
	 * @param result
	 * @param importCallback
	 */
	@SuppressWarnings("unchecked")
	private void importRoutesData(DataAccessResult<?> result, final OnImportFinished importCallback) {
		DataAccessResult<List<CoopReceipt>> receiptsResult = (DataAccessResult<List<CoopReceipt>>) importData(result, R.string.msg_downloading_coop_receipts,
		new ImportCaller() {			
			@Override
			public DataAccessResult<?> callImport() {
				return CoopReceiptManager.importCoopReceipts(username, password, selectedRoutesString);
			}
		});
		if(!receiptsResult.hasErrors() && receiptsResult.getResult().size()==0)
			receiptsResult.addError(new RouteWithNoReceiptException());
		if(receiptsResult.hasErrors())
		{
			importCallback.importCallback(receiptsResult);
		}
		else
		{
			coopReceiptIdsString = ObjectListToSQL.convertToSQL(receiptsResult.getResult(), "IDCBTE", new AttributePicker<String, CoopReceipt>(){
				@Override
				public String pickAttribute(CoopReceipt object) {
					return ""+object.getReceiptId();
				}});
			supplyIdsString =  ObjectListToSQL.convertToSQL(receiptsResult.getResult(), "IDSUMINISTRO", new AttributePicker<String, CoopReceipt>(){
				@Override
				public String pickAttribute(CoopReceipt object) {
					return ""+object.getSupplyId();
				}});
			importFinished = supplyFinished = supplyStatusFinished = receiptConceptFinished = fineBonusFinished = false;
			OnImportFinished dataImportCallback = new OnImportFinished() {				
				@Override
				public synchronized void importCallback(DataAccessResult<?> result) {
					if(result.hasErrors() && !importFinished)
					{
						importFinished = true;
						OracleDatabaseConnector.disposeInstance();
						view.showImportErrors(result.getErrors());
						importCallback.importCallback(result);
					}
					if(supplyFinished && supplyStatusFinished && receiptConceptFinished && 
							fineBonusFinished && !result.hasErrors() && !importFinished)
					{
						importFinished = true;
						view.addWaitingMessage(R.string.msg_download_finished, true);
						importCallback.importCallback(result);
					}
				}
			};
			view.deleteWaitingMessage(R.string.msg_downloading_coop_receipts);
			threadedImportSupplies(dataImportCallback);
			threadedImportSupplyStatuses(dataImportCallback);
			threadedImportReceiptConcepts(dataImportCallback);
			threadedImportFineBonuses(dataImportCallback);
		}
	}

	/**
	 * Llama a la importación de SUMINISTROS en un hilo aparte
	 * @param lastResult
	 * @param importCallback
	 */
	private void threadedImportSupplies(final OnImportFinished importCallback) {
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				DataAccessResult<?> result = threadImportData(R.string.msg_downloading_supplies, new ImportCaller() {		
					@Override
					public DataAccessResult<?> callImport() {
						return SupplyManager.importSupplies(username, password, supplyIdsString);
					}
				});
				supplyFinished = true;
				importCallback.importCallback(result);
			}
		});
		thread.start();
	}
	/**
	 * Llama a la importación de SUMIN_ESTADOS en un hilo aparte
	 * @param lastResult
	 * @param importCallback
	 */
	private void threadedImportSupplyStatuses(final OnImportFinished importCallback) {
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				DataAccessResult<?> result = threadImportData(R.string.msg_downloading_supply_status, new ImportCaller() {		
					@Override
					public DataAccessResult<?> callImport() {
						return SupplyStatusManager.importSupplyStatuses(username, password, coopReceiptIdsString);
					}
				});
				supplyStatusFinished = true;
				importCallback.importCallback(result);
			}
		});
		thread.start();
	}
	/**
	 * Llama a la importación de CBTES_CPTOS en un hilo aparte
	 * @param lastResult
	 * @param importCallback
	 */
	private void threadedImportReceiptConcepts(final OnImportFinished importCallback) {
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				DataAccessResult<?> result = threadImportData(R.string.msg_downloading_receipt_concepts, new ImportCaller() {			
					@Override
					public DataAccessResult<?> callImport() {
						return ReceiptConceptManager.importCoopReceipts(username, password, coopReceiptIdsString);
						}
				});
				receiptConceptFinished = true;
				importCallback.importCallback(result);
			}
		});
		thread.start();
	}
	/**
	 * Llama a la importación de BONIF_MULTAS en un hilo aparte
	 * @param lastResult
	 * @param importCallback
	 */
	private void threadedImportFineBonuses(final OnImportFinished importCallback) {
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				DataAccessResult<?> result = threadImportData(R.string.msg_downloading_fine_bonusess, new ImportCaller() {			
					@Override
					public DataAccessResult<?> callImport() {
						return FineBonusManager.importFineBonuses(username, password, coopReceiptIdsString);
					}
				});	
				fineBonusFinished = true;
				importCallback.importCallback(result);
			}
		});
		thread.start();
	}

	/**
	 * Llama a los métodos para realizar una importación y también actualiza el mensaje de espera 
	 * y muestra los errores en caso de haber sucedido
	 * @param lastResult
	 * @param loadingMessageResId
	 * @param caller
	 * @return
	 */
	private DataAccessResult<?> importData(DataAccessResult<?> lastResult, int loadingMessageResId, ImportCaller caller)
	{
		boolean alreadyImported = false;
		boolean isOnceReqData = (caller instanceof OnceRequiredDataImportCaller);
		if(isOnceReqData)
			alreadyImported = ((OnceRequiredDataImportCaller)caller).isAlreadyImported();
		if(!lastResult.hasErrors() && !alreadyImported)
		{
			view.addWaitingMessage(loadingMessageResId, true);
			DataAccessResult<?> result = caller.callImport();
			view.showImportErrors(result.getErrors());
			if(isOnceReqData)
				((OnceRequiredDataImportCaller)caller).setImportationResult(!result.hasErrors());
			return result;
		}
		return lastResult;
	}
	
	/**
	 * Llama a los métodos para realizar una importación y también actualiza el mensaje de espera
	 * @param lastResult
	 * @param loadingMessageResId
	 * @param caller
	 * @return
	 */
	private DataAccessResult<?> threadImportData(int loadingMessageResId, ImportCaller caller)
	{
		view.addWaitingMessage(loadingMessageResId, false);
		DataAccessResult<?> result = caller.callImport();
		view.deleteWaitingMessage(loadingMessageResId);
		return result;
	}

	/**
	 * Bloquea las rutas en el servidor
	 * @param selectedRoutes
	 * @param result
	 * @param hasErrors
	 * @return
	 */
	public DataAccessResult<?> lockRoutes(DataAccessResult<?> result) {
		if(!result.hasErrors())	
		{
			result = ZonesManager.setRemoteZoneRoutesLocked(selectedRoutes, 
					username, password, view.getIMEI());
			view.showImportErrors(result.getErrors());
		}
		return result;
	}
	
	/**
	 * Marca las rutas localmente como cargadas
	 * @param hasErrors
	 */
	public void setLoadedRoutes(boolean hasErrors)
	{
		if(!hasErrors)	
		{
			ZonesManager.setZoneRoutesLoaded(selectedRoutes);
			loadZoneRoutes(zoneRemoteId);
		}
	}
}
