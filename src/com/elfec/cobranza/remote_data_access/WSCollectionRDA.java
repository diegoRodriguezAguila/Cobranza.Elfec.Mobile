package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.Statement;

import com.elfec.cobranza.model.WSCollection;
import com.elfec.cobranza.model.enums.ExportStatus;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para
 *  operaciones relacionadas con <b>ERP_ELFEC.COB_WS</b> 
 * @author drodriguez
 */
public class WSCollectionRDA {
	
	private static final String INSERT_QUERY = "INSERT INTO ERP_ELFEC.COB_WS VALUES (%d, %d, '%s', %d, '%s', %d, %d, %d, "
			+ "NULL, NULL, NULL, NULL, TO_DATE('%s', 'dd/mm/yyyy'))";
	
	/**
	 * Inserta remotamente un ERP_ELFEC.COB_WS de oracle
	 * @param username
	 * @param password
	 * @param wSCollection COB_WS a insertar
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static void insertWSCollection(String username, String password, WSCollection wSCollection) throws ConnectException, SQLException
	{
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		int result = stmt.executeUpdate(String.format(INSERT_QUERY, wSCollection.getId(), wSCollection.getId(),
				wSCollection.getAction(), wSCollection.getReceiptId(), wSCollection.getStatus(), 
				wSCollection.getBankId(), wSCollection.getBankAccountId(), wSCollection.getPeriodNumber(),
				wSCollection.getPaymentDate().toString("dd/MM/yyyy")));
		if(result==1) //se insertó existosamente
		{
			wSCollection.setExportStatus(ExportStatus.EXPORTED);
			wSCollection.save();
		}
		stmt.close();
	}
}
