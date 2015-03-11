package com.elfec.cobranza.presenter;

import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.business_logic.SupplyManager;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.exceptions.SupplyNotFoundException;
import com.elfec.cobranza.presenter.views.ISearchCollectionView;

public class SearchCollectionPresenter {

	private ISearchCollectionView view;
	
	public SearchCollectionPresenter(ISearchCollectionView view)
	{
		this.view = view;
	}
	
	/**
	 * Busca un suministro para obtener sus facturas
	 */
	public void searchSupply()
	{
		String nus = view.getNUS();
		String accountNumber = view.getAccountNumber();
		if(!nus.isEmpty() || !accountNumber.isEmpty())
		{
			try {
				Supply suplly = SupplyManager.getSupplyByNUSOrAccount(nus, accountNumber);
				view.showFoundSupply(suplly);
			} catch (SupplyNotFoundException e) {
				List<Exception> errors = new ArrayList<Exception>();
				errors.add(e);
				view.showSearchErrors(errors);
			}
		}
		else view.notifyAtleastOneField();
	}
}
