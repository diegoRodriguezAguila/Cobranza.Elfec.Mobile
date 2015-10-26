package com.elfec.cobranza.view.services;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.model.events.DatePickListener;

/**
 * Provee el servicio de dialogo para la selección de fechas
 * @author drodriguez
 *
 */
public class DatesPickerService {
	private AlertDialogPro.Builder dialogBuilder;
	
	private DatePickListener listener;
	
	private DatePicker dateStart;
	private DatePicker dateEnd;
	
	@SuppressLint("InflateParams")
	public DatesPickerService(Context context, String title, int iconId, DatePickListener listener, final boolean isDateRange)
	{
		this.listener = listener;
		View rootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(isDateRange?R.layout.dialog_date_range_picker:R.layout.dialog_single_date_picker, null);
		
		dateStart = (DatePicker) rootView.findViewById(R.id.start_date);
		if(isDateRange)
			dateEnd = (DatePicker) rootView.findViewById(R.id.end_date);
		
		dialogBuilder = new AlertDialogPro.Builder(context);
		dialogBuilder.setTitle(title).setIcon(iconId)
			.setView(rootView)
			.setNegativeButton(R.string.btn_cancel, null)
			.setPositiveButton(R.string.btn_ok, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(DatesPickerService.this.listener!=null)
					{
						DateTime start = new DateTime(dateStart.getYear(), dateStart.getMonth()+1, dateStart.getDayOfMonth(), 0, 0);
						DateTime end = isDateRange?new DateTime(dateEnd.getYear(), dateEnd.getMonth()+1, dateEnd.getDayOfMonth(), 23, 59, 59,999):null;
						DatesPickerService.this.listener.onDatePicked(start, end);
					}
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
