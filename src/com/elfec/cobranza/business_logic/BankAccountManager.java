package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.elfec.cobranza.business_logic.data_exchange.DataImporter;
import com.elfec.cobranza.business_logic.data_exchange.DataImporter.ImportSource;
import com.elfec.cobranza.model.BankAccount;
import com.elfec.cobranza.model.PeriodBankAccount;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.remote_data_access.BankAccountRDA;

/**
 * Se encarga de las operaciones de negocio de <b>BAN_CTAS</b> y <b>BAN_CTAS_PER</b>
 * @author drodriguez
 *
 */
public class BankAccountManager {
	/**
	 * Importa las BAN_CTAS de oracle y los guarda
	 * @param username
	 * @param password
	 * @param cashdeskNumber
	 * @return
	 */
	public static DataAccessResult<Boolean> importBankAccounts(final String username, final String password, final int cashdeskNumber)
	{
		return DataImporter.importOnceRequiredData(new ImportSource<BankAccount>() {
			@Override
			public List<BankAccount> requestData() throws ConnectException, SQLException {
				return BankAccountRDA.requestBankAccounts(username, password, cashdeskNumber);
			}
		}); 
	}
	
	/**
	 * Importa las BAN_CTAS_PER de oracle y los guarda
	 * @param username
	 * @param password
	 * @param cashdeskNumber
	 * @return
	 */
	public static DataAccessResult<Boolean> importPeriodBankAccounts(final String username, final String password, final int cashdeskNumber)
	{
		return DataImporter.importOnceRequiredData(new ImportSource<PeriodBankAccount>() {
			@Override
			public List<PeriodBankAccount> requestData() throws ConnectException, SQLException {
				return BankAccountRDA.requestPeriodBankAccounts(username, password, cashdeskNumber);
			}
		}); 
	}
}
