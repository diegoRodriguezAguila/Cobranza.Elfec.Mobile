package com.elfec.cobranza.model.events;

import org.joda.time.DateTime;

/**
 * Evento que se ejecuta cuando se elijen fechas
 * @author drodriguez
 *
 */
public interface DatePickListener {

	/**
	 * Se ejecuta al elegirse fechas
	 * @param dates
	 */
	public void onDatePicked(DateTime... dates);
}
