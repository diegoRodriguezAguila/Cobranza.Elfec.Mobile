package com.elfec.cobranza.business_logic.data_exchange;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.activeandroid.Model;
import com.elfec.cobranza.model.enums.ExportStatus;
import com.elfec.cobranza.model.events.DataExportListener;
import com.elfec.cobranza.model.exceptions.ExportationException;
import com.elfec.cobranza.model.interfaces.IExportable;
import com.elfec.cobranza.model.results.DataAccessResult;

/**
 * Se encarga de exportar cualquier tipo de información
 * @author drodriguez
 *
 */
public class DataExporter {

	/**
	 * Importa cualquier tipo de información que debe ser importada una sola vez
	 * @param importSource
	 * @return
	 */
	public static <T extends Model & IExportable> DataAccessResult<Boolean> exportData(ExportSpecs<T> exportSpecs, DataExportListener exportListener)
	{
		DataAccessResult<Boolean> result = new DataAccessResult<Boolean>(true);
		if(exportListener==null)
			exportListener = new DataExportListener() {//DUMMY Listener
			@Override
				public void onExporting(int exportCount, int totalElements) {}
				@Override
				public void onExportInitialized(int totalElements) {}				
				@Override
				public void onExportFinalized() {}
			};//DUMMY Listener
			
		try {
				List<T> dataList = exportSpecs.requestExportData();
				int size = dataList.size();
				exportListener.onExportInitialized(size);
				int rowRes, count = 0;
				for(T data : dataList)
				{
					rowRes = exportSpecs.exportData(data);
					if(rowRes==1) //se insertó existosamente
					{
						data.setExportStatus(ExportStatus.EXPORTED);
						data.save();
						count++;
						exportListener.onExporting(count, size);
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
		exportListener.onExportFinalized();
		return result;
	}
	
	/**
	 * Interfaz que utiliza DataExporter para determinar la fuente de los datos que se quieren exportar
	 * y el método de exportación
	 * @author drodriguez
	 *
	 * @param <T>
	 */
	public interface ExportSpecs<T extends Model & IExportable>
	{
		/**
		 * Obtiene la información que se exportará
		 * @return Lista
		 */
		public List<T> requestExportData();
		/**
		 * Método que se llama para exportar la información
		 * @return el numero de filas exportadas exitosamente, debería ser 1
		 */
		public int exportData(T data) throws ConnectException, SQLException;
	}
}
