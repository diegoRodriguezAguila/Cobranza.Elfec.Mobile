package com.elfec.cobranza.presenter.services;

import com.elfec.cobranza.R;
import com.elfec.cobranza.model.AnnulmentReason;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter.OnCollectionAnnulmentCallback;
import com.elfec.cobranza.presenter.views.ICollectionAnnulmentDialog;

public class CollectionAnnulmentDialogPresenter {

	private ICollectionAnnulmentDialog view;
	private OnCollectionAnnulmentCallback annulmentCallback;

	private CoopReceipt annulmentReceipt;
	
	public CollectionAnnulmentDialogPresenter(ICollectionAnnulmentDialog view, 
			OnCollectionAnnulmentCallback annulmentCallback, CoopReceipt annulmentReceipt) {
		this.view = view;
		this.annulmentCallback = annulmentCallback;
		this.annulmentReceipt = annulmentReceipt;
	}
	
	/**
	 * Carga los motivos de anulaci�n
	 */
	public void loadAnnulmentReasons()
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				view.setAnnulmentReasons(AnnulmentReason.getAll(AnnulmentReason.class));
			}
		}).start();
	}
	
	/**
	 * Carga la informaci�n de la factura
	 */
	public void loadReceiptInfo()
	{
		view.setPeriod(annulmentReceipt.getYear(), annulmentReceipt.getPeriodNumber());
		view.setReceiptNumber(""+annulmentReceipt.getReceiptNumber());
	}
	
	/**
	 * Verifica si se cumplieron todos los requisitos para la anulaci�n
	 * si es que no, muestra mensajes de error, en caso contrario procede con la anulaci�n
	 */
	public void verifyAnnulation()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean isAnnulmentReasonValid = validateAnnulmentReason();
				boolean isInternalControlCodeValid = validateInteralControlCode();
				if (isAnnulmentReasonValid && isInternalControlCodeValid) {
					view.closeView();
					if (annulmentCallback != null)
						annulmentCallback.collectionAnnuled(view.getSelectedAnnulmentReasonId());
				}
			}
		}).start();
	}
	
	/**
	 * Valida que se haya seleccionado el motivo de anulaci�n
	 * @return
	 */
	public boolean validateAnnulmentReason()
	{
		int annulmentReasonId = view.getSelectedAnnulmentReasonId();
		boolean isValid = (annulmentReasonId!=-1);
		view.setAnnulmentReasonError(isValid?-1:R.string.errors_in_fields);
		return isValid;
	}
	
	/**
	 * Valida el c�digo de control interno
	 * @return
	 */
	public boolean validateInteralControlCode() {
		String internalCode = view.getInternalControlCode();
		int errorMsg = R.string.error_empty_internal_control_code;
		if (!internalCode.isEmpty()) {
			if (internalCode.equals(annulmentReceipt
					.getActiveCollectionPayment().getId().toString())) 
				return true;
			errorMsg  = R.string.error_internal_control_code;
		}
		view.setInternalControlCodeError(errorMsg);
		return false;
	}
	
	/**
	 * Valida y verifica el motivo de anulaci�n seleccionado
	 */
	public void processSelectedAnnulmentReason()
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				int annulmentReasonId = view.getSelectedAnnulmentReasonId();
				view.setInternalControlCode(
						(annulmentReasonId==2 || annulmentReasonId==3)?
						annulmentReceipt.getActiveCollectionPayment().getId():-1);
				view.setInternalControlCodeError(-1);
				validateAnnulmentReason();
			}
		}).start();
	}
	
}
