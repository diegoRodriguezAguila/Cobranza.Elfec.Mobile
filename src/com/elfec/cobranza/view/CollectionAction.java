package com.elfec.cobranza.view;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.alertdialogpro.ProgressDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.views.ICollectionPaymentView;
import com.elfec.cobranza.view.adapters.CollectionPagerAdapter;
import com.elfec.cobranza.view.adapters.collection.CollectionAdapterFactory;
import com.elfec.cobranza.view.adapters.collection.CollectionBaseAdapter;
import com.elfec.cobranza.view.animations.HeightAnimation;
import com.elfec.cobranza.view.controls.SlidingTabLayout;
import com.elfec.cobranza.view.controls.SlidingTabLayout.TabColorizer;
import com.elfec.cobranza.view.services.BluetoothDevicePickerService;

public class CollectionAction extends FragmentActivity implements SearchCollectionFragment.Callbacks{
	
	public static final String COLLECTION_TYPE = "collectionType";
	
	private CollectionBaseAdapter collectionAdapter;
	private CollectionPagerAdapter adapter;
	private ViewPager viewPager;
	private SlidingTabLayout slidingTabLayout;
	private ProgressDialogPro waitingDialog;
	private Fragment fSearchCollection;
	
	private boolean mIsTwoPane;
	private int searchCollectionHeight;
	private boolean searchCollectionCollapsed;
	private ICollectionPaymentView paymentView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		collectionAdapter = CollectionAdapterFactory.instance(
				getIntent().getExtras().getInt(COLLECTION_TYPE), this);
		 // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // If viewpager doesn't exists is two pane view
        mIsTwoPane = (viewPager==null);
        if(!mIsTwoPane)
        {
        	initializeTabs();
        	paymentView = (ICollectionPaymentView)adapter.getItem(1);
        }
        else 
    	{
        	paymentView = (ICollectionPaymentView)getSupportFragmentManager().findFragmentById(R.id.f_payment_collection);
        	fSearchCollection = getSupportFragmentManager().findFragmentById(R.id.f_search_collection);
        	setActionTitle();
    	}
        paymentView.setCollectionAdapter(collectionAdapter);
	}

	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection, menu);
		(menu.findItem(R.id.menu_item_pick_printer)).setVisible(collectionAdapter.hasToShowPickPrinter());
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int idItem = item.getItemId();
		switch(idItem)
		{
			case (R.id.menu_item_pick_printer):		
			{
				try
				{
					new BluetoothDevicePickerService(this, null, true).show();
				}
				catch(IllegalStateException e)
				{
					List<Exception> errors = new ArrayList<Exception>();
					errors.add(e);
					paymentView.showBluetoothErrors(errors);
				}
				return true;
			}
			default:
			{
				return true;
			}
		}
	}
	
	@Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}
	
	/**
	 * Inicializa los metodos de las tabs en caso de usarse un layout con tabs
	 */
	private void initializeTabs() {
		adapter = new CollectionPagerAdapter(getSupportFragmentManager(), CollectionAction.this, collectionAdapter);
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
        slidingTabLayout.setSmoothScrollingEnabled(true);
	}
	/**
	 * Deja de mostrar al usuario el mensaje de espera de busqueda
	 */
	public void hideSearchingMessage()
	{
		if(mIsTwoPane)
		{
			paymentView.hideSearchingMessage();
		}
		else
		{
			if(waitingDialog!=null)
				waitingDialog.dismiss();
		}
	}
	
	//#region Activity Callbacks

	@Override
	public void onSearchStarted() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				hideKeyboard();
				if(mIsTwoPane)
				{
					paymentView.hideNoSearchedSupplies();
					paymentView.showSearchingMessage();
				}
				else
				{
					waitingDialog = new ProgressDialogPro(CollectionAction.this, R.style.Theme_FlavoredMaterialLight);
					waitingDialog.setMessage(getResources().getString(R.string.msg_searching));
					waitingDialog.show();
				}
			}
		});
	}
	
	@Override
	public void onSupplyFound(Supply supply) {
		paymentView.showSupplyInfo(supply);
		paymentView.getPresenter().loadSupplyReceipts(supply);
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(!mIsTwoPane)
				{
					hideSearchingMessage();
					paymentView.hideNoSearchedSupplies();
					viewPager.setCurrentItem(1);
				}
				else
				{
					collapseSearchFragment();
				}
			}
		});
	}

	@Override
	public void onSearchErrors(final List<Exception> errors) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				hideSearchingMessage();
				if(mIsTwoPane)
				{
					paymentView.showSearchErrors(errors);
				}
				else
				{
					AlertDialogPro.Builder builder = new AlertDialogPro.Builder(CollectionAction.this);
					builder.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
					.setPositiveButton(R.string.btn_ok, null)
					.show();
				}
			}
		});
	}
	
	@Override
	public void onSearchCanceled() {
		runOnUiThread(new Runnable() {	
			@Override
			public void run() {
				if(mIsTwoPane)
					paymentView.showNoSearchedSupplies();
				hideSearchingMessage();
			}
		});
	}
	
	//#endregion
	
	/**
	 * Esconde el fragmento de búsqueda
	 */
	private void collapseSearchFragment() {
		searchCollectionCollapsed = true;
		hideKeyboard();
		if(searchCollectionHeight==0)
			searchCollectionHeight = fSearchCollection.getView().getHeight();
		Animation anim = new HeightAnimation(fSearchCollection.getView(), 
				searchCollectionHeight, 0);
		anim.setDuration(400);
		fSearchCollection.getView().startAnimation(anim);
	}
	
	/**
	 * muestra el fragmento de búsqueda
	 */
	private void expandSearchFragment() {
		searchCollectionCollapsed = false;
		Animation anim = new HeightAnimation(fSearchCollection.getView(), 
				0, searchCollectionHeight);
		anim.setDuration(400);
		fSearchCollection.getView().startAnimation(anim);
	}
	
	/**
	 * Esconde el teclado
	 */
	private void hideKeyboard() {   
	    // Check if no view has focus:
	    View view = this.getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
	public void lblSearchClicked(View view)
	{
		if(searchCollectionCollapsed)
			expandSearchFragment();
		else collapseSearchFragment();
	}
	/**
	 * Asigna el titulo correspondiente
	 */
	private void setActionTitle() {
		TextView lblActionTitle = (TextView) findViewById(R.id.lbl_action_title);
		lblActionTitle.setText(collectionAdapter.getActionTitle());
		lblActionTitle.setCompoundDrawablesWithIntrinsicBounds(
				getResources().getDrawable(collectionAdapter.getTitleDrawableId()), null, null, null);
	}
}
