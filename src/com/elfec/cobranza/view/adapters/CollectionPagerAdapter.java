package com.elfec.cobranza.view.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.elfec.cobranza.R;
import com.elfec.cobranza.view.PaymentCollectionFragment;
import com.elfec.cobranza.view.SearchCollectionFragment;
import com.elfec.cobranza.view.controls.interfaces.IconTabProvider;

public class CollectionPagerAdapter extends FragmentPagerAdapter implements IconTabProvider{
	private int PAGE_COUNT = 2;
    private final String tabTitles[] = new String[] { "BÚSQUEDA", "COBRANZA"};
    private final int imageResId[] = new int[]{R.drawable.search_supplies_selector, R.drawable.payment_collection_selector};
	private final Fragment fragments[] = new Fragment[]{new SearchCollectionFragment(), new PaymentCollectionFragment()};
    @SuppressWarnings("unused")
	private Context context;

    public CollectionPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }
    

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
    	return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


	@Override
	public int getPageIconResId(int position) {
		return imageResId[position];
	}

}
