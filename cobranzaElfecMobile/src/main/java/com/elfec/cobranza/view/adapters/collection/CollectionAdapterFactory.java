package com.elfec.cobranza.view.adapters.collection;

import android.content.Context;

/**
 * Administra las instancias de los adapters de cobranza
 * @author drodriguez
 *
 */
public class CollectionAdapterFactory {
	public static final int COLLECTION_PAYMENT = 0;
	public static final int COLLECTION_ANNULMENT = 1;
	/**
	 * Obtiene la instancia del adaptador de cobranzas
	 * @param key
	 * @param context
	 * @return
	 */
	public static CollectionBaseAdapter instance(int key, Context context)
	{
		switch(key)
		{
			case COLLECTION_PAYMENT:
				return new CollectionPaymentAdapter(context);
			case COLLECTION_ANNULMENT:
				return new CollectionAnnulmentAdapter(context);
			default:
				return new CollectionPaymentAdapter(context);
		}
	}
}
