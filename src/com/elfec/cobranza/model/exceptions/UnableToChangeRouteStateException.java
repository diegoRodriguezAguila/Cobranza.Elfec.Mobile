package com.elfec.cobranza.model.exceptions;

import java.util.Locale;

public class UnableToChangeRouteStateException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 6196280538623094721L;

	private int mRouteRemoteId;
	private boolean mIsLock;
	
	public UnableToChangeRouteStateException(int routeRemoteId, boolean isLock) {
		this.mRouteRemoteId = routeRemoteId;
		this.mIsLock = isLock;
	}
	
	@Override
	public String getMessage()
	{
		return String.format(Locale.getDefault(),"Ocurrió un error al realizar el %sbloqueo remoto de la ruta: <b>%d</b>!",
				(mIsLock?"":"des"), mRouteRemoteId);
	}

}
