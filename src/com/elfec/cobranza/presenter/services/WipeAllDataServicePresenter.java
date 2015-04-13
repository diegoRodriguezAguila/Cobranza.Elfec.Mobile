package com.elfec.cobranza.presenter.services;

import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.presenter.views.IWipeAllDataDialog;

public class WipeAllDataServicePresenter {
	
	/**
	 * Evento que se ejecuta cuando se confirma la eliminación de todos los datos
	 * @author drodriguez
	 *
	 */
	public interface WipeConfirmationListener
	{
		/**
		 * 	Se ejecuta al confirmarse la eliminación de todos los datos
		 */
		public void onWipeConfirmed();
	}
	
	private IWipeAllDataDialog view;

	public WipeAllDataServicePresenter(IWipeAllDataDialog view) {
		this.view = view;
	}
	
	/**
	 * Define si se mostrará un dialogo de confirmación de eliminación 
	 * o uno de que no se pueden eliminar los datos
	 */
	public void defineDialogType()
	{
		if(CollectionPayment.getAll(CollectionPayment.class).size()>0)//hay cobros
			view.initializeCannotWipeDialog();
		else view.initializeWipeConfirmDialog();
	}
	
}
