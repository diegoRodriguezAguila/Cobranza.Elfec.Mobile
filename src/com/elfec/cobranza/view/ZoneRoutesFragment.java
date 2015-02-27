package com.elfec.cobranza.view;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.elfec.cobranza.R;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.presenter.ZoneRoutesPresenter;
import com.elfec.cobranza.presenter.views.IZoneRoutesView;
import com.elfec.cobranza.view.adapters.RouteAdapter;

/**
 * A fragment representing a single Zone detail screen. This fragment is either
 * contained in a {@link ZoneListActivity} in two-pane mode (on tablets) or a
 * {@link ZoneRoutesActivity} on handsets.
 */
public class ZoneRoutesFragment extends Fragment implements IZoneRoutesView{
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	private ZoneRoutesPresenter presenter;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ZoneRoutesFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			presenter = new ZoneRoutesPresenter(this);
			presenter.loadZoneRoutes(getArguments().getInt(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_zone_routes,
				container, false);
		return rootView;
	}

	//#region Interface Methods
	@Override
	public void setZoneRoutes(final List<Route> zoneRoutes) {
		getActivity().runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				((ListView)getView().findViewById(R.id.listview_zone_routes))
				.setAdapter(new RouteAdapter(getActivity(), R.layout.route_list_item, zoneRoutes));
			}
		});
	}
	//#endregion
	
}
