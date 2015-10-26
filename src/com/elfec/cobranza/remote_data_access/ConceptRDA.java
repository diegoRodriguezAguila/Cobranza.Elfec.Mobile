package com.elfec.cobranza.remote_data_access;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.Concept;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * Provee una capa de acceso remoto a la base de datos oracle para
 *  operaciones relacionadas con <b>CONCEPTOS</b> 
 * @author drodriguez
 */
public class ConceptRDA {
	/**
	 * Obtiene las CONCEPTOS de oracle
	 * @param username
	 * @param password
	 * @return Lista de CONCEPTOS
	 * @throws ConnectException
	 * @throws SQLException
	 */
	public static List<Concept> requestConcepts(String username, String password) throws ConnectException, SQLException
	{
		List<Concept> concepts = new ArrayList<Concept>();
		ResultSet rs = OracleDatabaseConnector.instance(username, password).
				executeSelect("SELECT * FROM ERP_ELFEC.CONCEPTOS");
		while(rs.next())
		{
			concepts.add(new Concept(rs.getInt("IDCONCEPTO"), rs.getInt("IDSUBCONCEPTO"), rs.getInt("IDTIPO_SRV"), 
					rs.getString("DESCRIPCION"), rs.getString("CODCONCEPTO"), rs.getInt("IDGRUPO"), rs.getInt("IVA_TIPO_SRV"),
					rs.getInt("EXCLUIR_LIVA"), rs.getInt("IVA_COLUMNA"), rs.getShort("IMPRESION_AREA"), rs.getShort("TIPO_PART"), 
					rs.getShort("INCLUYA_UNIDADES")));
		}
		rs.close();
		return concepts;
	}
}
