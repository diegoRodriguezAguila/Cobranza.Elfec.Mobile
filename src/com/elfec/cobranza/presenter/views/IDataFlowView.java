package com.elfec.cobranza.presenter.views;

public interface IDataFlowView {

	/**
	 * Muestra al usuario actual
	 * @param username
	 */
	public void setCurrentUser(String username);
	/**
	 * Asigna el numero de caja del usuario
	 * @param cashdeskNumber
	 */
	public void setCashdeskNumber(int cashdeskNumber);
}
