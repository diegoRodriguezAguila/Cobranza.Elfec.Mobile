package com.elfec.cobranza.view;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.elfec.cobranza.R;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.SearchCollectionPresenter;
import com.elfec.cobranza.presenter.views.ISearchCollectionView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
/**
 * Fragment que representa solamente la pantalla de búsqueda de cobranzas
 * @author drodriguez
 *
 */
public class SearchCollectionFragment extends Fragment implements ISearchCollectionView{
	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of performed searches
	 */
	public interface Callbacks {
		/**
		 * Callback for when a search is performed
		 */
		public void onSupplyFound(Supply supply);
	}
	
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;
	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onSupplyFound(Supply supply) {
			
		}};
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SearchCollectionFragment() {
	}
	
	private SearchCollectionPresenter presenter;
	private Button btnSearch;
	private EditText txtNUS;
	private EditText txtAccountNumber;
	
	private de.keyboardsurfer.android.widget.crouton.Style croutonStyle;

    public static SearchCollectionFragment newInstance(int page) {
        SearchCollectionFragment fragment = new SearchCollectionFragment();
        return fragment;
    }
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		presenter = new SearchCollectionPresenter(this);
		croutonStyle =  new de.keyboardsurfer.android.widget.crouton.Style.Builder().setFontName("fonts/segoe_ui_semilight.ttf").setTextSize(16)
				.setBackgroundColorValue(getResources().getColor(R.color.cobranza_color)).build();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_payment_collection, container, false);
        btnSearch = (Button) view.findViewById(R.id.btn_search);
        txtNUS = (EditText) view.findViewById(R.id.txt_nus);
        txtAccountNumber = (EditText) view.findViewById(R.id.txt_account_number);
        btnSearch.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				presenter.searchSupply();
			}
		});
        return view;
    }
    
    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) activity;
	}
    
    @Override
	public void onDetach() {
		super.onDetach();
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

    
    //#region Interface Methods
    
	@Override
	public String getNUS() {
		return txtNUS.getText().toString().trim();
	}

	@Override
	public String getAccountNumber() {
		String accNumber = txtAccountNumber.getText().toString();
		if(accNumber.length()>0 && accNumber.charAt(0)=='0')
			accNumber = accNumber.substring(1, accNumber.length());
		return accNumber.replace("-", "").trim();
	}

	@Override
	public void notifyAtleastOneField() {
		Crouton.clearCroutonsForActivity(getActivity());
		Crouton.makeText(getActivity(), R.string.msg_atleast_one_filed, croutonStyle).show();
	}

	@Override
	public void showFoundSupply(Supply foundSupply) {
		mCallbacks.onSupplyFound(foundSupply);
	}

	@Override
	public void showSearchErrors(List<Exception> errors) {
		// TODO Auto-generated method stub
		
	}
	
	//#endregion
	
}
