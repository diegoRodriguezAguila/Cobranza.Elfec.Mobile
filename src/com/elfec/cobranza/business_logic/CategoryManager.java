package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.business_logic.DataImporter.ImportSource;
import com.elfec.cobranza.model.Category;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.remote_data_access.CategoryRDA;

/**
 * Se encarga de las operaciones de negocio de <b>CATEGORIAS</b> 
 * @author drodriguez
 *
 */
public class CategoryManager {
	/**
	 * Importa las CATEGORIAS de oracle y los guarda
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataAccessResult<Boolean> importCategories(final String username, final String password)
	{
		return DataImporter.importOnceRequiredData(new ImportSource<Category>() {
			@Override
			public List<Category> requestData() throws ConnectException, SQLException {
				return CategoryRDA.requestCategories(username, password);
			}
		}); 
	}
}
