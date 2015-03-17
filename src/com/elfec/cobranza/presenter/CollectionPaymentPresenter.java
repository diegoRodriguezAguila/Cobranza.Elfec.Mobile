package com.elfec.cobranza.presenter;

import java.util.List;

import com.elfec.cobranza.business_logic.CollectionManager;
import com.elfec.cobranza.business_logic.SupplyManager;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.views.ICollectionActionView;

public class CollectionPaymentPresenter extends CollectionActionPresenter {

	public CollectionPaymentPresenter(ICollectionActionView view) {
		super(view);
	}

	@Override
	public void loadSupplyReceipts(Supply supply) {
		currentSupply = supply;
		view.showReceipts(SupplyManager.getPendingReceipts(supply));
	}

	@Override
	public void processAction(final List<CoopReceipt> selectedReceipts) {
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				DataAccessResult<List<Long>> result = CollectionManager.savePayments(selectedReceipts);
				if(!result.hasErrors())
					view.informActionSuccessfullyFinished();
				view.showActionErrors(result.getErrors());
				loadSupplyReceipts(currentSupply);
			}
		});
		thread.start();
	}

}
