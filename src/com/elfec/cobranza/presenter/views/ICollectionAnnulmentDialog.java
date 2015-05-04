package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.AnnulmentReason;

/**
 * Abstracci�n del dialogo de confirmaci�n de una anulaci�n
 * @author drodriguez
 *
 */
public interface ICollectionAnnulmentDialog {
	/**
	 * Asigna los motivos de anulaci�n que se mostrar�n
	 * @param annulmentReasons
	 */
	public void setAnnulmentReasons(List<AnnulmentReason> annulmentReasons);
	/**
	 * Obtiene el c�digo de control interno
	 * @return string, codigo de control interno
	 */
	public String getInternalControlCode();
	/**
	 * Asigna el c�digo de control interno
	 */
	public void setInternalControlCode(long internalControlCode);
	/**
	 * Muestra un error en el c�digo de control interno
	 * @param strErrorId
	 */
	public void setInternalControlCodeError(int strErrorId);
	/**
	 * Ordena a la interfaz de cerrar la vista actual
	 */
	public void closeView();
	/**
	 * Retorna el Id del motivo de anulaci�n seleccionado
	 * @return Id
	 */
	public int getSelectedAnnulmentReasonId();
	/**
	 * Muestra un error en el m�tivo de anulaci�n
	 * @param strErrorId
	 */
	public void setAnnulmentReasonError(int strErrorId);
	/**
	 * Asigna los datos del periodo de la factura
	 * @param year
	 * @param month
	 */
	public void setPeriod(int year, int month);
	/**
	 * Asigna los datos del n�mero de factura
	 * @param receiptNumber
	 */
	public void setReceiptNumber(String receiptNumber);
}
