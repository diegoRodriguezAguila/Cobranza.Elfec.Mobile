package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.business_logic.DataImporter.ImportSource;
import com.elfec.cobranza.model.Concept;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.printer.PrintConcept;
import com.elfec.cobranza.remote_data_access.ConceptRDA;

/**
 * Se encarga de las operaciones de negocio de <b>CONCEPTOS</b> 
 * @author drodriguez
 *
 */
public class ConceptManager {
	/**
	 * Importa los CONCEPTOS de oracle y los guarda
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataAccessResult<Boolean> importConcepts(final String username, final String password)
	{
		return DataImporter.importOnceRequiredData(new ImportSource<Concept>() {
			@Override
			public List<Concept> requestData() throws ConnectException, SQLException {
				return ConceptRDA.requestConcepts(username, password);
			}
		}); 
	}
	
	/**
	 * Obtiene los conceptos para imprimir, del TOTAL CONSUMO
	 * @param receiptId
	 * @return lista de conceptos de impresión
	 */
	public static List<PrintConcept> getAllTotalConsumeConcepts(int receiptId)
	{
		List<PrintConcept> concepts = new ArrayList<PrintConcept>();
		concepts.add(Concept.getTotalConsumeConcept(receiptId));
		concepts.addAll(Concept.getTotalConsumeAreaConcepts(receiptId));
		return concepts;
	}
	
	/**
	 * Obtiene los conceptos para imprimir, del TOTAL SUMINISTRO
	 * @param receiptId
	 * @return lista de conceptos de impresión
	 */
	public static List<PrintConcept> getAllTotalSupplyConcepts(int receiptId)
	{
		List<PrintConcept> concepts = new ArrayList<PrintConcept>();
		concepts.add(Concept.getTotalSupplyConcept(receiptId));
		concepts.addAll(Concept.getTotalSupplyAreaConcepts(receiptId));
		return concepts;
	}
	
	/**
	 * Obtiene los conceptos para imprimir, del TOTAL FACTURA
	 * @param receipt
	 * @return lista de conceptos de impresión
	 */
	public static List<PrintConcept> getAllTotalReceiptConcepts(CoopReceipt receipt)
	{
		List<PrintConcept> concepts = new ArrayList<PrintConcept>();
		concepts.add(Concept.getTotalReceiptConcept(receipt.getReceiptId()));
		concepts.add(Concept.getSubjectToTaxCreditConcept(receipt.getReceiptId()));
		concepts.add(new PrintConcept("TOTAL A PAGAR", receipt.getTotalAmount()));
		return concepts;
	}

}
