package com.elfec.cobranza.model.printer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.elfec.cobranza.model.Concept;
import com.elfec.cobranza.model.ConceptCalculationBase;
import com.elfec.cobranza.model.PrintCalculationBase;
import com.elfec.cobranza.model.ReceiptConcept;

/**
 * Ayuda a obtener las queries para la obtención de conceptos de impresión
 * @author drodriguez
 *
 */
public class PrintConceptQuerier {
	private boolean isBuilt;
	private String mainQuery;
	private From subQuery;
	private String description = "";
	private String groupBy = "";
	private int[] printAreas;
	
	public PrintConceptQuerier(int receiptId, int... printAreas)
	{
		this.printAreas = printAreas;
		String query = "SELECT %1$s Description, ifnull(SUM(Amount), 0) Amount FROM (";
		String groupBy = ") %2$s";
		subQuery = new Select("A.ReceiptId,A.EnterpriseId, A.BranchOfficeId, A.ReceiptType, A.ReceiptGroup, A.ReceiptLetter,"
				+ "A.ReceiptNumber, A.ConceptId, A.Description Desc, A.Amount, B.PrintArea, C.ClaculationBaseId, D.Description")
	    .from(ReceiptConcept.class).as("A")
	    .join(Concept.class).as("B")
	    .on("(A.ConceptId = B.ConceptId AND A.SubconceptId = B.SubconceptId)")
	    .join(ConceptCalculationBase.class).as("C")
	    .on("(A.ConceptId = C.ConceptId AND A.SubconceptId = C.SubconceptId)")
	    .join(PrintCalculationBase.class).as("D")
	    .on(" C.ClaculationBaseId = D.ClaculationBaseId")
	    .where(" (A.EnterpriseId = 1) AND (A.BranchOfficeId = 10) "
	    		+ "AND (A.ReceiptType = 'FC') AND (A.ReceiptLetter = 'Y') "
	    		+ "AND (B.PrintArea IN (%3$s)) "
	    		+ "AND A.ReceiptId = ? "
	    		+ "AND C.ClaculationBaseId>=100 "
	    		+ "AND A.Amount<>0", receiptId);
		mainQuery = query+subQuery.toSql()+groupBy;
	}
	
	/**
	 * Le asigna una descripción al texto del select
	 * @param description
	 * @return la instancia de este querier
	 */
	public PrintConceptQuerier setDescription(String description)
	{
		this.description = "'"+description+"'";
		return this;
	}
	
	/**
	 * Asigna el group by de la consulta, si se quiere multiples campos se deben poner separados por comas
	 * @param groupBy en formato: COL1, COL2,...
	 * @return
	 */
	public PrintConceptQuerier setGroupBy(String groupBy)
	{
		this.groupBy = this.groupBy+" GROUP BY "+groupBy;
		return this;
	}
	
	/**
	 * Construye la consulta lista para ejecutarla
	 * @return la instancia de este querier
	 */
	public PrintConceptQuerier build()
	{
		mainQuery = String.format(mainQuery, description, groupBy, getPrintAreasString());
		isBuilt = true;
		return this;
	}
	
	/**
	 * Convierte las areas de impresión en una cadena
	 * @return
	 */
	private String getPrintAreasString()
	{
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < printAreas.length; i++) {
			str.append(printAreas[i]);
			if(i<printAreas.length-1)
				str.append(",");
		}
		return str.toString();
	}
	
	
	/**
	 * Ejecuta la query preparada, si no se llamó a build(), lo llama
	 * @return lista de conceptos de impresión
	 */
	public List<PrintConcept> execute()
	{
		if(!isBuilt)
			build();
		List<PrintConcept> concepts = new ArrayList<PrintConcept>();
		Cursor cursor = Cache.openDatabase().rawQuery(mainQuery, subQuery.getArguments());
		if(cursor!=null && cursor.moveToFirst())
		{
			do{ 	
				concepts.add(new PrintConcept(cursor.getString(cursor.getColumnIndex("Description")), 
						new BigDecimal(cursor.getString(cursor.getColumnIndex("Amount")))));			
			} while(cursor.moveToNext());
		}
		return concepts;
	}
	
	@Override
	public String toString()
	{
		return String.format(mainQuery, description, groupBy, getPrintAreasString());
	}
}
