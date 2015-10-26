package com.elfec.cobranza.presenter;

import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.business_logic.SupplyManager;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.events.SupplyResultPickedListener;
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
		final String nus = view.getNUS();
		final String accountNumber = view.getAccountNumber();
		final String clientName = view.getClientName();
		final String nit = view.getNIT();
		if(!nus.isEmpty() || !accountNumber.isEmpty() || !clientName.isEmpty() || !nit.isEmpty())
		{
			new Thread(new Runnable() {				
				@Override
				public void run() {
					view.notifySearchStarted();
					try {
						List<Supply> suppliesFound = SupplyManager.searchSupply(nus, accountNumber, clientName, nit);
						if(suppliesFound.size()==1)
							view.showFoundSupply(suppliesFound.get(0));
						else
						{
							view.pickSupplyResult(suppliesFound, new SupplyResultPickedListener() {								
								@Override
								public void onSupplyResultPicked(Supply supply) {
									view.showFoundSupply(supply);
								}							
								@Override
								public void onSupplyResultPickCanceled() {
									view.cancelSearch();
								}
							});
						}
					} catch (SupplyNotFoundException e) {
						List<Exception> errors = new ArrayList<Exception>();
						errors.add(e);
						view.showSearchErrors(errors);
					}
				}
			}).start();			
		}
		else view.notifyAtleastOneField();
	}
}
