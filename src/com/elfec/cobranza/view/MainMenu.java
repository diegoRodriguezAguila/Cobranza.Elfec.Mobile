package com.elfec.cobranza.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.elfec.cobranza.R;
import com.elfec.cobranza.view.adapters.collection.CollectionAdapterFactory;

public class MainMenu extends Activity {

	private long lastClickTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
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
	
	public void btnCollectionPaymentClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			Intent i = new Intent(MainMenu.this, CollectionAction.class);
			i.putExtra(CollectionAction.COLLECTION_TYPE, CollectionAdapterFactory.COLLECTION_PAYMENT);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnCollectionAnnulmentClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			Intent i = new Intent(MainMenu.this, CollectionAction.class);
			i.putExtra(CollectionAction.COLLECTION_TYPE, CollectionAdapterFactory.COLLECTION_ANNULMENT);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}

}
