package com.elfec.cobranza.view.adapters.collection;

import com.elfec.cobranza.presenter.behavior.ICollectionBehavior;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Adapter que sirve para cambiar de la interfaz de cobranza
 * @author drodriguez
 *
 */
public abstract class CollectionBaseAdapter {

	private Context context;
	private ICollectionBehavior collectionBehavior;

	public CollectionBaseAdapter(Context context, ICollectionBehavior collectionBehavior)
	{
		this.context = context;
		this.collectionBehavior = collectionBehavior;
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
	 * Obtiene el comportamiento con el que debe regirse el presentador de la acción de cobranza
	 */
	public ICollectionBehavior getCollectionBehavior()
	{
		return collectionBehavior;
	}

	/**
	 * Obtiene el context
	 * @return
	 */
	public Context getContext() {
		return context;
	}
	
}
