package com.elfec.cobranza.presenter.adapter_interfaces;

import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionActionPresenter;
import com.elfec.cobranza.presenter.views.ICollectionActionView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

/**
 * Provee de una abstracci�n del adapter para cobranzas
 * @author drodriguez
 *
 */
public interface ICollectionBaseAdapter {
	/**
	 * Obtiene el titulo para la vista
	 * @return titulo
	 */
	public String getActionTitle();
	/**
	 * Obtiene el id del drawable para el titulo de la vista
	 * @return id del drawable
	 */
	public int getTitleDrawableId();
	/**
	 * Obtiene el texto del boton de la acci�n
	 * @return texto boton
	 */
	public String getButtonText();
	/**
	 * Obtiene el drawable para el boton
	 * @return drawable
	 */
	public Drawable getButtonDrawable();
	/**
	 * Obtiene el titulo que se utilizar� para la lista de recibos
	 * @return titulo
	 */
	public String getReceiptListTitle();
	/**
	 * Obtiene el id de la cadena que se muestra en el titulo de 
	 * los errores de la acci�n
	 * @return id de la string
	 */
	public int getActionErrorsTitleId();
	/**
	 * Obtiene el id de la cadena de mensaje de �xito
	 * @return string id
	 */
	public int getActionSuccessMsgId();
	/**
	 * Obtiene el presenter adecuado para la acci�n de cobranza
	 */
	public CollectionActionPresenter getCollectionPresenter(ICollectionActionView view);
	/**
	 * Obtiene el adapter para la lista de facturas
	 * @param receipts
	 * @return
	 */
	public ArrayAdapter<CoopReceipt> getReceiptAdapter(List<CoopReceipt> receipts);
	/**
	 * Obtiene el context
	 * @return
	 */
	public Context getContext();
}
