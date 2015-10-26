package com.elfec.cobranza.model.results;

import java.util.ArrayList;


/**
 * Es una clase que contiene el resultado de un acceso a datos adicionalmente de una 
 * lista de errores en caso de haber ocurrido alguno
 * @author Diego
 *
 * @param <TResult>
 */
public class DataAccessResult<TResult> extends ManagerProcessResult {
	private TResult result;
	private boolean isRemoteDataAccess;
	
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
