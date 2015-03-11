package com.elfec.cobranza.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elfec.cobranza.R;
/**
 * Fragment que representa solamente la pantalla de b�squeda de cobranzas
 * @author drodriguez
 *
 */
public class SearchCollectionFragment extends Fragment {
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SearchCollectionFragment() {
	}

    public static SearchCollectionFragment newInstance(int page) {
        SearchCollectionFragment fragment = new SearchCollectionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_payment_collection, container, false);
        return view;
    }
	
}
