package com.elfec.cobranza.presenter.adapter_interfaces;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

import com.elfec.cobranza.model.CoopReceipt;
import com.elfec.cobranza.presenter.CollectionActionPresenter;
import com.elfec.cobranza.presenter.views.ICollectionActionView;

import java.util.List;

/**
 * Provee de una abstracción del adapter para cobranzas
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
	 * Obtiene el texto del boton de la acción
	 * @return texto boton
	 */
	public String getButtonText();
	/**
	 * Obtiene el drawable para el boton
	 * @return drawable
	 */
	public Drawable getButtonDrawable();
	/**
	 * Obtiene el titulo que se utilizará para la lista de recibos
	 * @return titulo
	 */
	public String getReceiptListTitle();
	/**
	 * Obtiene el id de la cadena que se muestra en el titulo de 
	 * los errores de la acción
	 * @return id de la string
	 */
	public int getActionErrorsTitleId();
	/**
	 * Obtiene el id de la cadena de mensaje de éxito
	 * @return string id
	 */
	public int getActionSuccessMsgId();
	/**
	 * Obtiene el presenter adecuado para la acción de cobranza
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
	/**
	 * Indica si se tiene o no que mostrar la opción del menu de elegir una impresora
	 * @return
	 */
	public boolean hasToShowPickPrinter();
}
