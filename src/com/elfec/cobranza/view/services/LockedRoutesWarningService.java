package com.elfec.cobranza.view.services;

import java.util.List;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.cobranza.R;
import com.elfec.cobranza.helpers.text_format.AttributePicker;
import com.elfec.cobranza.helpers.text_format.MessageListFormatter;
import com.elfec.cobranza.model.Route;
import com.elfec.cobranza.model.events.OnRoutesImportConfirmed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Html;
import android.text.Spanned;

/**
 * Es un servicio de dialogo que se encarga de advertir al usuario de rutas bloqueadas por otro dispositivo
 * @author drodriguez
 *
 */
public class LockedRoutesWarningService {

	private boolean mIsOnlyOne;
	private boolean mNoMoreRoutes;
	private OnServiceCanceled mCancelCallback;
	
	private AlertDialogPro.Builder builder;
	
	/**
	 * Interfaz que se llama cuando se cancela este servicio
	 * @author drodriguez
	 *
	 */
	public interface OnServiceCanceled
	{
		public void onServiceCanceled();
	}
	
	public LockedRoutesWarningService(Context context, List<Route> lockedRoutes, 
			final OnRoutesImportConfirmed callback, boolean noMoreRoutes, OnServiceCanceled cancelCallback)
	{
		mIsOnlyOne = lockedRoutes.size()==1;
		this.mNoMoreRoutes = noMoreRoutes;
		this.mCancelCallback = cancelCallback;
		
		builder = new AlertDialogPro.Builder(context);
		builder.setTitle(R.string.title_warning_locked_routes)
		.setMessage(buildMessage(lockedRoutes));
		evalIfCancelButton(noMoreRoutes, builder);	
		builder.setPositiveButton(R.string.btn_ok, new OnClickListener() {						
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new Thread(new Runnable() {							
					@Override
					public void run() {
						if(mCancelCallback!=null && mNoMoreRoutes)
							mCancelCallback.onServiceCanceled();
						if(callback!=null && !mNoMoreRoutes)
							callback.importConfirmed();			
					}
				}).start();							
			}
		});
	}

	/**
	 * Evalua si se necesita el boton de cancelar
	 * @param noMoreRoutes
	 * @param builder
	 */
	public void evalIfCancelButton(boolean noMoreRoutes,
			AlertDialogPro.Builder builder) {
		if(!noMoreRoutes)
			builder.setNegativeButton(R.string.btn_cancel, new OnClickListener() {						
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(mCancelCallback!=null)
						mCancelCallback.onServiceCanceled();
				}
			});
	}

	/**
	 * Construye el mensaje de la lista de rutas cargadas
	 * @param lockedRoutes
	 * @return
	 */
	public Spanned buildMessage(List<Route> lockedRoutes) {
		return Html.fromHtml(String.format(
				mIsOnlyOne?"La ruta: <b> %s </b> ya fue cargada en otro dispositivo y no se cargará"+
								(mNoMoreRoutes?"":", desea continuar?"):
					"Las rutas:<br/><b> %s </b><br/>Ya fueron cargadas en otros dispositivos y no se cargarán"+
								(mNoMoreRoutes?"":", desea continuar?")
				, MessageListFormatter.fotmatHTMLStringFromObjectList(lockedRoutes, 
						new AttributePicker<String, Route>() {
							@Override
							public String pickAttribute(Route route) {
								return ""+route.getRouteRemoteId();
							}
						})));
	}
	
	public void show()
	{
		builder.show();
	}
}
