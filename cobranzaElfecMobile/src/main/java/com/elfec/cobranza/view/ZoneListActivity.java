package com.elfec.cobranza.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.elfec.cobranza.R;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

/**
 * An activity representing a list of Routes. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ZoneRoutesActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ZoneListFragment} and the item details (if present) is a
 * {@link ZoneRoutesFragment}.
 * <p>
 * This activity also implements the required {@link ZoneListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ZoneListActivity extends Activity implements
		ZoneListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zone_list);
		OracleDatabaseConnector.initializeContext(this);
		if (findViewById(R.id.zone_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
			getActionBar().setTitle(R.string.title_zone_routes);

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ZoneListFragment) getFragmentManager().findFragmentById(
					R.id.zone_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}
	
	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	
	@Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}

	/**
	 * Callback method from {@link ZoneListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putInt(ZoneRoutesFragment.ARG_ITEM_ID, id);
			arguments.putBoolean(ZoneRoutesFragment.IS_TWO_PANE, mTwoPane);
			ZoneRoutesFragment fragment = new ZoneRoutesFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
			.setCustomAnimations(R.anim.slide_left_in_fragment, R.anim.slide_left_out_fragment)
					.replace(R.id.zone_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ZoneRoutesActivity.class);
			detailIntent.putExtra(ZoneRoutesFragment.ARG_ITEM_ID, id);
			detailIntent.putExtra(ZoneRoutesFragment.IS_TWO_PANE, mTwoPane);
			startActivity(detailIntent);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
	}
}
