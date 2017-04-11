package com.elfec.cobranza.view.services;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;

import com.elfec.cobranza.R;
import com.elfec.cobranza.presenter.services.WipeAllDataServicePresenter;
import com.elfec.cobranza.presenter.services.WipeAllDataServicePresenter.WipeConfirmationListener;
import com.elfec.cobranza.presenter.views.IWipeAllDataDialog;

/**
 * Un servicio de dialogo para eliminar toda la información local de la aplicación en el dispositivo
 * @author drodriguez
 *
 */
public class WipeAllDataDialogService implements IWipeAllDataDialog {

	private AlertDialog.Builder builder;
	private WipeAllDataServicePresenter presenter;
	private WipeConfirmationListener wipeConfirmationListener;
	
	public WipeAllDataDialogService(Context context, WipeConfirmationListener wipeConfirmationListener)
	{
		presenter = new WipeAllDataServicePresenter(this);
		this.wipeConfirmationListener = wipeConfirmationListener;
		builder = new AlertDialog.Builder(context).setIcon(R.drawable.wipe_all_data_d)
		.setTitle(R.string.title_wipe_all_data);
		presenter.defineDialogType();
	}
	
	@Override
	public void initializeWipeConfirmDialog()
	{
		builder.setMessage(R.string.msg_wipe_all_data_confirm)
		.setPositiveButton(R.string.btn_confirm, new OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(wipeConfirmationListener!=null)
					wipeConfirmationListener.onWipeConfirmed();
			}
		})
		.setNegativeButton(R.string.btn_cancel, null);
	}
	
	@Override
	public void initializeCannotWipeDialog()
	{
		builder.setMessage(R.string.msg_cannot_wipe_all_data)
		.setPositiveButton(R.string.btn_ok, null);
	}
	
	/**
	 * Muestra el dialogo
	 */
	public void show(){
		builder.show();
	}
}
