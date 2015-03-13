package com.elfec.cobranza.presenter;

import com.elfec.cobranza.business_logic.SupplyManager;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.views.IPaymentCollectionView;

public class PaymentCollectionPresenter {
	
	private IPaymentCollectionView view;

	public PaymentCollectionPresenter(IPaymentCollectionView view) {
		this.view = view;
	}
	/**
	 * Carga las facturas del suministro
	 * @param supply
	 */
	public void loadSupplyReceipts(Supply supply)
	{		
		view.showReceipts(SupplyManager.getSupplyReceiptsWithStatus(supply));
	}
	
}
