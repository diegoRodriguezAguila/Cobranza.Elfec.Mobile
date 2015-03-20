package com.elfec.cobranza.view.adapters;

import java.util.List;

import org.joda.time.DateTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.helpers.utils.AmountsCounter;
import com.elfec.cobranza.model.CoopReceipt;

public class ReceiptAdapter extends ArrayAdapter<CoopReceipt> {
	private List<CoopReceipt> receipts;
	private int resource;
	private LayoutInflater inflater = null;
	
	public ReceiptAdapter(Context context, int resource,
			List<CoopReceipt> receipts) {
		super(context, resource, receipts);
		this.receipts = receipts;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return receipts.size();
	}
	
	public int getEnabledItemsCount()
	{
		int count = 0;
		int size = getCount();
		for (int i = 0; i < size; i++) {
			if(isEnabled(i))
				count++;
		}
		return count;
	}
	
	@Override
	public CoopReceipt getItem(int position) {
		return receipts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView = inflater.inflate(resource, null);
		CoopReceipt receipt = getItem(position);
		((TextView) convertView.findViewById(R.id.receipt_amount)).setText(AmountsCounter.formatIntAmount(receipt.getTotalAmount()));
		((TextView) convertView.findViewById(R.id.receipt_amount_decimal)).setText(AmountsCounter.formatDecimalAmount(receipt.getTotalAmount()));
		DateTime date = new DateTime(receipt.getYear(), receipt.getPeriodNumber(),1,0,0);
		((TextView)convertView.findViewById(R.id.txt_year_month))
			.setText(TextFormater.capitalize(date.toString("yyyy MMMM")));
		((TextView)convertView.findViewById(R.id.txt_issue_date)).setText(receipt.getIssueDate().toString("dd/MM/yyyy"));
		((TextView)convertView.findViewById(R.id.txt_expiration_date)).setText(receipt.getExpirationDate().toString("dd/MM/yyyy"));
		((TextView)convertView.findViewById(R.id.txt_receipt_number)).setText("N°: "+receipt.getReceiptNumber());
		((TextView)convertView.findViewById(R.id.txt_category)).setText("Categoría: "+receipt.getCategoryId());
		((TextView)convertView.findViewById(R.id.txt_consume)).setText(AmountsCounter.formatInteger(receipt.getSupplyStatus().getConsume()));
		return convertView;
	}
}
