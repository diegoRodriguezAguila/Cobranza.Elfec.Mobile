package com.elfec.cobranza.helpers.text_format;
/**
 * Sirve para que el converToSQL pueda elegir el atributo que se quiere poner en la lista sql
 * @author drodriguez
 *
 * @param <RT> tipo del atributo que se selecciona
 * @param <OT> tipo del objeto del que se saca el atributo
 */
public interface AttributePicker<RT, OT> {
	/**
	 * Obtiene un atributo especifico del objeto
	 * @param object
	 * @return
	 */
	public RT pickAttribute(OT object);
}