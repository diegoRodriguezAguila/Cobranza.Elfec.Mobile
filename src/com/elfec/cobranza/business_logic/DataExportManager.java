package com.elfec.cobranza.business_logic;

import java.io.File;

import android.database.sqlite.SQLiteDatabase;

import com.activeandroid.ActiveAndroid;
import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.WSCollection;
import com.elfec.cobranza.model.exceptions.NoPaidCollectionsException;
import com.elfec.cobranza.model.exceptions.NoPendingExportDataException;
import com.elfec.cobranza.model.results.ManagerProcessResult;
import com.elfec.cobranza.settings.PreferencesManager;

/**
 * Capa de lógica de negocio para la exportación de datos
 * @author drodriguez
 *
 */
public class DataExportManager {
	
	/**
	 * Valida las condiciones de negocio para la exportación de los cobros
	 * @return resultado de la validación
	 */
	public static ManagerProcessResult validateExportation()
	{
		ManagerProcessResult result = new ManagerProcessResult();		
		try 
		{
			if(CollectionPayment.getAll(CollectionPayment.class).size()==0)
				throw new NoPaidCollectionsException();
			if(CollectionPayment.getExportPendingCollections().size()==0 &&
					WSCollection.getExportPendingWSCollections().size()==0 &&
					Route.getAllLoadedRoutes().size()==0)
				throw new NoPendingExportDataException();
		} 
		catch (NoPaidCollectionsException e) 
		{ result.addError(e); } 
		catch (NoPendingExportDataException e) 
		{ result.addError(e); }
		
		return result;
	}
	
	/**
	 * Elimina toda la información de la aplicación que se debe eliminar del dispositivo
	 * @return
	 */
	public static ManagerProcessResult wipeAllData()
	{
		ManagerProcessResult result = new ManagerProcessResult();	
		try
		{
			ActiveAndroid.getDatabase().close();
			SQLiteDatabase.deleteDatabase(new File(ActiveAndroid.getDatabase().getPath()));
			ActiveAndroid.dispose();
			ActiveAndroid.initialize(PreferencesManager.getApplicationContext());
			PreferencesManager.instance().wipeOnceRequiredDataPreferences();
		}
		catch(Exception e)
		{
			result.addError(new RuntimeException("Ocurrió un error al eliminar la información local! "
					+ "Es probable que la información se haya corrompido, porfavor elimine los datos desde el "
					+ "administrador de aplicaciones de Android! Info. adicional: "+e.getMessage()));
		}
		
		return result;
	}
}
