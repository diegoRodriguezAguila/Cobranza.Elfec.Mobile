package com.elfec.cobranza.view.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.TextFormater;
import com.elfec.cobranza.model.Zone;


public class ZoneAdapter extends ArrayAdapter<Zone> {
	private List<Zone> zones;
	private int resource;
	private LayoutInflater inflater = null;
	
	public ZoneAdapter(Context context, int resource,
			List<Zone> zones) {
		super(context, resource, zones);
		this.zones = zones;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return zones.size();
	}

	@Override
	public Zone getItem(int position) {
		return zones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView = inflater.inflate(resource, null);
		Zone zone = getItem(position);
		((TextView)convertView.findViewById(R.id.txt_zone_id)).setText(""+zone.getZoneRemoteId());
		((TextView)convertView.findViewById(R.id.txt_zone_desc)).setText(TextFormater.capitalize(zone.getDescription(), 3));
		return convertView;
	}
}
