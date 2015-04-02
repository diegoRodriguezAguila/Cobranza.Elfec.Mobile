package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.Statement;

import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.model.enums.ExportStatus;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para
 *  operaciones relacionadas con <b>COBRANZA.COBROS</b> 
 * @author drodriguez
 */
public class CollectionPaymentRDA {
	private static final String INSERT_QUERY = "INSERT INTO COBRANZA.COBROS VALUES (%d, %d, TO_DATE('%s', 'dd/mm/yyyy'), '%s', %d, %f, %d, %d, USER, "
			+ "TO_DATE('%s', 'dd/mm/yyyy hh24:mi:ss'), %s, %s, %d, %s, %d, %s, %d, %d, %d, '%s', 'F', %s, 1, TO_DATE('%s', 'dd/mm/yyyy hh24:mi:ss'))";
	
	/**
	 * Inserta remotamente un COBRANZA.COBROS de oracle
	 * @param username
	 * @param password
	 * @param collectionPayment COBRO a insertar
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static void insertCollectionPayment(String username, String password, CollectionPayment collectionPayment) throws ConnectException, SQLException
	{
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		int result = stmt.executeUpdate(String.format(INSERT_QUERY, collectionPayment.getId(), collectionPayment.getCashDeskNumber(),
				collectionPayment.getPaymentDate().toString("dd/MM/yyyy"), collectionPayment.getUser(), collectionPayment.getReceiptId(), 
				collectionPayment.getAmount().doubleValue(), collectionPayment.getId(), collectionPayment.getStatus(), 
				collectionPayment.getPaymentDate().toString("dd/MM/yyyy hh:mm:ss"), 
				(collectionPayment.getAnnulmentUser()==null?"NULL":"'"+collectionPayment.getAnnulmentUser()+"'"),
				(collectionPayment.getAnnulmentDate()==null?"NULL":String.format("TO_DATE('%s', 'dd/mm/yyyy hh24:mi:ss')", 
						collectionPayment.getAnnulmentDate().toString("dd/MM/yyyy hh:mm:ss"))),
				collectionPayment.getTransactionNumber(), 
				(collectionPayment.getAnnulmentTransacNum()==0?"NULL":""+collectionPayment.getAnnulmentTransacNum()),
				collectionPayment.getSupplyId(), collectionPayment.getSupplyNumber(), collectionPayment.getReceiptNumber(),
				collectionPayment.getYear(), collectionPayment.getPeriodNumber(), collectionPayment.getCashDeskDescription(),
				collectionPayment.getAnnulmentReasonId()==null?"NULL":collectionPayment.getAnnulmentReasonId().toString(),
				collectionPayment.getPaymentDate().toString("dd/MM/yyyy hh:mm:ss")));
		if(result==1) //se insertó existosamente
		{
			collectionPayment.setExportStatus(ExportStatus.EXPORTED);
			collectionPayment.save();
		}
		stmt.close();
	}
}
