package com.elfec.cobranza.view.adapters.collection;


import android.content.Context;

import com.elfec.cobranza.presenter.adapter_interfaces.ICollectionBaseAdapter;

/**
 * Adapter que sirve para cambiar de la interfaz de cobranza
 * @author drodriguez
 *
 */
public abstract class CollectionBaseAdapter implements ICollectionBaseAdapter{

	private Context context;

	public CollectionBaseAdapter(Context context)
	{
		this.context = context;
	}
	
	@Override
	public Context getContext() {
		return context;
	}
	
}
