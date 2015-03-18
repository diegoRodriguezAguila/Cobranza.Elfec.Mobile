package com.elfec.cobranza.view.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elfec.cobranza.R;
import com.elfec.cobranza.model.CoopReceipt;

public class AnnulationReceiptAdapter extends ReceiptAdapter {

	/**
	 * El limite de facturas que se pueden seleccionar a la vez -1 para limite infinito
	 */
	private int limit;
	public AnnulationReceiptAdapter(Context context, int resource,
			List<CoopReceipt> receipts, int limit) {
		super(context, resource, receipts);
		this.limit = limit;
	}
	
	@Override
	public boolean isEnabled(int position) {
	    return limit==-1?true:position<limit;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		convertView.setEnabled(isEnabled(position));
		CoopReceipt receipt = getItem(position);
		((TextView)convertView.findViewById(R.id.txt_receipt_number)).setText("N°: "+receipt.getReceiptNumber()+"/"+receipt.getActiveCollectionPayment().getId());
		return convertView;
	}

}
