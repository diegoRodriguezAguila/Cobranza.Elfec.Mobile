package com.elfec.cobranza.view.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elfec.cobranza.R;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;


public class BluetoothPrinterAdapter extends ArrayAdapter<DiscoveredPrinterBluetooth>{
	private List<DiscoveredPrinterBluetooth> printers;
	private int resource;
	private LayoutInflater inflater = null;
	
	public BluetoothPrinterAdapter(Context context, int resource,
			List<DiscoveredPrinterBluetooth> printers) {
		super(context, resource, printers);
		this.printers = printers;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return printers.size();
	}
	
	@Override
	public DiscoveredPrinterBluetooth getItem(int position) {
		return printers.get(position);
	}
	
	@Override
	public void add(DiscoveredPrinterBluetooth printer)
	{
		printers.add(printer);
		notifyDataSetChanged();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView = inflater.inflate(resource, null);
		DiscoveredPrinterBluetooth printer = getItem(position);
		
		((TextView)convertView.findViewById(R.id.lbl_printer_name)).setText(printer.friendlyName);
		((TextView)convertView.findViewById(R.id.lbl_printer_mac)).setText(printer.address);
		return convertView;
	}
}
