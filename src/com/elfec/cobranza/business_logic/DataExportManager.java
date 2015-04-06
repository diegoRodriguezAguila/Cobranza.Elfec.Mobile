package com.elfec.cobranza.business_logic;

import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.model.WSCollection;
import com.elfec.cobranza.model.exceptions.NoPaidCollectionsException;
import com.elfec.cobranza.model.exceptions.NoPendingExportDataException;
import com.elfec.cobranza.model.results.ManagerProcessResult;

/**
 * Capa de l�gica de negocio para la exportaci�n de datos
 * @author drodriguez
 *
 */
public class DataExportManager {
	
	/**
	 * Valida las condiciones de negocio para la exportaci�n de los cobros
	 * @return resultado de la validaci�n
	 */
	public static ManagerProcessResult validateExportation()
	{
		ManagerProcessResult result = new ManagerProcessResult();		
		try 
		{
			if(CollectionPayment.getAll(CollectionPayment.class).size()==0)
				throw new NoPaidCollectionsException();
			if(CollectionPayment.getExportPendingCollections().size()==0 &&
					WSCollection.getExportPendingWSCollections().size()==0)
				throw new NoPendingExportDataException();
		} 
		catch (NoPaidCollectionsException e) 
		{ result.addError(e); } 
		catch (NoPendingExportDataException e) 
		{ result.addError(e); }
		
		return result;
	}
}
