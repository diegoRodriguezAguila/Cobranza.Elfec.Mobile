package com.elfec.cobranza.view;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
	/**
	 * La clave para recuperar la propiedad is two pane
	 */
	public static final String IS_TWO_PANE ="is_two_pane";

	private ZoneRoutesPresenter presenter;
	private boolean isTwoPane;
	
	private boolean areAllItemsSelected;
	
	private ListView listViewZoneRoutes;
	private Button btnSelectAllRoutes;
	private Button btnDownloadRoutes;
	private Handler mHandler;
	

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ZoneRoutesFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		presenter = new ZoneRoutesPresenter(this);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			presenter.loadZoneRoutes(getArguments().getInt(ARG_ITEM_ID));
		}
		if(getArguments().containsKey(IS_TWO_PANE))
			isTwoPane = getArguments().getBoolean(IS_TWO_PANE);
		this.mHandler = new Handler();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_zone_routes,
				container, false);
		if(!isTwoPane)
			((TextView)rootView.findViewById(R.id.txt_routes)).setVisibility(View.GONE);
		listViewZoneRoutes = ((ListView)rootView.findViewById(R.id.listview_zone_routes));
		listViewZoneRoutes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				if(areAllItemsSelected && !view.isActivated())
				{
					areAllItemsSelected = false;
					setButtonSelectAllName();
				}
				if(listViewZoneRoutes.getCheckedItemCount()==listViewZoneRoutes.getAdapter().getCount())
				{
					areAllItemsSelected = true;
					setButtonSelectAllName();
				}
			}
		});
		btnSelectAllRoutes = ((Button)rootView.findViewById(R.id.btn_select_all_routes));
		btnSelectAllRoutes.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				areAllItemsSelected=!areAllItemsSelected;
				btnSelectAllRoutesClick(areAllItemsSelected);
				setButtonSelectAllName();
			}
		});
		btnDownloadRoutes = ((Button)rootView.findViewById(R.id.btn_download_routes));
		btnDownloadRoutes.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Cargar datos
			}
		});
		return rootView;
	}
	
	/**
	 * Asigna el nombre y drawable del boton de seleccionar todos
	 */
	private void setButtonSelectAllName() {
		btnSelectAllRoutes.setText(areAllItemsSelected?R.string.btn_unselect_all_routes:R.string.btn_select_all_routes);
		btnSelectAllRoutes.setCompoundDrawablesWithIntrinsicBounds(
				getResources().getDrawable(areAllItemsSelected?R.drawable.list_unselect_all:R.drawable.list_select_all)
				, null, null, null);
	}
	
	public void btnSelectAllRoutesClick(final boolean select)
	{
		new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < listViewZoneRoutes.getAdapter().getCount(); i++) {
                    final int pos = i;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                        	listViewZoneRoutes.setItemChecked(pos, select);  
                        }
                    });
                }
            }
        }).start();     
	}

	//#region Interface Methods
	@Override
	public void setZoneRoutes(final List<Route> zoneRoutes) {
		getActivity().runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				listViewZoneRoutes.setAdapter(new RouteAdapter(getActivity(), R.layout.route_list_item, zoneRoutes));
			}
		});
	}
	//#endregion
	
}
