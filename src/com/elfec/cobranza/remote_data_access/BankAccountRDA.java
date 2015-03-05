package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.model.BankAccount;
import com.elfec.cobranza.model.PeriodBankAccount;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para
 *  operaciones relacionadas con <b>BAN_CTAS</b> y <b>BAN_CTAS_PER</b>
 * @author drodriguez
 */
public class BankAccountRDA {

	/**
	 * Obtiene remotamente las BAN_CTAS
	 * @param username
	 * @param password
	 * @param cashdeskNumber
	 * @return Lista de BAN_CTAS
	 * @throws SQLException 
	 * @throws ConnectException 
	 */
	public static List<BankAccount> requestBankAccounts(String username, String password, int cashdeskNumber) throws ConnectException, SQLException
	{
		List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
					executeSelect("SELECT * FROM ERP_ELFEC.BAN_CTAS WHERE IDBAN_CTA="+cashdeskNumber);
		while(rs.next())
		{
			bankAccounts.add(new BankAccount(rs.getInt("IDBANCO"), rs.getInt("IDBAN_CTA"), rs.getString("NROCUENTA"), 
					rs.getString("TIPO_CTA"), rs.getBigDecimal("SALDO"), new DateTime(rs.getDate("SALDO_FECHA")), 
					rs.getInt("IDCENTRO_COSTO"), rs.getString("DESCRIPCION"), rs.getShort("DA_VUELTO"), rs.getShort("DA_ANTICIPO"), 
					rs.getInt("IDEMPRESA"), rs.getInt("IDSUCURSAL"), rs.getInt("IDZONA"), rs.getInt("IDTIPO_CAJA"), 
					rs.getInt("NROCAJA_EXT"), rs.getInt("IDCENTRO_COSTO_CR"), rs.getInt("IDCTA_CONTAB_CR"), 
					rs.getInt("IDCENTRO_COSTO_DB"), rs.getInt("IDCTA_CONTAB_DB"), rs.getInt("IDTARJETA")));
		}
		rs.close();
		return bankAccounts;
	}
	
	/**
	 * Obtiene remotamente las BAN_CTAS_PER
	 * @param username
	 * @param password
	 * @param cashdeskNumber
	 * @return Lista de BAN_CTAS_PER
	 * @throws SQLException 
	 * @throws ConnectException 
	 */
	public static List<PeriodBankAccount> requestPeriodBankAccounts(String username, String password, int cashdeskNumber) throws ConnectException, SQLException
	{
		List<PeriodBankAccount> periodBankAccounts = new ArrayList<PeriodBankAccount>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
					executeSelect("SELECT * FROM ERP_ELFEC.BAN_CTAS_PER WHERE IDBAN_CTA="+cashdeskNumber+" AND FECHA>=trunc(SYSDATE)");
		while(rs.next())
		{
			periodBankAccounts.add(new PeriodBankAccount(rs.getInt("IDBANCO"), rs.getInt("IDBAN_CTA"), rs.getInt("NROPERIODO"), 
					rs.getInt("IDCAJERO"), new DateTime(rs.getDate("HORA_APERTURA")), new DateTime(rs.getDate("HORA_CIERRE")), 
					rs.getShort("IDSTATUS"), rs.getInt("IDZONA"), rs.getInt("IDEMPRESA")));
		}
		rs.close();
		return periodBankAccounts;
	}
}
