package com.elfec.cobranza.presenter.views;

public interface IWipeAllDataDialog {
	/**
	 * Inicializa el dialogo de confirmación de eliminación de datos
	 */
	public void  initializeWipeConfirmDialog();
	/**
	 * Inicializa el dialogo de que no se puede realizar eliminación por cobros
	 */
	public void initializeCannotWipeDialog();
}
