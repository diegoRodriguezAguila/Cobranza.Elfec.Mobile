package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.model.SupplyCategoryType;
import com.elfec.cobranza.model.downloaders.DataImporter;
import com.elfec.cobranza.model.downloaders.DataImporter.ImportSource;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.SupplyCategoryTypeRDA;

/**
 * Se encarga de las operaciones de negocio de TIPOS_CATEG_SUM
 * @author drodriguez
 *
 */
public class SupplyCategoryTypeManager {

	/**
	 * Importa los TIPOS_CATEG_SUM de oracle y los guarda
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataAccessResult<Boolean> importSupplyCategoryTypes(final String username, final String password)
	{
		return DataImporter.importOnceRequiredData(new ImportSource<SupplyCategoryType>() {
			@Override
			public List<SupplyCategoryType> requestData() throws ConnectException, SQLException {
				return SupplyCategoryTypeRDA.requestSupplyCategoryTypes(username, password);
			}
		});
	}
}
