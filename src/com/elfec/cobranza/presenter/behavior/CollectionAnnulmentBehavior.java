package com.elfec.cobranza.presenter.behavior;

import java.util.List;

import com.elfec.cobranza.business_logic.SupplyManager;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;

public class CollectionAnnulmentBehavior implements ICollectionBehavior {

	@Override
	public List<CoopReceipt> getSupplyReceipts(Supply supply) {
		return SupplyManager.getPaidReceipts(supply);
	}

}
