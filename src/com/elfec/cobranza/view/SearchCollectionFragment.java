package com.elfec.cobranza.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elfec.cobranza.R;
/**
 * Fragment que representa solamente la pantalla de búsqueda de cobranzas
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
	public static final String ARG_PAGE = "ARG_PAGE";

    @SuppressWarnings("unused")
	private int mPage;

    public static SearchCollectionFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SearchCollectionFragment fragment = new SearchCollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_payment_collection, container, false);
        return view;
    }
	
}
