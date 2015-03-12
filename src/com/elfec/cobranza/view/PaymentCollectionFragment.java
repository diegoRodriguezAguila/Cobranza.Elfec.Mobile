package com.elfec.cobranza.view;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.AccountFormatter;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.PaymentCollectionPresenter;
import com.elfec.cobranza.presenter.views.IPaymentCollectionView;
import com.elfec.cobranza.view.adapters.ReceiptAdapter;
/**
 * Fragment que representa solamente la pantalla de pago de un cobro 
 * @author drodriguez
 *
 */
public class PaymentCollectionFragment extends Fragment implements IPaymentCollectionView {
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PaymentCollectionFragment() {
	}
	
	private PaymentCollectionPresenter presenter;
	
	//Layout parts
	private RelativeLayout layoutSupplyInfo;
	private TextView txtNoSuppliesFound;
	private LinearLayout layoutWaitingSearch;
	
	//Supply info
	private TextView txtClientName;
	private TextView txtNus;
	private TextView txtAccountNumber;
	private TextView txtClientAddress;
	
	//Receipts
	private ListView listPendingReceipts;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PaymentCollectionPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_collection, container, false);
        layoutSupplyInfo = (RelativeLayout) view.findViewById(R.id.layout_supply_info);
        txtNoSuppliesFound = (TextView) view.findViewById(R.id.txt_no_supplies_found);
        layoutWaitingSearch = (LinearLayout) view.findViewById(R.id.layout_waiting_search);
        
        txtClientName = (TextView) view.findViewById(R.id.txt_client_name);
        txtNus = (TextView) view.findViewById(R.id.txt_nus);
        txtAccountNumber = (TextView) view.findViewById(R.id.txt_account_number);
        txtClientAddress = (TextView) view.findViewById(R.id.txt_client_address);
        
        listPendingReceipts = (ListView) view.findViewById(R.id.list_pending_receipts);
        return view;
    }

    //#region Interface Methods
    
	@Override
	public PaymentCollectionPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void showSearchingMessage() {	
		layoutWaitingSearch.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideSearchingMessage() {
		layoutWaitingSearch.setVisibility(View.GONE);
	}

	@Override
	public void showSearchErrors(List<Exception> errors) {
		txtNoSuppliesFound.setText(MessageListFormatter.fotmatHTMLFromErrors(errors));
		txtNoSuppliesFound.setVisibility(View.VISIBLE);
	}

	@Override
	public void showSupplyInfo(final Supply supply) {
		getActivity().runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				txtClientName.setText(supply.getClientName());
				txtNus.setText(""+supply.getSupplyId());
				txtAccountNumber.setText(AccountFormatter.formatAccountNumber(supply.getSupplyNumber()));
				txtClientAddress.setText(supply.getClientAddress());
				layoutSupplyInfo.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void hideNoSearchedSupplies() {
		txtNoSuppliesFound.setVisibility(View.GONE);
	}

	@Override
	public void showReceipts(final List<CoopReceipt> receipts) {
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				listPendingReceipts.setAdapter(new ReceiptAdapter(getActivity(), R.layout.receipt_list_item, receipts));
				hideSearchingMessage();
				if(layoutSupplyInfo.getVisibility()==View.GONE)
					layoutSupplyInfo.setVisibility(View.VISIBLE);
			}
		});
	}
	
	//#endregion
}
