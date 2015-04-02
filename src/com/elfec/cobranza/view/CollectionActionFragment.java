package com.elfec.cobranza.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.AccountFormatter;
import com.elfec.cobranza.helpers.text_format.AttributePicker;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.helpers.utils.AmountsCounter;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.events.BluetoothStateListener;
import com.elfec.cobranza.presenter.CollectionActionPresenter;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter.OnCollectionAnnulmentCallback;
import com.elfec.cobranza.presenter.CollectionPaymentPresenter.OnPaymentConfirmedCallback;
import com.elfec.cobranza.presenter.adapter_interfaces.ICollectionBaseAdapter;
import com.elfec.cobranza.presenter.services.BluetoothDevicePickerPresenter.OnBluetoothDevicePicked;
import com.elfec.cobranza.presenter.views.ICollectionActionView;
import com.elfec.cobranza.view.services.BluetoothDevicePickerService;
import com.elfec.cobranza.view.services.CollectionAnnulmentDialogService;
import com.elfec.cobranza.view.services.PaymentConfirmationDialogService;
import com.elfec.cobranza.view.services.bluetooth.BluetoothStateMonitor;

import de.keyboardsurfer.android.widget.crouton.Crouton;
/**
 * Fragment que representa solamente la pantalla de pago de un cobro 
 * @author drodriguez
 *
 */
public class CollectionActionFragment extends Fragment implements ICollectionActionView {

	private ICollectionBaseAdapter collectionAdapter;
	private CollectionActionPresenter presenter;
	private Handler mHandler;
	private long lastClickTime;
	private de.keyboardsurfer.android.widget.crouton.Style croutonStyle;
	
	private int lastChecked;
	
	//Layout parts
	private RelativeLayout layoutSupplyInfo;
	private TextView txtNoSuppliesFound;
	private LinearLayout layoutWaitingSearch;
	private LinearLayout layoutTotalAmount;
	
	//Supply info
	private TextView txtClientName;
	private TextView txtNUS;
	private TextView txtAccountNumber;
	private TextView txtClientAddress;
	private TextView txtReceiptListType;
	
	//Receipts
	private ListView listReceipts;
	private Button btnAction;
	
	//Total Amount
	private TextView txtTotalAmount;
	private TextView txtTotalAmountDecimal;

