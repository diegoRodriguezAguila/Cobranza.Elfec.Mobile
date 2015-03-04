package com.elfec.cobranza.helpers.text_format;

import java.util.List;

/**
 * Convierte una lista de objetos a una lista aplicable a la consulta IN de SQL
 * con el formato (1234,125534,1234), se utiliza un atributo del objeto definido por la clase
 * @author drodriguez
 *
 */
public class ObjectListToSQL {

	/**
	 * Convierte la lista de objetos a la cadena utilizable en una consulta IN
	 * @param objectList
	 * @param picker
	 * @return
	 */
	public static <T> String convertToSQL(List<T> objectList, AttributePicker<T> picker)
	{
		StringBuilder query = new StringBuilder("(");
		for(T obj : objectList)
		{
			query.append(picker.pickString(obj)).append(",");
		}
		query.setCharAt(query.length()-1, ')');
		return query.toString();
	}
	
	/**
	 * Convierte la lista de objetos a la cadena utilizable en una consulta IN, utiliza toString para obtener la cadena
	 * @param objectList
	 * @return
	 */
	public static <T> String convertToSQL(List<T> objectList)
	{
		return convertToSQL(objectList, new AttributePicker<T>() {
			@Override
			public String pickString(T object) {
				return object.toString();
			}
		});
	}
	/**
	 * Sirve para que el converToSQL pueda elegir el atributo que se quiere poner en la lista sql
	 * @author drodriguez
	 *
	 * @param <T>
	 */
	public static interface AttributePicker<T>
	{
		/**
		 * Obtiene un atributo especifico del objeto
		 * @param object
		 * @return
		 */
		public String pickString(T object);
	}
}
