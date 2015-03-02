package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.SupplyCategoryType;
import com.elfec.cobranza.remote_data_access.SupplyCategoryTypeRDA;

/**
 * Se encarga de las operaciones de negocio de TIPOS_CATEG_SUM
 * @author drodriguez
 *
 */
public class SupplyCategoryTypesManager {

	/**
	 * Importa los TIPOS_CATEG_SUM de oracle y los guarda
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataAccessResult<List<SupplyCategoryType>> importSupplyCategoryTypes(String username, String password)
	{
		DataAccessResult<List<SupplyCategoryType>> result = new DataAccessResult<List<SupplyCategoryType>>(true);
		ActiveAndroid.beginTransaction();
		try {
				List<SupplyCategoryType> supplyCategTypes = SupplyCategoryTypeRDA.requestSupplyCategoryTypes(username, password);
				for(SupplyCategoryType suppCategType : supplyCategTypes)
				{
					suppCategType.save();
				}
				ActiveAndroid.setTransactionSuccessful();
		} catch (ConnectException e) {
			result.addError(e);
		} catch (SQLException e) {
			e.printStackTrace();
			result.addError(e);
		}
		finally{
			ActiveAndroid.endTransaction();
		}
		return result;
	}
}
