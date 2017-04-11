package com.elfec.cobranza.model.events;
/**
 * Evento que se llama cuando se finalizó la descarga de imagenes
 * @author drodriguez
 *
 */
public interface OnImageDownloadFinished {
	/**
	 * Callback al finalizar la descarga de imagenes, ya sea por errores o porque se 
	 * finalizó exitosamente
	 * @param succes
	 */
	public void downloadFinished(boolean succes);
}
