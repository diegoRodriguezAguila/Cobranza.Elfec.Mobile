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
import com.elfec.cobranza.model.Route;

public class RouteAdapter extends ArrayAdapter<Route> {

	private List<Route> routes;
	private int resource;
	private LayoutInflater inflater = null;
	
	public RouteAdapter(Context context, int resource,
			List<Route> routes) {
		super(context, resource, routes);
		this.routes = routes;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return routes.size();
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
	public boolean isEnabled(int position) {
	    return !getItem(position).isLoaded();
	}

	@Override
	public Route getItem(int position) {
		return routes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView = inflater.inflate(resource, null);
		Route route = getItem(position);
		convertView.setEnabled(!route.isLoaded());
		((TextView)convertView.findViewById(R.id.txt_route_id)).setText(""+route.getRouteRemoteId());
		((TextView)convertView.findViewById(R.id.txt_route_desc)).setText(TextFormater.capitalize(route.getDescription(), 3)+(route.isLoaded()?" (cargado)":""));
		return convertView;
	}
}
