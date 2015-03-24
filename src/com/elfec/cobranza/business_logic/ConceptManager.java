package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.business_logic.DataImporter.ImportSource;
import com.elfec.cobranza.model.Concept;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.remote_data_access.ConceptRDA;

/**
 * Se encarga de las operaciones de negocio de <b>CONCEPTOS</b> 
 * @author drodriguez
 *
 */
public class ConceptManager {
	/**
	 * Importa los CONCEPTOS de oracle y los guarda
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataAccessResult<Boolean> importConcepts(final String username, final String password)
	{
		return DataImporter.importOnceRequiredData(new ImportSource<Concept>() {
			@Override
			public List<Concept> requestData() throws ConnectException, SQLException {
				return ConceptRDA.requestConcepts(username, password);
			}
		}); 
	}

}
