package com.elfec.cobranza.view.adapters.collection;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.elfec.cobranza.R;
import com.elfec.cobranza.presenter.behavior.CollectionPaymentBehavior;

public class CollectionPaymentAdapter extends CollectionBaseAdapter{

	public CollectionPaymentAdapter(Context context) {
		super(context, new CollectionPaymentBehavior());
	}

	@Override
	public String getActionTitle() {
		return getContext().getResources().getString(R.string.lbl_collection_payment);
	}

	@Override
	public int getTitleDrawableId() {
		return R.drawable.collection_payment_selector;
	}

	@Override
	public String getButtonText() {
		return getContext().getResources().getString(R.string.btn_collect);
	}

	@Override
	public Drawable getButtonDrawable() {
		return getContext().getResources().getDrawable(R.drawable.collect);
	}

	@Override
	public String getReceiptListTitle() {
		return getContext().getResources().getString(R.string.lbl_pending_receipt_list_title);
	}

}
