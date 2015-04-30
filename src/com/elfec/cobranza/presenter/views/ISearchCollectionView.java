package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.events.SupplyResultPickedListener;

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
	 * Obtiene el nombre del cliente
	 * @return
	 */
	public String getClientName();
	/**
	 * Obtiene el NIT del cliente
	 * @return
	 */
	public String getNIT();
	/**
	 * Notifica al usuario que debe al menos poner un t�rmino de b�squeda
	 */
	public void notifyAtleastOneField();
	/**
	 * Notifica a la vista que se inici� la b�squeda
	 */
	public void notifySearchStarted();
	/**
	 * Pide al usuario que seleccione un resultado de los obtenidos
	 * @param foundSupplies
	 * @param supplyResultPickedListener
	 */
	public void pickSupplyResult(List<Supply> foundSupplies, SupplyResultPickedListener supplyResultPickedListener);
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
	/**
	 * Oculta el mensaje de espera
	 */
	public void cancelSearch();
}
