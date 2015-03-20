package com.elfec.cobranza.view.adapters.collection;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

import com.elfec.cobranza.R;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionActionPresenter;
import com.elfec.cobranza.presenter.CollectionPaymentPresenter;
import com.elfec.cobranza.presenter.views.ICollectionActionView;
import com.elfec.cobranza.view.adapters.ReceiptAdapter;

public class CollectionPaymentAdapter extends CollectionBaseAdapter{

	public CollectionPaymentAdapter(Context context) {
		super(context);
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

	@Override
	public int getActionErrorsTitleId() {
		return R.string.title_payment_errors;
	}

	@Override
	public CollectionActionPresenter getCollectionPresenter(
			ICollectionActionView view) {
		return new CollectionPaymentPresenter(view);
	}

	@Override
	public ArrayAdapter<CoopReceipt> getReceiptAdapter(List<CoopReceipt> receipts) {
		return new ReceiptAdapter(getContext(), R.layout.receipt_list_item, receipts);
	}

	@Override
	public int getActionSuccessMsgId() {
		return R.string.msg_succesfull_payment;
	}

}
