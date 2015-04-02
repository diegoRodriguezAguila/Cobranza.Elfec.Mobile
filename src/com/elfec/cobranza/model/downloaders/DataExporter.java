package com.elfec.cobranza.model.downloaders;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.activeandroid.Model;
import com.elfec.cobranza.model.enums.ExportStatus;
import com.elfec.cobranza.model.exceptions.ExportationException;
import com.elfec.cobranza.model.interfaces.IExportable;
import com.elfec.cobranza.model.results.DataAccessResult;

/**
 * Se encarga de exportar cualquier tipo de informaci�n
 * @author drodriguez
 *
 */
public class DataExporter {
	/**
	 * Importa cualquier tipo de informaci�n que debe ser importada una sola vez
	 * @param importSource
	 * @return
	 */
	public static <T extends Model & IExportable> DataAccessResult<Boolean> exportData(ExportSpecs<T> exportSpecs)
	{
		DataAccessResult<Boolean> result = new DataAccessResult<Boolean>(true);
		try {
				List<T> dataList = exportSpecs.requestExportData();
				int rowRes;
				for(T data : dataList)
				{
					rowRes = exportSpecs.exportData(data);
					if(rowRes==1) //se insert� existosamente
					{
						data.setExportStatus(ExportStatus.EXPORTED);
						data.save();
					}
					else throw new ExportationException(data.getRegistryResume());
				}
				result.setResult(true);
		} catch (ConnectException e) {
			result.addError(e);
		} catch (SQLException e) {
			e.printStackTrace();
			result.addError(new ExportationException(e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			result.addError(e);
		}
		return result;
	}
	
	/**
	 * Interfaz que utiliza DataExporter para determinar la fuente de los datos que se quieren exportar
	 * y el m�todo de exportaci�n
	 * @author drodriguez
	 *
	 * @param <T>
	 */
	public interface ExportSpecs<T extends Model & IExportable>
	{
		/**
		 * Obtiene la informaci�n que se exportar�
		 * @return Lista
		 */
		public List<T> requestExportData();
		/**
		 * M�todo que se llama para exportar la informaci�n
		 * @return el numero de filas exportadas exitosamente, deber�a ser 1
		 */
		public int exportData(T data) throws ConnectException, SQLException;
	}
}
