package com.elfec.cobranza.presenter;

import java.util.List;

import com.elfec.cobranza.business_logic.CollectionManager;
import com.elfec.cobranza.business_logic.SupplyManager;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.views.ICollectionActionView;

public class CollectionAnnulmentPresenter extends CollectionActionPresenter{
	/**
	 * Interfaz que sirve para llamarse cuando una factura se anula
	 * @author drodriguez
	 *
	 */
	public interface OnCollectionAnnulmentCallback
	{
		public void collectionAnnuled(int annulmentReasonRemoteId);
	}
	
	public CollectionAnnulmentPresenter(ICollectionActionView view) {
		super(view);
	}

	@Override
	public void loadSupplyReceipts(Supply supply) {
		currentSupply = supply;
		view.showReceipts(SupplyManager.getPaidReceipts(supply));
	}

	@Override
	public void processAction(final List<CoopReceipt> selectedReceipts) {
		view.showAnnulmentConfirmation(selectedReceipts, new OnCollectionAnnulmentCallback() {			
			@Override
			public void collectionAnnuled(final int annulmentReasonRemoteId) {
				new Thread(new Runnable() {			
					@Override
					public void run() {
						DataAccessResult<Void> result = CollectionManager.annulateCollections(selectedReceipts, annulmentReasonRemoteId);
						if(!result.hasErrors())
							view.informActionSuccessfullyFinished();
						view.showActionErrors(result.getErrors());
						loadSupplyReceipts(currentSupply);
					}
				}).start();
			}
		});
	}

}
