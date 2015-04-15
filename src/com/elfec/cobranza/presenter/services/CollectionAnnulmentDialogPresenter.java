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
	 * Carga los motivos de anulación
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
	 * Carga la información de la factura
	 */
	public void loadReceiptInfo()
	{
		view.setPeriod(annulmentReceipt.getYear(), annulmentReceipt.getPeriodNumber());
		view.setReceiptNumber(""+annulmentReceipt.getReceiptNumber());
	}
	
	/**
	 * Verifica si se cumplieron todos los requisitos para la anulación
	 * si es que no, muestra mensajes de error, en caso contrario procede con la anulación
	 */
	public void verifyAnnulation()
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				String internalCode = view.getInternalControlCode();
	        	  if(!internalCode.isEmpty())
	        	  {
		        	  if(internalCode.equals(annulmentReceipt.getActiveCollectionPayment().getId().toString()))
						{
							view.closeView();
							if(annulmentCallback!=null)
								annulmentCallback.collectionAnnuled(view.getSelectedAnnulmentReasonId());					
						}
						else view.setInternalControlCodeError(R.string.error_internal_control_code);;
					}
	        	  else view.setInternalControlCodeError(R.string.error_empty_internal_control_code);
			}
		}).start();
	}
	
	/**
	 * Valida y verifica el motivo de anulación seleccionado
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
			}
		}).start();
	}
	
}
