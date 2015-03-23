package com.elfec.cobranza.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el resultado de realizar una acción en un gestionador de la 
 * capa de lógica de negocio u otra que lo requiera
 * @author drodriguez
 *
 */
public class ManagerProcessResult {
	protected List<Exception> listOfErrors;
	
	public ManagerProcessResult() {
		listOfErrors = new ArrayList<Exception>();
	}
	
	/**
	 * Obtiene la lista de errores del resultado de un servicio web
	 * @return Lista de errores del WS
	 */
	public List<Exception> getErrors()
	{
		return listOfErrors;
	}
	/**
	 * Agrega un error a la lista de errores del resultado de un servicio web
	 * @param e
	 */
	public void addError(Exception e)
	{
		listOfErrors.add(e);
	}
	/**
	 * Agrega multiples errores a la lita de errores
	 * @param errors
	 */
	public void addErrors(List<Exception> errors)
	{
		for (int i = 0; i < errors.size(); i++) {
			addError(errors.get(i));
		}
	}
	/**
	 * Si es que el resultado del acceso a datos tuvo errores
	 * @return true si los hubo
	 */
	public boolean hasErrors()
	{
		return listOfErrors.size() > 0;
	}
}
