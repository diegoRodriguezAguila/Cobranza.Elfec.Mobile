package com.elfec.cobranza.presenter.views;

import java.util.List;

import com.elfec.cobranza.model.AnnulmentReason;

/**
 * Abstracción del dialogo de confirmación de una anulación
 * @author drodriguez
 *
 */
public interface ICollectionAnnulmentDialog {
	/**
	 * Asigna los motivos de anulación que se mostrarán
	 * @param annulmentReasons
	 */
	public void setAnnulmentReasons(List<AnnulmentReason> annulmentReasons);
	/**
	 * Obtiene el código de control interno
	 * @return string, codigo de control interno
	 */
	public String getInternalControlCode();
	/**
	 * Asigna el código de control interno
	 */
	public void setInternalControlCode(long internalControlCode);
	/**
	 * Muestra un error en el código de control interno
	 * @param strErrorId
	 */
	public void setInternalControlCodeError(int strErrorId);
	/**
	 * Ordena a la interfaz de cerrar la vista actual
	 */
	public void closeView();
	/**
	 * Retorna el Id del motivo de anulación seleccionado
	 * @return Id
	 */
	public int getSelectedAnnulmentReasonId();
	/**
	 * Muestra un error en el mótivo de anulación
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
	 * Asigna los datos del número de factura
	 * @param receiptNumber
	 */
	public void setReceiptNumber(String receiptNumber);
}
