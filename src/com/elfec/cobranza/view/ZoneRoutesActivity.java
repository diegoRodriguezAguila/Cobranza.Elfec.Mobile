package com.elfec.cobranza.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.elfec.cobranza.R;

/**
 * An activity representing a single Zone detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ZoneListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ZoneRoutesFragment}.
 */
public class ZoneRoutesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zone_routes);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putInt(ZoneRoutesFragment.ARG_ITEM_ID, getIntent()
					.getIntExtra(ZoneRoutesFragment.ARG_ITEM_ID, -1));
			ZoneRoutesFragment fragment = new ZoneRoutesFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.add(R.id.zone_detail_container, fragment).commit();
		}
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

}
