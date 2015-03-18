package com.elfec.cobranza.view.controls.services;

import java.util.List;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
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
	
	//components
	private Spinner spinnerAnnulmentReason;
	
	@SuppressLint("InflateParams")
	public CollectionAnnulmentDialogService(Context context, List<CoopReceipt> annulmentReceipts, OnCollectionAnnulmentCallback annulmentCallback)
	{
		if(annulmentReceipts.size()>1)
			throw new UnsupportedOperationException("Si bien la implementación para anulación de múltiples facturas existe,"
					+ "no se implementó el dialogo de anulación para múltiples facturas!");
		this.annulmentCallback = annulmentCallback;
		View annulmentView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.dialog_collection_annulment, null);
		spinnerAnnulmentReason = (Spinner) annulmentView.findViewById(R.id.spinner_annulment_reason);
		populateSpinner(context);
		showReceiptData(annulmentView, annulmentReceipts.get(0));//si no existe al menos uno dará excepción
		
		dialogBuilder = new AlertDialogPro.Builder(context);
		dialogBuilder.setTitle(R.string.title_collection_annulment).setIcon(R.drawable.collection_annulment_pressed)
			.setView(annulmentView)
			.setNegativeButton(R.string.btn_cancel, null)
			.setPositiveButton(R.string.btn_confirm, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int wich) {
					dialog.dismiss();
					if(CollectionAnnulmentDialogService.this.annulmentCallback!=null)
						CollectionAnnulmentDialogService.this.annulmentCallback
							.collectionAnnuled(((AnnulmentReason)spinnerAnnulmentReason.getSelectedItem()).getAnnulmentReasonRemoteId());
				}
			});
	}
	
	private void showReceiptData(View annulmentView, CoopReceipt annulmentReceipt ) {
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
	}

	/**
	 * Muestra el diálogo construido
	 */
	public void show()
	{
		dialogBuilder.show();
	}	
}
