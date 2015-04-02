package com.elfec.cobranza.presenter;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import android.bluetooth.BluetoothAdapter;

import com.elfec.cobranza.R;
import com.elfec.cobranza.business_logic.ReportManager;
import com.elfec.cobranza.business_logic.printer.CashDeskDailyReportGenerator;
import com.elfec.cobranza.business_logic.printer.CashDeskReportGenerator;
import com.elfec.cobranza.business_logic.printer.CollectionAnnulmentRerportGenerator;
import com.elfec.cobranza.business_logic.printer.CollectionDetailReportGenerator;
import com.elfec.cobranza.model.events.BluetoothStateListener;
import com.elfec.cobranza.model.events.DatePickListener;
import com.elfec.cobranza.model.printer.IReportGenerator;
import com.elfec.cobranza.model.results.ManagerProcessResult;
import com.elfec.cobranza.presenter.services.BluetoothDevicePickerPresenter.OnBluetoothDevicePicked;
import com.elfec.cobranza.presenter.views.IMainMenuView;
import com.elfec.cobranza.settings.PreferencesManager;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;

public class MainMenuPresenter implements BluetoothStateListener {
	private IMainMenuView view;
	
	private List<Runnable> bluetoothActionsQueue;

	public MainMenuPresenter(IMainMenuView view) {
		this.view = view;
		bluetoothActionsQueue = new ArrayList<Runnable>();
		BluetoothAdapter.getDefaultAdapter().enable();
	}
	
	/**
	 * Inicia el proceso para generar el reporte
	 * de detalles de cobranza
	 */
	public void processCollectionDetailsReport()
	{
		view.showDateRangePicker(CollectionDetailReportGenerator.REPORT_NAME, 
			R.drawable.collection_details_d, new DatePickListener() {				
				@Override
				public void onDatePicked(DateTime... dates) {
					printReport(new CollectionDetailReportGenerator(dates[0], dates[1]));
				}
			});
	}
	
	/**
	 * Inicia el proceso para generar el reporte
	 * de cobranzas anuladas
	 */
	public void processAnnuledsReport()
	{
		view.showDateRangePicker(CollectionAnnulmentRerportGenerator.REPORT_NAME, 
				R.drawable.annuleds_report_d, new DatePickListener() {				
					@Override
					public void onDatePicked(DateTime... dates) {
						printReport(new CollectionAnnulmentRerportGenerator(dates[0], dates[1]));
					}
				});
	}
	
	/**
	 * Inicia el proceso para generar el reporte
	 * de cobranzas anuladas
	 */
	public void processDailySummaryReport()
	{
		view.showSingleDatePicker(CashDeskReportGenerator.REPORT_NAME, 
				R.drawable.daily_summary_d, new DatePickListener() {				
					@Override
					public void onDatePicked(DateTime... dates) {
						printReport(new CashDeskDailyReportGenerator(dates[0]));
					}
				});
	}
	
	/**
	 * Inicia el proceso para generar el reporte
	 * de cobranzas anuladas
	 */
	public void processCashDeskSummaryReport()
	{
		view.showDateRangePicker(CashDeskReportGenerator.REPORT_NAME, 
				R.drawable.cashdesk_summary_d, new DatePickListener() {				
					@Override
					public void onDatePicked(DateTime... dates) {
						printReport(new CashDeskReportGenerator(dates[0], dates[1]));
					}
				});
	}
	
	/**
	 * Llama a los métodos necesarios para imprimir un reporte
	 * @param generator
	 */
	public void printReport(final IReportGenerator generator)
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				final OnBluetoothDevicePicked callback = new OnBluetoothDevicePicked() {			
					@Override
					public void bluetoothDevicePicked(DiscoveredPrinterBluetooth device) {
						ManagerProcessResult result = ReportManager.printReport(generator, device);
						view.showPrintErrors(result.getErrors());
					}
				};
				Runnable runnable = new Runnable() {			
					@Override
					public void run() {
						DiscoveredPrinterBluetooth defaultPrinter = PreferencesManager.instance().getDefaultPrinter();
						if(defaultPrinter==null)
							view.showBluetoothPrintDialog(callback);
						else callback.bluetoothDevicePicked(defaultPrinter);
					}
				};
				if(BluetoothAdapter.getDefaultAdapter().isEnabled())
					runnable.run();
				else 
				{
					bluetoothActionsQueue.add(runnable);
					BluetoothAdapter.getDefaultAdapter().enable();
				}
			}
		}).start();
	}

	@Override
	public void onBluetoothTurnedOn() {
		for(Runnable runnable : bluetoothActionsQueue)
		{
			runnable.run();
		}
		bluetoothActionsQueue.clear();
	}

	@Override
	public void onBluetoothTurnedOff() {
		BluetoothAdapter.getDefaultAdapter().enable();
	}
	
}
