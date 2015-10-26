package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.FineBonus;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee de una capa de acceso a datos remotos de Oracle para BONIF_MULTAS, BONIF_MULTAS_IT pertenecientes a cada CBTE_CPTOS
 * @author drodriguez
 *
 */
public class FineBonusRDA {
	/**
	 * Obtiene las BONIF_MULTAS de oracle
	 * @param username
	 * @param password
	 * @param coopReceiptIds una lista de ids de todas las facturas (IDCBTE) que se necesitaran en formato (123,534,234)
	 * @return Lista de CATEGORIAS
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<FineBonus> requestFineBonuses(String username, String password, String coopReceiptIds) throws ConnectException, SQLException
	{
		List<FineBonus> fineBonuses = new ArrayList<FineBonus>();
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		ResultSet rs = stmt.executeQuery("SELECT /*+CHOOSE*/  A.IDCBTE, A.IDCONCEPTO, "
						+ "trim(A.DESCRIPCION)||' '|| substr (bm.descripcion_legal, 1,instr(bm.descripcion_legal,'/')+2) descripcion, "
						+ "A.IMPORTE, B.IMPRESION_AREA "
						+ "FROM  (select * from erp_elfec.cbtes_CPTOS where  (IDCBTE in "+coopReceiptIds+")"
						+ " AND (IDEMPRESA = 1) AND (IDSUCURSAL = 10) AND (TIPO_CBTE = 'FC') AND (LETRA_CBTE = 'Y') "
						+ "AND IMPORTE<>0 AND IDCONCEPTO BETWEEN 10600 AND 10699) A, "
						+ "(select idconcepto,idsubconcepto,impresion_area from ERP_ELFEC.CONCEPTOS where IMPRESION_AREA=4) B, "
						+ "(select /*+CHOOSE*/ idcbte,idconcepto, idsubconcepto,IDBONIF_MULTA from ERP_ELFEC.BONIF_MULTAS_IT where idcbte in "+coopReceiptIds+") bo,"
						+ " ERP_ELFEC.BONIF_MULTAS bm "
						+ "WHERE A.IDCONCEPTO = B.IDCONCEPTO "
						+ "AND A.IDSUBCONCEPTO = B.IDSUBCONCEPTO "
						+ "AND A.IDCBTE = BO.IDCBTE "
						+ "AND A.IDCONCEPTO = BO.IDCONCEPTO "
						+ "AND BO.IDBONIF_MULTA = bm.IDBONIF_MULTA");
		while(rs.next())
		{
			fineBonuses.add(new FineBonus(rs.getInt("IDCBTE"), rs.getInt("IDCONCEPTO"), 
					rs.getString("DESCRIPCION"), rs.getBigDecimal("IMPORTE"), rs.getShort("IMPRESION_AREA")));
		}
		rs.close();
		stmt.close();
		return fineBonuses;
	}
}
