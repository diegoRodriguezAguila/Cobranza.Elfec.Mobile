package com.elfec.cobranza.view.services;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alertdialogpro.AlertDialogPro;
import com.alertdialogpro.AlertDialogPro.Builder;
import com.elfec.cobranza.R;
import com.elfec.cobranza.model.Supply;
import com.elfec.cobranza.model.events.SupplyResultPickedListener;
import com.elfec.cobranza.view.adapters.SupplyAdapter;

/**
 * Provee el servicio de dialogo para la selección de resultados de una búsqueda de suministros
 * @author drodriguez
 *
 */
public class SupplyResultPickerService {
	
	private Builder dialogBuilder;
	private AlertDialogPro dialog; 
	private ListView listSuppliesFound;
	
	private SupplyResultPickedListener supplyResultPickedListener;
	/**
	 * Inicializa la clase
	 * @param context tiene que ser una activity dado que se invoca al intent de encendido de bluetooth
	 * @param bluetoothDevicePickedCallback
	 */
	@SuppressLint("InflateParams")
	public SupplyResultPickerService(Context context, List<Supply> foundSupplies, SupplyResultPickedListener supplyResultPickedListener)
	{
		this.supplyResultPickedListener = supplyResultPickedListener;
		
		View rootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.dialog_supply_search_result_picker, null);	
		listSuppliesFound = (ListView) rootView.findViewById(R.id.list_supplies_found);
		listSuppliesFound.setAdapter(new SupplyAdapter(context, R.layout.supply_list_item,foundSupplies));
		listSuppliesFound.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				dialog.dismiss();
				if(SupplyResultPickerService.this.supplyResultPickedListener!=null)
					SupplyResultPickerService.this
					.supplyResultPickedListener
					.onSupplyResultPicked((Supply)adapter.getItemAtPosition(position));
			}
		});
		
		dialogBuilder = new AlertDialogPro.Builder(context);
		dialogBuilder.setTitle(R.string.title_select_supply_search_result).setIcon(R.drawable.search_supplies_pressed)
			.setView(rootView)
			.setNegativeButton(R.string.btn_cancel, new OnClickListener() {			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(SupplyResultPickerService.this.supplyResultPickedListener!=null)
						SupplyResultPickerService.this.supplyResultPickedListener.onSupplyResultPickCanceled();
				}
			});
	}
	
	/**
	 * Muestra el diálogo construido
	 */
	public void show()
	{
		dialog = dialogBuilder.create();
		dialog.show();
	}
}
