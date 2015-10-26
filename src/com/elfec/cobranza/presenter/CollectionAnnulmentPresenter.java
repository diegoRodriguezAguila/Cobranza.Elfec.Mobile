package com.elfec.cobranza.presenter;

import java.util.List;

import com.elfec.cobranza.business_logic.CollectionManager;
import com.elfec.cobranza.business_logic.SupplyManager;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.results.DataAccessResult;
import com.elfec.cobranza.presenter.views.ICollectionAnnulmentView;

public class CollectionAnnulmentPresenter extends CollectionActionPresenter{
	protected ICollectionAnnulmentView view;
	/**
	 * Interfaz que sirve para llamarse cuando una factura se anula
	 * @author drodriguez
	 *
	 */
	public interface OnCollectionAnnulmentCallback
	{
		public void collectionAnnuled(int annulmentReasonRemoteId);
	}
	
	public CollectionAnnulmentPresenter(ICollectionAnnulmentView view) {
		super(view);
		this.view = view;
	}

	@Override
	public void loadSupplyReceipts(Supply supply) {
		currentSupply = supply;
		view.showReceipts(SupplyManager.getTodayPaidReceipts(supply));
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
