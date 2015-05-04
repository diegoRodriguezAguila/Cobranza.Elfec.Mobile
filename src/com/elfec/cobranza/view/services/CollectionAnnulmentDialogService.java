package com.elfec.cobranza.view.services;

import java.util.List;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.model.AnnulmentReason;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter.OnCollectionAnnulmentCallback;
import com.elfec.cobranza.presenter.services.CollectionAnnulmentDialogPresenter;
import com.elfec.cobranza.presenter.views.ICollectionAnnulmentDialog;
import com.elfec.cobranza.view.adapters.AnnulmentReasonAdapter;

/**
 * Esta clase provee de un servicio de dialogo para confirmar y realizar la anulación de un cobro
 * @author drodriguez
 */
public class CollectionAnnulmentDialogService implements ICollectionAnnulmentDialog {
	
	private AlertDialogPro.Builder dialogBuilder;
	private AlertDialogPro dialog;
	private Context context;
	private Handler mHandler;
	private AnnulmentReasonAdapter adapter;

	private CollectionAnnulmentDialogPresenter presenter;
	//components
	private View annulmentView;
	private Spinner spinnerAnnulmentReason;
	private EditText txtInternalControlCode;
	
	@SuppressLint("InflateParams")
	public CollectionAnnulmentDialogService(Context context, List<CoopReceipt> annulmentReceipts, OnCollectionAnnulmentCallback annulmentCallback)
	{
		if(annulmentReceipts.size()>1)
			throw new UnsupportedOperationException("Si bien la implementación para anulación de múltiples facturas existe,"
					+ "no se implementó el dialogo de anulación para múltiples facturas!");
		presenter = new CollectionAnnulmentDialogPresenter(this, annulmentCallback, annulmentReceipts.get(0));
		this.context = context;
		mHandler = new Handler(Looper.getMainLooper());
		annulmentView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.dialog_collection_annulment, null);
		spinnerAnnulmentReason = (Spinner) annulmentView.findViewById(R.id.spinner_annulment_reason);
		presenter.loadAnnulmentReasons();
		setItemClickListener();		
		txtInternalControlCode = (EditText) annulmentView.findViewById(R.id.txt_internal_control_code);
		
		presenter.loadReceiptInfo();
		
		dialogBuilder = new AlertDialogPro.Builder(context);
		dialogBuilder.setTitle(R.string.title_collection_annulment).setIcon(R.drawable.collection_annulment_pressed)
			.setView(annulmentView)
			.setNegativeButton(R.string.btn_cancel, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					hideKeyboard();
				}
			})
			.setPositiveButton(R.string.btn_confirm, null);
	}
	

	/**
	 * Asigna las acciones que se toman al cambiar de item seleccionado
	 */
	private void setItemClickListener() {
		spinnerAnnulmentReason.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View v,
					int pos, long id) {
				if(pos!=0)
					presenter.processSelectedAnnulmentReason();
			}
			@Override public void onNothingSelected(AdapterView<?> adapter) {}
		});
	}

	/**
	 * Muestra el diálogo construido
	 */
	public void show()
	{
		dialog = dialogBuilder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.getButton(AlertDialogPro.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
	      {            
	          @Override
	          public void onClick(View v) {
	        	  presenter.verifyAnnulation();
	          }
	      });
	}	
	
	/**
	 * Esconde el teclado
	 */
	private void hideKeyboard() {   
	    // Check if no view has focus:
	    View view = annulmentView.findFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
	//#region Interface Methods

	@Override
	public void setAnnulmentReasons(final List<AnnulmentReason> annulmentReasons) {
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				adapter = new AnnulmentReasonAdapter(
						new ArrayAdapter<AnnulmentReason>(context, 
								R.layout.simple_spinner_row, annulmentReasons),
								context, 
						R.layout.nothing_selected_spinner_row);
				spinnerAnnulmentReason.setAdapter(adapter);
			}
		});
	}

	@Override
	public String getInternalControlCode() {
		return txtInternalControlCode.getText().toString();
	}

	@Override
	public void closeView() {
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				hideKeyboard();
				if(dialog!=null && dialog.isShowing())
					dialog.dismiss();
			}
		});		
	}

	@Override
	public int getSelectedAnnulmentReasonId() {
		AnnulmentReason selectedReason = (AnnulmentReason)spinnerAnnulmentReason.getSelectedItem();
		return (selectedReason!=null)? selectedReason.getAnnulmentReasonRemoteId():-1;
	}

	@Override
	public void setInternalControlCodeError(final int strErrorId) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				txtInternalControlCode.setError(strErrorId==-1?null:context.getResources().getString(strErrorId));
			}
		});
	}

	@Override
	public void setPeriod(int year, int month) {
		DateTime date = new DateTime(year, month,1 ,0 ,0 );
		((TextView)annulmentView.findViewById(R.id.txt_year_month))
			.setText(TextFormater.capitalize(date.toString("yyyy MMMM")));
	}

	@Override
	public void setReceiptNumber(String receiptNumber) {
		((TextView)annulmentView.findViewById(R.id.txt_receipt_number))
		.setText("N°: "+receiptNumber);
	}


	@Override
	public void setInternalControlCode(final long internalControlCode) {
		mHandler.post(new Runnable() {		
			@Override
			public void run() {
				String controlCodeStr = ""+internalControlCode;
				txtInternalControlCode.setText(internalControlCode==-1?null:controlCodeStr);
				if(internalControlCode!=-1)
					txtInternalControlCode.setSelection(controlCodeStr.length());
			}
		});
	}


	@Override
	public void setAnnulmentReasonError(final int strErrorId) {
		mHandler.post(new Runnable() {		
			@Override
			public void run() {
				if(adapter!=null)
					adapter.setError(spinnerAnnulmentReason.getSelectedView(), 
							strErrorId==-1?null:
							context.getResources().getString(strErrorId));
			}
		});
	}
	
	//#endregion
}
