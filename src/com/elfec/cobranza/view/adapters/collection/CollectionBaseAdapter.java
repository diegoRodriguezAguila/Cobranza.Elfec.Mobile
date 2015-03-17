package com.elfec.cobranza.view.adapters.collection;


import com.elfec.cobranza.presenter.CollectionActionPresenter;
import com.elfec.cobranza.presenter.views.ICollectionActionView;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Adapter que sirve para cambiar de la interfaz de cobranza
 * @author drodriguez
 *
 */
public abstract class CollectionBaseAdapter {

	private Context context;

	public CollectionBaseAdapter(Context context)
	{
		this.context = context;
	}
	
	/**
	 * Obtiene el titulo para la vista
	 * @return titulo
	 */
	public abstract String getActionTitle();
	/**
	 * Obtiene el id del drawable para el titulo de la vista
	 * @return id del drawable
	 */
	public abstract int getTitleDrawableId();
	/**
	 * Obtiene el texto del boton de la acción
	 * @return texto boton
	 */
	public abstract String getButtonText();
	/**
	 * Obtiene el drawable para el boton
	 * @return drawable
	 */
	public abstract Drawable getButtonDrawable();
	/**
	 * Obtiene el titulo que se utilizará para la lista de recibos
	 * @return titulo
	 */
	public abstract String getReceiptListTitle();
	/**
	 * Obtiene el id de la cadena que se muestra en el titulo de 
	 * los errores de la acción
	 * @return id de la string
	 */
	public abstract int getActionErrorsTitleId();
	/**
	 * Obtiene el presenter adecuado para la acción de cobranza
	 */
	public abstract CollectionActionPresenter getCollectionPresenter(ICollectionActionView view);

	/**
	 * Obtiene el context
	 * @return
	 */
	public Context getContext() {
		return context;
	}
	
}
