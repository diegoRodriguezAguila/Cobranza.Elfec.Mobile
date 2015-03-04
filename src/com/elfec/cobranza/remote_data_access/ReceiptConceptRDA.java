package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.helpers.text_format.ObjectListToSQL;
import com.elfec.cobranza.helpers.text_format.ObjectListToSQL.AttributePicker;
import com.elfec.cobranza.model.ReceiptConcept;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para operaciones relacionadas con CBTES_CPTOS
 * @author drodriguez
 *
 */
public class ReceiptConceptRDA {

	/**
	 * Obtiene los CBTES_CPTOS de Oracle
	 * @param username
	 * @param password
	 * @param routes la lista de rutas en formato (12334,1234)
	 * @return
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<ReceiptConcept> requestReceiptConcepts(String username, String password, String routes) throws ConnectException, SQLException
	{
		List<ReceiptConcept> receiptConcepts = new ArrayList<ReceiptConcept>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT * FROM ERP_ELFEC.SUMIN_ESTADOS WHERE IDRUTA IN "+routes);
		while(rs.next())
		{
			receiptConcepts.add(new ReceiptConcept(rs.getInt("IDEMPRESA"), rs.getInt("IDSUCURSAL"), rs.getString("TIPO_CBTE"), 
					rs.getInt("GRUPO_CBTE"), rs.getString("LETRA_CBTE"), rs.getInt("NROCBTE"), rs.getInt("IDMEDIDOR"), 
					rs.getInt("IDCONCEPTO"), rs.getInt("IDSUBCONCEPTO"), rs.getBigDecimal("IMPORTE"), rs.getInt("ANIO"), rs.getInt("NROPER"), 
					rs.getInt("LECTURA_ANTERIOR"), rs.getInt("LECTURA_ACTUAL"), rs.getString("DESCRIPCION"), rs.getString("IDCATEGORIA"), rs.getInt("IDCBTE")));
		}
		return receiptConcepts;
	}
	
	/**
	 * Obtiene los CBTES_CPTOS de Oracle
	 * @param username
	 * @param password
	 * @param routes la lista de rutas
	 * @return
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<ReceiptConcept> requestReceiptConcepts(String username, String password, List<Route> routes) throws ConnectException, SQLException
	{
		return requestReceiptConcepts(username, password, ObjectListToSQL.convertToSQL(routes, new AttributePicker<Route>() {
			@Override
			public String pickString(Route route) {
				return ""+route.getRouteRemoteId();
			}}));
	}
}
