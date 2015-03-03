package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.helpers.text_format.RouteListToSQL;
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
				executeSelect("SELECT * FROM ERP_ELFEC.CBTES_COOP WHERE IDEMPRESA=1 AND TIPO_CBTE='FC' "
						+ "AND LETRA_CBTE='Y' AND SRV_SALDO>0 AND ESTADO='A' "
						+ "AND IDRUTA IN "+routes);
		while(rs.next())
		{
			CoopReceipt receipt = new CoopReceipt(rs.getInt("IDCBTE"), rs.getInt("IDSUMINISTRO"), rs.getInt("IDCLIENTE"), 
					rs.getInt("IDEMPRESA"), rs.getInt("IDSUCURSAL"), rs.getString("TIPO_CBTE"), rs.getInt("GRUPO_CBTE"), 
					rs.getString("LETRA_CBTE"), rs.getInt("NROCBTE"), new DateTime(rs.getDate("FECHA_EMISION")), 
					new DateTime(rs.getDate("FECHA_VTO_ORIGINAL")), new DateTime(rs.getDate("FECHA_VTO")), 
					new DateTime(rs.getDate("FECHA_INICIO")), new DateTime(rs.getDate("FECHA_FIN")), rs.getInt("ANIO"), 
					rs.getInt("NROPER"), rs.getInt("NROSUM"), rs.getInt("IDRUTA"), rs.getString("NOMBRE"), rs.getInt("IDIVA"), 
					rs.getString("CUIT"), rs.getString("DOMICILIO_SUM"),  rs.getString("IDCATEGORIA"), rs.getBigDecimal("SRV_IMPORTE"), 
					rs.getBigDecimal("SRV_SALDO"), rs.getBigDecimal("TOTALIMP"), rs.getString("ESTADO"), rs.getInt("IDLOTE") ,
					rs.getString("NRO_AUT_IMPRESION"), new DateTime(rs.getDate("FECHA_VTO_AUT")), rs.getString("COD_CONTROL"));
			
			requestCoopReceiptLiteral(username, password, receipt);
			requestCoopReceiptClientAddress(username, password, receipt);
			requestCoopReceiptMeterNumber(username, password, receipt);
			requestCoopReceiptAuthDesc(username, password, receipt);
			coopReceipts.add(receipt);
		}
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
		return requestCoopReceipts(username, password, RouteListToSQL.convertToSQL(routes));
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
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT COBRANZA.FLITERAL("+receipt.getTotalAmount()+") AS LITERAL FROM DUAL");
		while(rs.next())
		{
			receipt.setLiteral(rs.getString("LITERAL"));
		}
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
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT MOVILES.FCOBRA_OBTENER_DIRECCION("+receipt.getSupplyId()+") AS DIRECCION FROM DUAL");
		while(rs.next())
		{
			receipt.setClientAddress(rs.getString("DIRECCION"));
		}
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
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT MOVILES.FCOBRA_OBTENER_MEDIDOR("+receipt.getReceiptId()+") AS MEDIDOR FROM DUAL");
		while(rs.next())
		{
			receipt.setMeterNumber(rs.getString("MEDIDOR"));
		}
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
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT MOVILES.FCOBRA_OBTENER_DESC_AUT("+receipt.getAuthorizationNumber()+") AS DESC_AUT_IMPRESION FROM DUAL");
		while(rs.next())
		{
			receipt.setMeterNumber(rs.getString("DESC_AUT_IMPRESION"));
		}
		return receipt;
	}
}
