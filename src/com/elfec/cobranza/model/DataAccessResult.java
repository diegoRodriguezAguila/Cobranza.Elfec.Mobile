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
	private boolean isRemoteDataAccess;
	private List<Exception> listOfErrors;
	
	public DataAccessResult() {
		listOfErrors = new ArrayList<Exception>();
	}
	
	public DataAccessResult(boolean isRemoteDataAccess) {
		this.isRemoteDataAccess = isRemoteDataAccess;
		listOfErrors = new ArrayList<Exception>();
	}
	
	public DataAccessResult(TResult result)
	{
		this.result = result;
		listOfErrors = new ArrayList<Exception>();
	}
	
	public DataAccessResult(boolean isRemoteDataAccess, TResult result) {
		this.isRemoteDataAccess = isRemoteDataAccess;
		this.result = result;
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

	/**
	 * Inidica si el resultado es de un acceso a datos remoto o no
	 * @return
	 */
	public boolean isRemoteDataAccess() {
		return isRemoteDataAccess;
	}

	public void setRemoteDataAccess(boolean isRemoteDataAccess) {
		this.isRemoteDataAccess = isRemoteDataAccess;
	}
}
