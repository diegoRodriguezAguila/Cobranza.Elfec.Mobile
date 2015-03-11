package com.elfec.cobranza.view;

import com.elfec.cobranza.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Fragment que representa solamente la pantalla de pago de un cobro 
 * @author drodriguez
 *
 */
public class PaymentCollectionFragment extends Fragment {
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PaymentCollectionFragment() {
	}
	
	public static PaymentCollectionFragment newInstance(int page) {
		PaymentCollectionFragment fragment = new PaymentCollectionFragment();
        return fragment;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_collection, container, false);
        return view;
    }
}
