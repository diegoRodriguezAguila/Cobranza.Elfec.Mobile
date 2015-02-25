package com.elfec.cobranza.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Es una clase que contiene el resultado de un acceso a datos adicionalmente de una 
 * lista de errores en caso de haber ocurrido alguno
 * @author Diego
 *
 * @param <TResult>
 */
public class DataAccessResult<TResult> {
	private TResult result;
	private List<Exception> listOfErrors;
	
	public DataAccessResult() {
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
	 * Asigna el resultado a un servicio web
	 * @param result
	 * @return
	 */
	public DataAccessResult<TResult> setResult(TResult result)
	{
		this.result = result;
		return this;
	}
	
	/**
	 * Obtiene el resultado de un acceso a datos
	 * @return Resultado del acceso a datos
	 */
	public TResult getResult()
	{
		return this.result;
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
