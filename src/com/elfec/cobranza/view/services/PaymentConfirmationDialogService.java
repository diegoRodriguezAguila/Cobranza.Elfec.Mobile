package com.elfec.cobranza.view.services;

import java.math.BigDecimal;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.utils.ReceiptsCounter;
import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionPaymentPresenter.OnPaymentConfirmedCallback;

/**
* Esta clase provee de un servicio para mostrar un diálogo para la confirmación de un pago
* @author Diego
*
*/
public class PaymentConfirmationDialogService {
	
	private AlertDialogPro.Builder dialogBuilder;
	private OnPaymentConfirmedCallback paymentConfirmedCallback;
	
	//components
	private TextView lblPaymentConfirmation;
	private TextView lblPaymentReceiptCount;
	private TextView txtTotalAmount;
	private TextView txtTotalAmountDecimal;

	@SuppressLint("InflateParams")
	public PaymentConfirmationDialogService(Context context, List<CoopReceipt> paymentReceipts, OnPaymentConfirmedCallback paymentConfirmedCallback) {
		this.paymentConfirmedCallback = paymentConfirmedCallback;
		View confirmationView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.dialog_payment_confirmation, null);
		
		lblPaymentConfirmation = (TextView) confirmationView.findViewById(R.id.lbl_payment_confirmation);
		lblPaymentReceiptCount = (TextView) confirmationView.findViewById(R.id.lbl_payment_receipt_count);
		txtTotalAmount = (TextView) confirmationView.findViewById(R.id.txt_total_amount);
		txtTotalAmountDecimal = (TextView) confirmationView.findViewById(R.id.txt_total_amount_decimal);
		
		setViewInfo(context, paymentReceipts);
		
		dialogBuilder = new AlertDialogPro.Builder(context);
		dialogBuilder.setTitle(R.string.title_payment_confirmation).setIcon(R.drawable.collection_payment_pressed)
			.setView(confirmationView)
			.setNegativeButton(R.string.btn_cancel, null)
			.setPositiveButton(R.string.btn_confirm, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int wich) {
					dialog.dismiss();
					if(PaymentConfirmationDialogService.this.paymentConfirmedCallback!=null)
						PaymentConfirmationDialogService.this.paymentConfirmedCallback.paymentConfirmed();
				}
			});
	}
	
	private void setViewInfo(Context context, List<CoopReceipt> paymentReceipts)
	{
		String nus = "<b>"+paymentReceipts.get(0).getSupplyId()+"</b>";
		String paymentConfirmText = String.format(context.getResources().getString(R.string.lbl_payment_confirmation), nus);
		lblPaymentConfirmation.setText(Html.fromHtml(paymentConfirmText));
		int count = paymentReceipts.size();
		String paymentCountText = String.format(
				context.getResources().getString(R.string.lbl_payment_count), count>1?"n":"", ""+count, count>1?"s":"");
		lblPaymentReceiptCount.setText(paymentCountText);
		BigDecimal totalAmount = ReceiptsCounter.countTotalAmount(paymentReceipts);
		txtTotalAmount.setText(ReceiptsCounter.formatIntAmount(totalAmount));
		txtTotalAmountDecimal.setText(ReceiptsCounter.formatDecimalAmount(totalAmount));
	}
	
	/**
	 * Muestra el diálogo construido
	 */
	public void show()
	{
		dialogBuilder.show();
	}	
}
