package com.elfec.cobranza.view.adapters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
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
import com.elfec.cobranza.model.CoopReceipt;

public class ReceiptAdapter extends ArrayAdapter<CoopReceipt> {
	private List<CoopReceipt> receipts;
	private int resource;
	private NumberFormat nf;
	private LayoutInflater inflater = null;
	
	public ReceiptAdapter(Context context, int resource,
			List<CoopReceipt> receipts) {
		super(context, resource, receipts);
		this.receipts = receipts;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		nf = DecimalFormat.getInstance();
		DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
		customSymbol.setDecimalSeparator(',');
		customSymbol.setGroupingSeparator('.');
		((DecimalFormat)nf).setDecimalFormatSymbols(customSymbol);
		nf.setGroupingUsed(true);
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
		setAccountBalanceInformation(convertView, receipt);
		DateTime date = new DateTime(receipt.getYear(), receipt.getPeriodNumber(),1,0,0);
		((TextView)convertView.findViewById(R.id.txt_year_month))
			.setText(TextFormater.capitalize(date.toString("yyyy MMMM")));
		((TextView)convertView.findViewById(R.id.txt_issue_date)).setText(receipt.getIssueDate().toString("dd/MM/yyyy"));
		((TextView)convertView.findViewById(R.id.txt_expiration_date)).setText(receipt.getExpirationDate().toString("dd/MM/yyyy"));
		((TextView)convertView.findViewById(R.id.txt_receipt_number)).setText("N°: "+receipt.getReceiptNumber());
		((TextView)convertView.findViewById(R.id.txt_category)).setText("Categoría: "+receipt.getCategoryId());
		((TextView)convertView.findViewById(R.id.txt_consume)).setText(nf.format(receipt.getSupplyStatus().getConsume()));
		return convertView;
	}
	
	private void setAccountBalanceInformation(View convertView, CoopReceipt receipt) {
		TextView txtReceiptAmount = ((TextView) convertView.findViewById(R.id.receipt_amount));
		txtReceiptAmount.setText(nf.format
				(receipt.getTotalAmount().toBigInteger().doubleValue()));
		
		String decimal = (receipt.getTotalAmount().remainder(BigDecimal.ONE).multiply(new BigDecimal("100"))
				.setScale(0, RoundingMode.CEILING).toString());
		((TextView) convertView.findViewById(R.id.receipt_amount_decimal))
		.setText(decimal.equals("0")?"00":decimal);
	}
}
