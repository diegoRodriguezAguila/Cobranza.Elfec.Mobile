package com.elfec.cobranza.view.adapters.collection;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.elfec.cobranza.R;
import com.elfec.cobranza.presenter.CollectionActionPresenter;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter;
import com.elfec.cobranza.presenter.views.ICollectionActionView;

public class CollectionAnnulmentAdapter extends CollectionBaseAdapter {

	public CollectionAnnulmentAdapter(Context context) {
		super(context);
	}

	@Override
	public String getActionTitle() {
		return getContext().getResources().getString(R.string.lbl_collection_annulment);
	}

	@Override
	public int getTitleDrawableId() {
		return R.drawable.collection_annulment_selector;
	}

	@Override
	public String getButtonText() {
		return getContext().getResources().getString(R.string.btn_annulate);
	}

	@Override
	public Drawable getButtonDrawable() {
		return getContext().getResources().getDrawable(R.drawable.annulate);
	}

	@Override
	public String getReceiptListTitle() {
		return getContext().getResources().getString(R.string.lbl_paid_receipt_list_title);
	}

	@Override
	public int getActionErrorsTitleId() {
		return R.string.title_annulment_errors;
	}

	@Override
	public CollectionActionPresenter getCollectionPresenter(
			ICollectionActionView view) {
		return new CollectionAnnulmentPresenter(view);
	}

}
