package com.elfec.cobranza.view;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;

import com.alertdialogpro.AlertDialogPro;
import com.alertdialogpro.ProgressDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.presenter.views.IPaymentCollectionView;
import com.elfec.cobranza.view.adapters.CollectionPagerAdapter;
import com.elfec.cobranza.view.animations.HeightAnimation;
import com.elfec.cobranza.view.controls.SlidingTabLayout;
import com.elfec.cobranza.view.controls.SlidingTabLayout.TabColorizer;

public class PaymentCollection extends FragmentActivity implements SearchCollectionFragment.Callbacks{
	
	private CollectionPagerAdapter adapter;
	private ViewPager viewPager;
	private SlidingTabLayout slidingTabLayout;
	private ProgressDialogPro waitingDialog;
	private Fragment fSearchCollection;
	
	private boolean mIsTwoPane;
	private int searchCollectionHeight;
	private boolean searchCollectionCollapsed;
	private IPaymentCollectionView paymentView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		 // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // If viewpager doesn't exists is two pane view
        mIsTwoPane = (viewPager==null);
        if(!mIsTwoPane)
        {
        	initializeTabs();
        	paymentView = (IPaymentCollectionView)adapter.getItem(1);
        }
        else 
    	{
        	paymentView = (IPaymentCollectionView)getSupportFragmentManager().findFragmentById(R.id.f_payment_collection);
        	fSearchCollection = getSupportFragmentManager().findFragmentById(R.id.f_search_collection);
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
	
	/**
	 * Inicializa los metodos de las tabs en caso de usarse un layout con tabs
	 */
	private void initializeTabs() {
		adapter = new CollectionPagerAdapter(getSupportFragmentManager(), PaymentCollection.this);
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
		hideKeyboard();
		if(mIsTwoPane)
		{
			paymentView.hideNoSearchedSupplies();
			paymentView.showSearchingMessage();
		}
		else
		{
			waitingDialog = new ProgressDialogPro(PaymentCollection.this, R.style.Theme_FlavoredMaterialLight);
			waitingDialog.setMessage(getResources().getString(R.string.msg_searching));
			waitingDialog.show();
		}
	}
	
	@Override
	public void onSupplyFound(Supply supply) {
		paymentView.showSupplyInfo(supply);
		paymentView.getPresenter().loadSupplyReceipts(supply);
		hideSearchingMessage();
		if(!mIsTwoPane)
		{
			paymentView.hideNoSearchedSupplies();
			viewPager.setCurrentItem(1);
		}
		else
		{
			collapseSearchFragment();
		}
	}

	@Override
	public void onSearchErrors(List<Exception> errors) {
		hideSearchingMessage();
		if(mIsTwoPane)
		{
			paymentView.showSearchErrors(errors);
		}
		else
		{
			AlertDialogPro.Builder builder = new AlertDialogPro.Builder(PaymentCollection.this);
			builder.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
			.setPositiveButton(R.string.btn_ok, null)
			.show();
		}
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
}
