package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.helpers.text_format.AttributePicker;
import com.elfec.cobranza.helpers.text_format.ObjectListToSQL;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para operaciones relacionadas con <B>CBTES_COOP</B>
 * @author drodriguez
 *
 */
public class CoopReceiptRDA {

	/**
	 * Obtiene los CBTES_COOP de Oracle
	 * @param username
	 * @param password
	 * @param routes la lista de rutas en formato (12334,1234)
	 * @return
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<CoopReceipt> requestCoopReceipts(String username, String password, String routes) throws ConnectException, SQLException
	{
		List<CoopReceipt> coopReceipts = new ArrayList<CoopReceipt>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT /*+CHOOSE*/  IDCBTE, IDSUMINISTRO, IDCLIENTE, IDEMPRESA, IDSUCURSAL, TIPO_CBTE, "
						+ "GRUPO_CBTE, LETRA_CBTE, NROCBTE, FECHA_EMISION, FECHA_VTO_ORIGINAL, FECHA_VTO, FECHA_INICIO, "
						+ "FECHA_FIN, ANIO, NROPER, NROSUM, IDRUTA, NOMBRE, IDIVA, CUIT, DOMICILIO_SUM, IDCATEGORIA, "
						+ "SRV_IMPORTE, SRV_SALDO, TOTALIMP, ESTADO, IDLOTE, NRO_AUT_IMPRESION, FECHA_VTO_AUT, COD_CONTROL, "
						+ "COBRANZA.FLITERAL(TOTALIMP) AS LITERAL, "
						+ "MOVILES.FCOBRA_OBTENER_DIRECCION(IDSUMINISTRO) AS DIRECCION, "
						+ "MOVILES.FCOBRA_OBTENER_MEDIDOR(IDCBTE) AS MEDIDOR, "
						+ "MOVILES.FCOBRA_OBTENER_DESC_AUT(NRO_AUT_IMPRESION) AS DESC_AUT_IMPRESION "
						+ "FROM ERP_ELFEC.CBTES_COOP WHERE IDEMPRESA=1 AND TIPO_CBTE='FC' "
						+ "AND LETRA_CBTE='Y' AND SRV_SALDO>0 AND ESTADO='A' "
						+ "AND IDSUMINISTRO IN (SELECT/*+CHOOSE*/ IDSUMINISTRO FROM ERP_ELFEC.SUMINISTROS WHERE IDRUTA IN "+routes+")");
		while(rs.next())
		{
			CoopReceipt receipt = new CoopReceipt(rs.getInt("IDCBTE"), rs.getInt("IDSUMINISTRO"), rs.getInt("IDCLIENTE"), 
					rs.getInt("IDEMPRESA"), rs.getInt("IDSUCURSAL"), rs.getString("TIPO_CBTE"), rs.getInt("GRUPO_CBTE"), 
					rs.getString("LETRA_CBTE"), rs.getInt("NROCBTE"), new DateTime(rs.getDate("FECHA_EMISION")), 
					new DateTime(rs.getDate("FECHA_VTO_ORIGINAL")), new DateTime(rs.getDate("FECHA_VTO")), 
					new DateTime(rs.getDate("FECHA_INICIO")), new DateTime(rs.getDate("FECHA_FIN")), rs.getInt("ANIO"), 
					rs.getInt("NROPER"), rs.getString("NROSUM"), rs.getInt("IDRUTA"), rs.getString("NOMBRE"), rs.getInt("IDIVA"), 
					rs.getString("CUIT"), rs.getString("DOMICILIO_SUM"),  rs.getString("IDCATEGORIA"), rs.getBigDecimal("SRV_IMPORTE"), 
					rs.getBigDecimal("SRV_SALDO"), rs.getBigDecimal("TOTALIMP"), rs.getString("ESTADO"), rs.getInt("IDLOTE") ,
					rs.getString("NRO_AUT_IMPRESION"), new DateTime(rs.getDate("FECHA_VTO_AUT")), rs.getString("COD_CONTROL"));
			receipt.setLiteral(rs.getString("LITERAL"));
			receipt.setClientAddress(rs.getString("DIRECCION"));
			receipt.setMeterNumber(rs.getString("MEDIDOR"));
			receipt.setAuthorizationDescription(rs.getString("DESC_AUT_IMPRESION"));
			coopReceipts.add(receipt);
		}
		rs.close();
		return coopReceipts;
	}
	
	/**
	 * Obtiene los CBTES_COOP de Oracle
	 * @param username
	 * @param password
	 * @param routes la lista de rutas
	 * @return
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<CoopReceipt> requestCoopReceipts(String username, String password, List<Route> routes) throws ConnectException, SQLException
	{
		return requestCoopReceipts(username, password, ObjectListToSQL.convertToSQL(routes, new AttributePicker<String, Route>() {
			@Override
			public String pickAttribute(Route route) {
				return ""+route.getRouteRemoteId();
			}}));
	}
	
	/**
	 * Obtiene el literal del monto total de la factura
	 * @param username
	 * @param password
	 * @param receipt
	 * @return la factura con su literal ya asignado
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static CoopReceipt requestCoopReceiptLiteral(String username, String password, CoopReceipt receipt) throws ConnectException, SQLException
	{
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		ResultSet rs = stmt.executeQuery("SELECT COBRANZA.FLITERAL("+receipt.getTotalAmount()+") AS LITERAL FROM DUAL");
		while(rs.next())
		{
			receipt.setLiteral(rs.getString("LITERAL"));
		}
		rs.close();
		stmt.close();
		return receipt;
	}
	/**
	 * Obtiene la dirección del cliente de la factura
	 * @param username
	 * @param password
	 * @param receipt
	 * @return la factura con la dirección del cliente asignada
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static CoopReceipt requestCoopReceiptClientAddress(String username, String password, CoopReceipt receipt) throws ConnectException, SQLException
	{
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		ResultSet rs = stmt.executeQuery("SELECT MOVILES.FCOBRA_OBTENER_DIRECCION("+receipt.getSupplyId()+") AS DIRECCION FROM DUAL");
		while(rs.next())
		{
			receipt.setClientAddress(rs.getString("DIRECCION"));
		}
		rs.close();
		stmt.close();
		return receipt;
	}
	
	/**
	 * Obtiene el numero de medidor de la factura
	 * @param username
	 * @param password
	 * @param receipt
	 * @return la factura con el numero de medidor asignado
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static CoopReceipt requestCoopReceiptMeterNumber(String username, String password, CoopReceipt receipt) throws ConnectException, SQLException
	{
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		ResultSet rs =  stmt.executeQuery("SELECT MOVILES.FCOBRA_OBTENER_MEDIDOR("+receipt.getReceiptId()+") AS MEDIDOR FROM DUAL");
		while(rs.next())
		{
			receipt.setMeterNumber(rs.getString("MEDIDOR"));
		}
		rs.close();
		stmt.close();
		return receipt;
	}
	
	/**
	 * Obtiene la descripción de la autorización de la factura
	 * @param username
	 * @param password
	 * @param receipt
	 * @return la factura con la descripción de la autorización asignada
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static CoopReceipt requestCoopReceiptAuthDesc(String username, String password, CoopReceipt receipt) throws ConnectException, SQLException
	{
		Statement stmt = OracleDatabaseConnector.instance(username, password).getNewQuerier();
		ResultSet rs =  stmt.executeQuery("SELECT MOVILES.FCOBRA_OBTENER_DESC_AUT("+receipt.getAuthorizationNumber()+") AS DESC_AUT_IMPRESION FROM DUAL");
		while(rs.next())
		{
			receipt.setMeterNumber(rs.getString("DESC_AUT_IMPRESION"));
		}
		rs.close();
		stmt.close();
		return receipt;
	}
}
