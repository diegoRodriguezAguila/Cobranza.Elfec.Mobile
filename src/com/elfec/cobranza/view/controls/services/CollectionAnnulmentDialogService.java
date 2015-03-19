package com.elfec.cobranza.view.controls.services;

import java.util.List;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.model.AnnulmentReason;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter.OnCollectionAnnulmentCallback;

/**
 * Esta clase provee de un servicio de dialogo para confirmar y realizar la anulación de un cobro
 * @author drodriguez
 *
 */
public class CollectionAnnulmentDialogService {
	
	private AlertDialogPro.Builder dialogBuilder;
	private OnCollectionAnnulmentCallback annulmentCallback;
	private Context context;
	private CoopReceipt annulmentReceipt;
	
	//components
	private View annulmentView;
	private Spinner spinnerAnnulmentReason;
	private TextView txtInternalControlCode;
	
	@SuppressLint("InflateParams")
	public CollectionAnnulmentDialogService(Context context, List<CoopReceipt> annulmentReceipts, OnCollectionAnnulmentCallback annulmentCallback)
	{
		if(annulmentReceipts.size()>1)
			throw new UnsupportedOperationException("Si bien la implementación para anulación de múltiples facturas existe,"
					+ "no se implementó el dialogo de anulación para múltiples facturas!");
		this.context = context;
		this.annulmentCallback = annulmentCallback;
		annulmentView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.dialog_collection_annulment, null);
		spinnerAnnulmentReason = (Spinner) annulmentView.findViewById(R.id.spinner_annulment_reason);
		populateSpinner(context);
		
		txtInternalControlCode = (TextView) annulmentView.findViewById(R.id.txt_internal_control_code);
		
		this.annulmentReceipt = annulmentReceipts.get(0);//si no existe al menos uno dará excepción
		showReceiptData();
		
		dialogBuilder = new AlertDialogPro.Builder(context);
		dialogBuilder.setTitle(R.string.title_collection_annulment).setIcon(R.drawable.collection_annulment_pressed)
			.setView(annulmentView)
			.setNegativeButton(R.string.btn_cancel, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					hideKeyboard();
				}
			})
			.setPositiveButton(R.string.btn_confirm, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int wich) {
				}
			});
	}
	
	private void showReceiptData() {
		DateTime date = new DateTime(annulmentReceipt.getYear(), annulmentReceipt.getPeriodNumber(),1,0,0);
		((TextView)annulmentView.findViewById(R.id.txt_year_month))
			.setText(TextFormater.capitalize(date.toString("yyyy MMMM")));
		((TextView)annulmentView.findViewById(R.id.txt_receipt_number))
		.setText("N°: "+annulmentReceipt.getReceiptNumber()+"/"+annulmentReceipt.getActiveCollectionPayment().getId());
	}

	/**
	 * Obtiene las motivos de anulación para ponerlos en el spinner
	 */
	private void populateSpinner(Context context) {
		spinnerAnnulmentReason.setAdapter(new ArrayAdapter<AnnulmentReason>(context, 
				R.layout.simple_spinner_row, AnnulmentReason.getAll(AnnulmentReason.class)));
		spinnerAnnulmentReason.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View v,
					int pos, long id) {
				int annulmentReasonId = ((AnnulmentReason)adapter.getItemAtPosition(pos)).getAnnulmentReasonRemoteId();
				if(annulmentReasonId==2 || annulmentReasonId==3)
					txtInternalControlCode.setText(""+annulmentReceipt.getActiveCollectionPayment().getId());
				else txtInternalControlCode.setText(null);
				txtInternalControlCode.setError(null);
			}
			@Override public void onNothingSelected(AdapterView<?> adapter) {}
		});
	}

	/**
	 * Muestra el diálogo construido
	 */
	public void show()
	{
		final AlertDialogPro dialog = dialogBuilder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.getButton(AlertDialogPro.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
	      {            
	          @Override
	          public void onClick(View v)
	          {
	        	  String internalCode = txtInternalControlCode.getText().toString();
	        	  if(!internalCode.isEmpty())
	        	  {
		        	  if(Long.parseLong(txtInternalControlCode.getText().toString())==annulmentReceipt.getActiveCollectionPayment().getId())
						{
							hideKeyboard();
							dialog.dismiss();
							if(CollectionAnnulmentDialogService.this.annulmentCallback!=null)
								CollectionAnnulmentDialogService.this.annulmentCallback
									.collectionAnnuled(((AnnulmentReason)spinnerAnnulmentReason.getSelectedItem()).getAnnulmentReasonRemoteId());
					
						}
						else txtInternalControlCode.setError(context.getResources().getString(R.string.error_internal_control_code));
					}
	        	  else txtInternalControlCode.setError(context.getResources().getString(R.string.error_empty_internal_control_code));
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
}
