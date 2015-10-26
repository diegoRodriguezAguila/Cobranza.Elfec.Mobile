package com.elfec.cobranza.view;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.AccountFormatter;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.events.SupplyResultPickedListener;
import com.elfec.cobranza.presenter.SearchCollectionPresenter;
import com.elfec.cobranza.presenter.views.ISearchCollectionView;
import com.elfec.cobranza.view.services.SupplyResultPickerService;

import de.keyboardsurfer.android.widget.crouton.Crouton;

/**
 * Fragment que representa solamente la pantalla de búsqueda de cobranzas
 * 
 * @author drodriguez
 *
 */
public class SearchCollectionFragment extends Fragment implements
		ISearchCollectionView {
	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of performed
	 * searches
	 */
	public interface Callbacks {
		/**
		 * Evento que se ejecuta cuando el usuario presionó el boton de búsqueda
		 */
		public void onSearchStarted();

		/**
		 * Callback for when a search is performed
		 */
		public void onSupplyFound(Supply supply);

		/**
		 * Callback para cuando existieron errores en una búsqueda realizada
		 * 
		 * @param errors
		 */
		public void onSearchErrors(List<Exception> errors);

		/**
		 * Callback para cuando se canceló la búsqueda realizada
		 */
		public void onSearchCanceled();
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
		}

		@Override
		public void onSearchErrors(List<Exception> errors) {
		}

		@Override
		public void onSearchStarted() {
		}

		@Override
		public void onSearchCanceled() {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SearchCollectionFragment() {
	}

	private SearchCollectionPresenter presenter;

	private Handler mHandler;
	private Button btnSearch;
	private EditText txtNUS;
	private EditText txtAccountNumber;
	private EditText txtClientName;
	private EditText txtNIT;

	private de.keyboardsurfer.android.widget.crouton.Style croutonStyle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		presenter = new SearchCollectionPresenter(this);
		this.mHandler = new Handler();
		croutonStyle = new de.keyboardsurfer.android.widget.crouton.Style.Builder()
				.setFontName("fonts/segoe_ui_semilight.ttf")
				.setTextSize(16)
				.setBackgroundColorValue(
						ContextCompat.getColor(getActivity(),
								R.color.cobranza_color)).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.fragment_search_payment_collection, container, false);
		btnSearch = (Button) view.findViewById(R.id.btn_search);
		txtNUS = (EditText) view.findViewById(R.id.txt_nus);
		// txtNUS.setText("425154");//420588 425154 (con hartas deudas)
		txtAccountNumber = (EditText) view
				.findViewById(R.id.txt_account_number);
		txtClientName = (EditText) view.findViewById(R.id.txt_client_name);
		txtNIT = (EditText) view.findViewById(R.id.txt_nit);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.searchSupply();
			}
		});
		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		// Activities containing this fragment must implement its callbacks.
		if (!(context instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) context;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	// #region Interface Methods

	@Override
	public String getNUS() {
		return txtNUS.getText().toString().trim();
	}

	@Override
	public String getAccountNumber() {
		return AccountFormatter.unformatAccountNumber(txtAccountNumber
				.getText().toString());
	}

	@Override
	public void notifyAtleastOneField() {
		Crouton.clearCroutonsForActivity(getActivity());
		Crouton.makeText(getActivity(), R.string.msg_atleast_one_filed,
				croutonStyle).show();
	}

	@Override
	public void showFoundSupply(Supply foundSupply) {
		mCallbacks.onSupplyFound(foundSupply);
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				txtNUS.setText(null);
				txtAccountNumber.setText(null);
				txtClientName.setText(null);
				txtNIT.setText(null);
			}
		});
	}

	@Override
	public void showSearchErrors(List<Exception> errors) {
		mCallbacks.onSearchErrors(errors);
	}

	@Override
	public void notifySearchStarted() {
		mCallbacks.onSearchStarted();
	}

	@Override
	public String getClientName() {
		return txtClientName.getText().toString().trim()
				.toUpperCase(Locale.getDefault());
	}

	@Override
	public String getNIT() {
		return txtNIT.getText().toString().trim();
	}

	@Override
	public void pickSupplyResult(final List<Supply> foundSupplies,
			final SupplyResultPickedListener supplyResultPickedListener) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				new SupplyResultPickerService(getActivity(), foundSupplies,
						supplyResultPickedListener).show();
			}
		});
	}

	@Override
	public void cancelSearch() {
		mCallbacks.onSearchCanceled();
	}

	// #endregion

}
