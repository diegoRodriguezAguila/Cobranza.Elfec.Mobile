package com.elfec.cobranza.business_logic.data_exchange;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.elfec.cobranza.model.results.DataAccessResult;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

/**
 * Se encarga de importar cualquier tipo de información
 * @author drodriguez
 *
 */
public class DataImporter {
	
	/**
	 * Importa cualquier tipo de información que debe ser importada una sola vez
	 * @param importSource
	 * @return
	 */
	public static <T extends Model> DataAccessResult<Boolean> importOnceRequiredData(ImportSource<T> importSource)
	{
		DataAccessResult<Boolean> result = new DataAccessResult<Boolean>(true);
		try {
				List<T> dataList = importSource.requestData();
				ActiveAndroid.beginTransaction();
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
		}catch (Exception e) {
			e.printStackTrace();
			result.addError(e);
		}
		finally{
			ActiveAndroid.endTransaction();
		}
		return result;
	}
	
	/**
	 * Importa cualquier tipo de información
	 * @param importSpecs
	 * @return
	 */
	public static <T extends Model, TResult> DataAccessResult<TResult> importData(ImportSpecs<T, TResult> importSpecs)
	{
		DataAccessResult<TResult> result = new DataAccessResult<TResult>(true);
		try {
				List<T> dataList = importSpecs.requestData();
				ActiveAndroid.beginTransaction();
				for(T data : dataList)
				{
					data.save();
				}
				ActiveAndroid.setTransactionSuccessful();
				result.setResult(importSpecs.resultHandle(dataList));
		} catch (ConnectException e) {
			result.addError(e);
		} catch (SQLException e) {
			e.printStackTrace();
			result.addError(e);
		}catch (Exception e) {
			e.printStackTrace();
			result.addError(e);
		}
		finally{
			if(ActiveAndroid.inTransaction())
			ActiveAndroid.endTransaction();
		}
		return result;
	}
	
	/**
	 * Interfaz que utiliza DataImporter para determinar la fuente de los datos que se quieren importar
	 * @author drodriguez
	 *
	 * @param <T>
	 */
	public static interface ImportSource<T extends Model>
	{
		/**
		 * Obtiene la información
		 * @return Lista de tipo T
		 * @throws ConnectException
		 * @throws SQLException
		 */
		public List<T> requestData() throws ConnectException, SQLException;
	}
	/**
	 * Interfaz que utiliza DataImporter para determinar la fuente de los datos que se quieren importar y también cómo se interpretarán 
	 * los datos obtenidos
	 * @author drodriguez
	 *
	 * @param <T>
	 * @param <TResult>
	 */
	public static interface ImportSpecs<T extends Model, TResult> extends ImportSource<T>
	{
		/**
		 * Convierte la lista de objetos importados al resultado deseado del acceso a datos
		 * @param importList
		 * @return
		 */
		public TResult resultHandle(List<T> importList);
	}
}
