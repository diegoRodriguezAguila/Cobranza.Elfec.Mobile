package com.elfec.cobranza.view.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

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
		return convertView;
	}

}
