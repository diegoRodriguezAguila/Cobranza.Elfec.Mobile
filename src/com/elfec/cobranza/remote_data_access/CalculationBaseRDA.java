package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.ConceptCalculationBase;
import com.elfec.cobranza.model.PrintCalculationBase;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para
 *  operaciones relacionadas con <b>GBASES_CALC_CPTOS</b> y <b>GBASES_CALC_IMP</b>
 * @author drodriguez
 */
public class CalculationBaseRDA {
	/**
	 * Obtiene las GBASES_CALC_CPTOS de oracle
	 * @param username
	 * @param password
	 * @return Lista de GBASES_CALC_CPTOS
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<ConceptCalculationBase> requestConceptCalculationBases(String username, String password) throws ConnectException, SQLException
	{
		List<ConceptCalculationBase> conceptCalculationBases = new ArrayList<ConceptCalculationBase>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT * FROM ERP_ELFEC.GBASES_CALC_CPTOS");
		while(rs.next())
		{
			conceptCalculationBases.add(new ConceptCalculationBase(rs.getInt("IDBASE_CALCULO"), rs.getInt("IDCONCEPTO"), rs.getInt("IDSUBCONCEPTO")));
		}
		return conceptCalculationBases;
	}
	
	/**
	 * Obtiene las GBASES_CALC_IMP de oracle
	 * @param username
	 * @param password
	 * @return Lista de GBASES_CALC_IMP
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<PrintCalculationBase> requestPrintCalculationBases(String username, String password) throws ConnectException, SQLException
	{
		List<PrintCalculationBase> printCalculationBase = new ArrayList<PrintCalculationBase>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT * FROM ERP_ELFEC.GBASES_CALC_IMP");
		while(rs.next())
		{
			printCalculationBase.add(new PrintCalculationBase(rs.getInt("IDBASE_CALCULO"), rs.getShort("ORDEN_IMPRESION"), rs.getString("DESCRIPCION")));
		}
		return printCalculationBase;
	}
}
