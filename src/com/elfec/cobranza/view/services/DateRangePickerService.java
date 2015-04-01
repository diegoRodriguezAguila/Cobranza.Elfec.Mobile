package com.elfec.cobranza.view.services;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.model.events.DatePickListener;

/**
 * Provee el servicio de dialogo para la selección de un rango de fechas
 * @author drodriguez
 *
 */
public class DateRangePickerService {
	private AlertDialogPro.Builder dialogBuilder;
	
	private DatePickListener listener;
	
	@SuppressLint("InflateParams")
	public DateRangePickerService(Context context, String title, int iconId, DatePickListener listener)
	{
		this.listener = listener;
		View rootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.dialog_payment_confirmation, null);
		
		dialogBuilder = new AlertDialogPro.Builder(context);
		dialogBuilder.setTitle(title).setIcon(iconId)
			.setView(rootView)
			.setNegativeButton(R.string.btn_cancel, null)
			.setPositiveButton(R.string.btn_ok, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(DateRangePickerService.this.listener!=null)
						DateRangePickerService.this.listener.onDatePicked(DateTime.now(), DateTime.now());
				}
			});
	}
	
	/**
	 * Muestra el diálogo construido
	 */
	public void show()
	{
		dialogBuilder.show();
	}	
}
