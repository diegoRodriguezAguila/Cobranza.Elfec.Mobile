package com.elfec.cobranza.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.AccountFormatter;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.CollectionActionPresenter;
import com.elfec.cobranza.presenter.views.ICollectionActionView;
import com.elfec.cobranza.view.adapters.ReceiptAdapter;
import com.elfec.cobranza.view.adapters.collection.CollectionBaseAdapter;
/**
 * Fragment que representa solamente la pantalla de pago de un cobro 
 * @author drodriguez
 *
 */
public class CollectionActionFragment extends Fragment implements ICollectionActionView {

	private CollectionBaseAdapter collectionAdapter;
	private CollectionActionPresenter presenter;
	private Handler mHandler;
	private long lastClickTime;
	
	private int lastChecked;
	
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
	private ListView listReceipts;
	private Button btnAction;
	
	//Events
	/**
	 * Evento para ejecutar cuando se asigna un adapter de collection
	 * @author drodriguez
	 *
	 */
	public interface OnSetCollectionAdapter
	{
		public void collectionAdapterSet(CollectionBaseAdapter collectionAdapter, View rootView);
	}
	/**
	 * Evento por defecto
	 */
	private OnSetCollectionAdapter defaultCollectionAdapterEvent = new OnSetCollectionAdapter() {		
		@Override
		public void collectionAdapterSet(CollectionBaseAdapter collectionAdapter, View rootView) {
			if(rootView!=null)
			{
				((TextView)rootView.findViewById(R.id.lbl_receipt_list_type)).setText(collectionAdapter.getReceiptListTitle());
		        setButtonInfo();
			}	
		}
	};
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public CollectionActionFragment() {
		presenter = new CollectionActionPresenter(this);
		this.mHandler = new Handler();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_action, container, false);
        layoutSupplyInfo = (RelativeLayout) view.findViewById(R.id.layout_supply_info);
        txtNoSuppliesFound = (TextView) view.findViewById(R.id.txt_no_supplies_found);
        layoutWaitingSearch = (LinearLayout) view.findViewById(R.id.layout_waiting_search);
        
        txtClientName = (TextView) view.findViewById(R.id.txt_client_name);
        txtNus = (TextView) view.findViewById(R.id.txt_nus);
        txtAccountNumber = (TextView) view.findViewById(R.id.txt_account_number);
        txtClientAddress = (TextView) view.findViewById(R.id.txt_client_address);
        
        listReceipts = (ListView) view.findViewById(R.id.list_receipts);
        setReceiptListItemClickListener();
        
        btnAction = (Button) view.findViewById(R.id.btn_action);
        btnAction.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
					List<CoopReceipt> selectedReceipts = new ArrayList<CoopReceipt>();
					SparseBooleanArray sparseBooleanArray = listReceipts.getCheckedItemPositions();
					int size = listReceipts.getAdapter().getCount();
					for (int i = 0; i < size; i++) {
						if(sparseBooleanArray.get(i))
						{
							selectedReceipts.add((CoopReceipt)listReceipts.getItemAtPosition(i));
						}
					}
					if(selectedReceipts.size()>0)
						presenter.processAction(selectedReceipts);
					//else warnUserNotSelectedRoutes();
				}
		        lastClickTime = SystemClock.elapsedRealtime();
			}
		});
        if(collectionAdapter!=null)
        	defaultCollectionAdapterEvent.collectionAdapterSet(collectionAdapter, view);
        return view;
    }

    /**
     * Asigna el evento cuando se selecciona un item
     */
	private void setReceiptListItemClickListener() {
		listReceipts.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, final int position,
					long id) {
				new Thread(new Runnable() {
		            @Override
		            public void run() {
		            	final boolean setChecked = (position!=lastChecked)? true : listReceipts.isItemChecked(position);
		            	
						int size = listReceipts.getAdapter().getCount();
						lastChecked = position;
		                for (int i = 0; i <= position; i++) {
		                    final int pos = i;
		                    mHandler.post(new Runnable() {
		                        @Override
		                        public void run() {
		                        	listReceipts.setItemChecked(pos, setChecked);  
		                        }
		                    });
		                }
		                for (int i = position+1; i < size; i++) {
		                    final int pos = i;
		                    mHandler.post(new Runnable() {
		                        @Override
		                        public void run() {
		                        	listReceipts.setItemChecked(pos, false);  
		                        }
		                    });
		                }
		            }
		        }).start();  
			}
		});
	}

    private void setButtonInfo() {
		btnAction.setText(collectionAdapter.getButtonText());
		btnAction.setCompoundDrawablesWithIntrinsicBounds(null, null, collectionAdapter.getButtonDrawable(),null);
	}

	//#region Interface Methods
    
	@Override
	public CollectionActionPresenter getPresenter() {
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
				txtClientName.setText(TextFormater.capitalize(supply.getClientName()));
				txtNus.setText(""+supply.getSupplyId());
				txtAccountNumber.setText(AccountFormatter.formatAccountNumber(supply.getSupplyNumber()));
				txtClientAddress.setText(TextFormater.capitalize(supply.getClientAddress(), 2));
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
				listReceipts.setAdapter(new ReceiptAdapter(getActivity(), R.layout.receipt_list_item, receipts));
				hideSearchingMessage();
				if(layoutSupplyInfo.getVisibility()==View.GONE)
					layoutSupplyInfo.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void setCollectionAdapter(CollectionBaseAdapter collectionAdapter) {
		this.collectionAdapter = collectionAdapter;
		presenter.setCollectionBehavior(collectionAdapter.getCollectionBehavior());
		this.defaultCollectionAdapterEvent.collectionAdapterSet(collectionAdapter, getView());
	}
	
	//#endregion
}
