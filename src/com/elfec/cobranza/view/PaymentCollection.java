package com.elfec.cobranza.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.elfec.cobranza.R;
import com.elfec.cobranza.view.adapters.CollectionPagerAdapter;
import com.elfec.cobranza.view.controls.SlidingTabLayout;
import com.elfec.cobranza.view.controls.SlidingTabLayout.TabColorizer;

public class PaymentCollection extends FragmentActivity {

	private CollectionPagerAdapter adapter;
	private ViewPager viewPager;
	private SlidingTabLayout slidingTabLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		 // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new CollectionPagerAdapter(getSupportFragmentManager(), 
        		PaymentCollection.this);
        viewPager.setAdapter(adapter);

        // Give the SlidingTabLayout the ViewPager
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabColorizer(new TabColorizer() {			
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.cobranza_color);
			}
		});
        slidingTabLayout.setCustomTabView(R.layout.custom_tab, R.id.tab_title, R.id.tab_icon);
        // Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
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
	
	public void btnSearchSupplyClick(View view)
	{
		viewPager.setAdapter(adapter);
		slidingTabLayout.setViewPager(viewPager);
	}
}
