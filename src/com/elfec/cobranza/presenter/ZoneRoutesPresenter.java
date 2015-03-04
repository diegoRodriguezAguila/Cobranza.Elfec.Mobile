package com.elfec.cobranza.presenter;

import java.util.List;

import com.elfec.cobranza.R;
import com.elfec.cobranza.business_logic.BankAccountManager;
import com.elfec.cobranza.business_logic.CalculationBaseManager;
import com.elfec.cobranza.business_logic.CategoryManager;
import com.elfec.cobranza.business_logic.ConceptManager;
import com.elfec.cobranza.business_logic.CoopReceiptManager;
import com.elfec.cobranza.business_logic.SessionManager;
import com.elfec.cobranza.business_logic.SupplyCategoryTypeManager;
import com.elfec.cobranza.business_logic.SupplyStatusManager;
import com.elfec.cobranza.helpers.security.AES;
import com.elfec.cobranza.helpers.text_format.ObjectListToSQL;
import com.elfec.cobranza.helpers.text_format.ObjectListToSQL.AttributePicker;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.Zone;
import com.elfec.cobranza.presenter.views.IZoneRoutesView;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

public class ZoneRoutesPresenter {

	private IZoneRoutesView view;
	private String selectedRoutesString;
	private String coopReceiptIdsString;
	private String username;
	private String password;
	private int cashdeskNumber;

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
				
				DataAccessResult<Boolean> result = importOnceRequiredData();
				
				view.updateWaitingMessage(R.string.msg_downloading_coop_receipts);
				DataAccessResult<List<CoopReceipt>> receiptsResult = CoopReceiptManager.importCoopReceipts(username, password, selectedRoutesString);
				coopReceiptIdsString = ObjectListToSQL.convertToSQL(receiptsResult.getResult(), "IDCBTE", new AttributePicker<CoopReceipt>(){
					@Override
					public String pickString(CoopReceipt object) {
						return ""+object.getReceiptId();
					}});
				view.showImportErrors(receiptsResult.getErrors());
				
				view.updateWaitingMessage(R.string.msg_downloading_supply_status);
				result = SupplyStatusManager.importSupplyStatuses(username, password, coopReceiptIdsString);
				view.showImportErrors(receiptsResult.getErrors());
				
				
				
				/*view.updateWaitingMessage(R.string.msg_downloading_supply_status);
				result = SupplyStatusManager.importSupplyStatuses(username, password, selectedRoutesString);
				view.showImportErrors(result.getErrors());*/
				view.hideWaiting();
				if(!result.hasErrors())
				{
					view.successfullyImportation();
					OracleDatabaseConnector.disposeInstance();
				}
				view.showImportErrors(result.getErrors());
			}
		});
		thread.start();
	}
	
	/**
	 * Importa la información que solo se requiere una vez
	 * @return
	 */
	private DataAccessResult<Boolean> importOnceRequiredData() {
		view.updateWaitingMessage(R.string.msg_downloading_supply_category_types);
		DataAccessResult<Boolean> result = SupplyCategoryTypeManager.importSupplyCategoryTypes(username, password);
		view.showImportErrors(result.getErrors());
		
		view.updateWaitingMessage(R.string.msg_downloading_cpt_calculation_bases);
		result = CalculationBaseManager.importConceptCalculationBases(username, password);
		view.showImportErrors(result.getErrors());
		
		view.updateWaitingMessage(R.string.msg_downloading_prnt_calculation_bases);
		result = CalculationBaseManager.importPrintCalculationBases(username, password);
		view.showImportErrors(result.getErrors());
		
		view.updateWaitingMessage(R.string.msg_downloading_categories);
		result = CategoryManager.importCategories(username, password);
		view.showImportErrors(result.getErrors());
		
		view.updateWaitingMessage(R.string.msg_downloading_concepts);
		result = ConceptManager.importConcepts(username, password);
		view.showImportErrors(result.getErrors());
		
		view.updateWaitingMessage(R.string.msg_downloading_bank_accounts);
		result = BankAccountManager.importBankAccounts(username, password, cashdeskNumber);
		view.showImportErrors(result.getErrors());
		
		view.updateWaitingMessage(R.string.msg_downloading_period_bank_accounts);
		result = BankAccountManager.importPeriodBankAccounts(username, password, cashdeskNumber);
		view.showImportErrors(result.getErrors());
		return result;
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
}