	//Events
	/**
	 * Evento para ejecutar cuando se asigna un adapter de collection
	 * @author drodriguez
	 *
	 */
	public interface OnSetCollectionAdapter
	{
		public void collectionAdapterSet(ICollectionBaseAdapter collectionAdapter, View rootView);
	}
	/**
	 * Evento por defecto
	 */
	private OnSetCollectionAdapter defaultCollectionAdapterEvent = new OnSetCollectionAdapter() {		
		@Override
		public void collectionAdapterSet(ICollectionBaseAdapter collectionAdapter, View rootView) {
			if(rootView!=null)
			{
				txtReceiptListType.setText(collectionAdapter.getReceiptListTitle());
		        setButtonInfo();
		        getActivity().getActionBar().setTitle(collectionAdapter.getActionTitle());
			}	
		}
	};
	/**
	 * Monitor en los cambios de estado del bluetooth, solo se usa para el pago de cobros
	 * no para anulaciones
	 */
	private BluetoothStateMonitor bluetoothStateMonitor;
	private Runnable attachBluetoothMonitor;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public CollectionActionFragment() {
		this.mHandler = new Handler();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        croutonStyle =  new de.keyboardsurfer.android.widget.crouton.Style.Builder().setFontName("fonts/segoe_ui_semilight.ttf").setTextSize(16)
				.setBackgroundColorValue(getResources().getColor(R.color.cobranza_color)).build();
    }
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if(bluetoothStateMonitor==null && attachBluetoothMonitor!=null)
			attachBluetoothMonitor.run();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(bluetoothStateMonitor!=null)
			bluetoothStateMonitor.removeListener();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_action, container, false);
        layoutSupplyInfo = (RelativeLayout) view.findViewById(R.id.layout_supply_info);
        txtNoSuppliesFound = (TextView) view.findViewById(R.id.txt_no_supplies_found);
        layoutWaitingSearch = (LinearLayout) view.findViewById(R.id.layout_waiting_search);
        layoutTotalAmount = (LinearLayout) view.findViewById(R.id.layout_total_amount);
        
        txtClientName = (TextView) view.findViewById(R.id.txt_client_name);
        txtNUS = (TextView) view.findViewById(R.id.txt_nus);
        txtAccountNumber = (TextView) view.findViewById(R.id.txt_account_number);
        txtClientAddress = (TextView) view.findViewById(R.id.txt_client_address);
        txtReceiptListType = (TextView) view.findViewById(R.id.lbl_receipt_list_type);
        
        listReceipts = (ListView) view.findViewById(R.id.list_receipts);
        setReceiptListItemClickListener();
        
        txtTotalAmount = (TextView) view.findViewById(R.id.txt_total_amount);
        txtTotalAmountDecimal = (TextView) view.findViewById(R.id.txt_total_amount_decimal);
        
        btnAction = (Button) view.findViewById(R.id.btn_action);
        btnAction.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
					List<CoopReceipt> selectedReceipts = getSelectedReceipts();
					if(selectedReceipts.size()>0)
						presenter.processAction(selectedReceipts);
					else warnUserNoReceiptsSelected();
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
				final boolean setChecked = (position!=lastChecked)? true : listReceipts.isItemChecked(position);
				listReceipts.setItemChecked(position, setChecked);					            	
				int size = listReceipts.getAdapter().getCount();
				lastChecked = position;
                for (int i = 0; i < size; i++) {
                	listReceipts.setItemChecked(i, (i>position)? false: setChecked);  
                }
				processTotalAmount();
			}
		});
	}

    private void setButtonInfo() {
		btnAction.setText(collectionAdapter.getButtonText());
		btnAction.setCompoundDrawablesWithIntrinsicBounds(null, null, collectionAdapter.getButtonDrawable(),null);
	}
    
    /**
     * Obtiene las facturas seleccionadas por el usuario
     * @return lista de facturas seleccionadas
     */
	private List<CoopReceipt> getSelectedReceipts() {
		List<CoopReceipt> selectedReceipts = new ArrayList<CoopReceipt>();
		SparseBooleanArray sparseBooleanArray = listReceipts.getCheckedItemPositions();
		int size = listReceipts.getAdapter().getCount();
		for (int i = 0; i < size; i++) {
			if(sparseBooleanArray.get(i))
			{
				selectedReceipts.add((CoopReceipt)listReceipts.getItemAtPosition(i));
			}
		}
		return selectedReceipts;
	}
    
    /**
	 * Muestra un mensaje al usuario de que no se seleccionaron facturas para cobrar o anular
	 */
	public void warnUserNoReceiptsSelected()
	{
		Crouton.clearCroutonsForActivity(getActivity());
		Crouton.makeText(getActivity(), R.string.msg_no_receipts_selected, croutonStyle).show();
	}
	/**
	 * Modifica el texto de monto total
	 */
	private void processTotalAmount() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				List<CoopReceipt> selectedReceipts = getSelectedReceipts();
				int size = selectedReceipts.size();
				final int animId = size==0?R.anim.slide_right_to_outside:R.anim.slide_left_from_outside;
				final int visibility = size==0?View.GONE:View.VISIBLE;
				final BigDecimal totalAmount = AmountsCounter.countTotalAmount(selectedReceipts, new AttributePicker<BigDecimal, CoopReceipt>() {
					@Override
					public BigDecimal pickAttribute(CoopReceipt receipt) {
						return receipt.getTotalAmount();
					}
				});				
				final String totalAmountStr = AmountsCounter.formatIntAmount(totalAmount);

				mHandler.post(new Runnable() {						
					@Override
					public void run() {
						layoutTotalAmount.setVisibility(visibility);
						layoutTotalAmount.startAnimation(AnimationUtils.loadAnimation(getActivity(), animId));
						if(!totalAmountStr.equals("0"))
						{
							txtTotalAmount.setText(totalAmountStr);
							txtTotalAmountDecimal.setText(AmountsCounter.formatDecimalAmount(totalAmount));
						}
					}
				});
			}
		}).start();
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
		layoutSupplyInfo.setVisibility(View.GONE);
	}

	@Override
	public void showSupplyInfo(final Supply supply) {
		getActivity().runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				txtClientName.setText(TextFormater.capitalize(supply.getClientName()));
				txtNUS.setText(""+supply.getSupplyId());
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
				int size = receipts.size();
				listReceipts.setAdapter(collectionAdapter.getReceiptAdapter(receipts));
				hideSearchingMessage();
				processTotalAmount();
				if(size==0)
					txtReceiptListType.setText("Sin " +collectionAdapter.getReceiptListTitle().toLowerCase(Locale.getDefault()));
				else txtReceiptListType.setText(collectionAdapter.getReceiptListTitle());
			}
		});
	}

	@Override
	public void setCollectionAdapter(ICollectionBaseAdapter collectionAdapter) {
		this.collectionAdapter = collectionAdapter;
		presenter = collectionAdapter.getCollectionPresenter(this);
		attachBluetoothMonitor = new Runnable() {			
			@Override
			public void run() {
				if(presenter instanceof BluetoothStateListener)
					bluetoothStateMonitor = new BluetoothStateMonitor(getActivity(),(BluetoothStateListener)presenter);
			}
		};
		if(this.isAdded())
			attachBluetoothMonitor.run();
		this.defaultCollectionAdapterEvent.collectionAdapterSet(this.collectionAdapter, getView());
	}

	@Override
	public void informActionSuccessfullyFinished() {
		getActivity().runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				Crouton.clearCroutonsForActivity(getActivity());
				Crouton.makeText(getActivity(), collectionAdapter.getActionSuccessMsgId(), croutonStyle).show();
			}
		});
	}

	@Override
	public void showActionErrors(final List<Exception> errors) {
		if(getActivity()!=null)
		getActivity().runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(errors.size()>0)
				{
					AlertDialogPro.Builder builder = new AlertDialogPro.Builder(getActivity());
					builder.setTitle(collectionAdapter.getActionErrorsTitleId())
					.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
					.setPositiveButton(R.string.btn_ok, null)
					.show();
				}
			}
		});
	}

	@Override
	public void showPaymentConfirmation(List<CoopReceipt> selectedReceipts,
			OnPaymentConfirmedCallback paymentCallback) {
		new PaymentConfirmationDialogService(getActivity(), selectedReceipts, paymentCallback).show();
	}

	@Override
	public void showAnnulmentConfirmation(List<CoopReceipt> selectedReceipts,
			OnCollectionAnnulmentCallback annulmentCallback) {
		new CollectionAnnulmentDialogService(getActivity(), selectedReceipts, annulmentCallback).show();
	}

	@Override
	public void showBluetoothPrintDialog(final OnBluetoothDevicePicked callback) {
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				try
				{
					new BluetoothDevicePickerService(getActivity(), callback, false).show();
				}
				catch(IllegalStateException e)
				{
					List<Exception> exceptions = new ArrayList<Exception>();
					exceptions.add(e);
					showPrintErrors(exceptions);
				}
			}
		});
	}

	@Override
	public void showPrintErrors(final List<Exception> errors) {
		if(getActivity()!=null)
		getActivity().runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(errors.size()>0)
				{
					AlertDialogPro.Builder builder = new AlertDialogPro.Builder(getActivity());
					builder.setTitle(R.string.title_print_errors)
					.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
					.setPositiveButton(R.string.btn_ok, null)
					.show();
				}
			}
		});
	}

	@Override
	public void showBluetoothErrors(final List<Exception> errors) {
		getActivity().runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(errors.size()>0)
				{
					AlertDialogPro.Builder builder = new AlertDialogPro.Builder(getActivity());
					builder.setTitle(R.string.title_bluetooth_errors)
					.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
					.setPositiveButton(R.string.btn_ok, null)
					.show();
				}
			}
		});
	}
	
	//#endregion
}
