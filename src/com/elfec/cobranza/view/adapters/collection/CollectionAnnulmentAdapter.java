package com.elfec.cobranza.view.adapters.collection;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;

import com.elfec.cobranza.R;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionActionPresenter;
import com.elfec.cobranza.presenter.CollectionAnnulmentPresenter;
import com.elfec.cobranza.presenter.views.ICollectionActionView;
import com.elfec.cobranza.presenter.views.ICollectionAnnulmentView;
import com.elfec.cobranza.view.adapters.AnnulationReceiptAdapter;

public class CollectionAnnulmentAdapter extends CollectionBaseAdapter {

	public CollectionAnnulmentAdapter(Context context) {
		super(context);
	}

	@Override
	public String getActionTitle() {
		return getContext().getResources().getString(
				R.string.lbl_collection_annulment);
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
		return ContextCompat.getDrawable(getContext(), R.drawable.annulate);
	}

	@Override
	public String getReceiptListTitle() {
		return getContext().getResources().getString(
				R.string.lbl_paid_receipt_list_title);
	}

	@Override
	public int getActionErrorsTitleId() {
		return R.string.title_annulment_errors;
	}

	@Override
	public CollectionActionPresenter getCollectionPresenter(
			ICollectionActionView view) {
		return new CollectionAnnulmentPresenter((ICollectionAnnulmentView) view);
	}

	@Override
	public ArrayAdapter<CoopReceipt> getReceiptAdapter(
			List<CoopReceipt> receipts) {
		return new AnnulationReceiptAdapter(getContext(),
				R.layout.receipt_list_item, receipts, 1);
	}

	@Override
	public int getActionSuccessMsgId() {
		return R.string.msg_succesfull_annulment;
	}

	@Override
	public boolean hasToShowPickPrinter() {
		return false;
	}

}
