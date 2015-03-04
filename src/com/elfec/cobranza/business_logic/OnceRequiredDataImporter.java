package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.elfec.cobranza.model.DataAccessResult;

/**
 * Se encarga de importar cualquier tipo de información que debe ser importada una sola vez
 * @author drodriguez
 *
 */
public class OnceRequiredDataImporter {
	/**
	 * Importa la información y la guarda
	 * @param username
	 * @param password
	 * @param importSource
	 * @return
	 */
	public static <T extends Model> DataAccessResult<Boolean> importData(ImportSource<T> importSource)
	{
		DataAccessResult<Boolean> result = new DataAccessResult<Boolean>(true);
		ActiveAndroid.beginTransaction();
		try {
				List<T> dataList = importSource.requestData();
				for(T data : dataList)
				{
					data.save();
				}
				ActiveAndroid.setTransactionSuccessful();
				result.setResult(true);
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
	
	public static interface ImportSource<T extends Model>
	{
		public List<T> requestData() throws ConnectException, SQLException;
	}
}
