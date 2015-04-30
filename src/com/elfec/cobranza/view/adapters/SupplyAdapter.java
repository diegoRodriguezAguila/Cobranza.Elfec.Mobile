package com.elfec.cobranza.view.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.AccountFormatter;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.model.Supply;

public class SupplyAdapter extends ArrayAdapter<Supply>{
	private List<Supply> supplies;
	private int resource;
	private LayoutInflater inflater = null;
	
	public SupplyAdapter(Context context, int resource,
			List<Supply> supplies) {
		super(context, resource, supplies);
		this.supplies = supplies;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return supplies.size();
	}
	
	@Override
	public Supply getItem(int position) {
		return supplies.get(position);
	}
	
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView = inflater.inflate(resource, null);
		Supply supply = getItem(position);
		
		((TextView)convertView.findViewById(R.id.txt_client_name)).setText(TextFormater.capitalize(supply.getClientName()));
		((TextView)convertView.findViewById(R.id.txt_nus)).setText(""+supply.getSupplyId());
		((TextView)convertView.findViewById(R.id.txt_account_number))
		.setText(AccountFormatter.formatAccountNumber(supply.getSupplyNumber()));
		((TextView)convertView.findViewById(R.id.txt_client_address)).setText(TextFormater.capitalize(supply.getClientAddress(), 2));
		return convertView;
	}
}
