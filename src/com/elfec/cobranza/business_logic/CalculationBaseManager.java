package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.business_logic.data_exchange.DataImporter;
import com.elfec.cobranza.business_logic.data_exchange.DataImporter.ImportSource;
import com.elfec.cobranza.model.ConceptCalculationBase;
import com.elfec.cobranza.model.PrintCalculationBase;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.CalculationBaseRDA;

/**
 * Se encarga de las operaciones de negocio de <b>GBASES_CALC_CPTOS</b> y <b>GBASES_CALC_IMP</b>
 * @author drodriguez
 *
 */
public class CalculationBaseManager {

	/**
	 * Importa los <b>GBASES_CALC_CPTOS</b> de oracle y los guarda
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataAccessResult<Boolean> importConceptCalculationBases(final String username, final String password)
	{
		return DataImporter.importOnceRequiredData(new ImportSource<ConceptCalculationBase>() {
			@Override
			public List<ConceptCalculationBase> requestData() throws ConnectException, SQLException {
				return CalculationBaseRDA.requestConceptCalculationBases(username, password);
			}
		});
	}
	
	/**
	 * Importa los <b>GBASES_CALC_IMP</b> de oracle y los guarda
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataAccessResult<Boolean> importPrintCalculationBases(final String username, final String password)
	{
		return DataImporter.importOnceRequiredData(new ImportSource<PrintCalculationBase>() {
			@Override
			public List<PrintCalculationBase> requestData() throws ConnectException, SQLException {
				return CalculationBaseRDA.requestPrintCalculationBases(username, password);
			}
		});
	}
}
