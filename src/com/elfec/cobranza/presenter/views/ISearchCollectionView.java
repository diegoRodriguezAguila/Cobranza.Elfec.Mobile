package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.Supply;

/**
 * Abstracci�n de la vista de busqueda de facturas
 * @author drodriguez
 *
 */
public interface ISearchCollectionView {

	/**
	 * Obtiene el nus
	 * @return
	 */
	public String getNUS();
	/**
	 * Obtiene el numero de cuenta sin guiones
	 * @return el numero de cuenta sin formato
	 */
	public String getAccountNumber();
	/**
	 * Notifica al usuario que debe al menos poner un t�rmino de b�squeda
	 */
	public void notifyAtleastOneField();
	/**
	 * Muestra al usuario el suministro encontrado
	 * @param foundSupply
	 */
	public void showFoundSupply(Supply foundSupply);
	/**
	 * Muestra los errores que podr�an haber ocurrido durante la b�squeda
	 * @param errors
	 */
	public void showSearchErrors(List<Exception> errors);
}
