package com.elfec.cobranza.helpers.text_format;

import java.util.Locale;

/**
 * Se encarga de formatear cadenas
 * @author drodriguez
 *
 */
public class TextFormater {

	/**
	 * Convierte una cadena a su versión con la primera mayuscula y luego minúsculas para cada palabra
	 * @param line
	 * @return
	 */
	public static String capitalize(String line)
	{
		StringBuilder result=new StringBuilder();
		String[] words=line.split(" ");
		for(String word:words)
		{
			if(word.length()>0)
			result.append(Character.toUpperCase(word.charAt(0))+word.substring(1).toLowerCase(Locale.getDefault())+" ");
		}
		return result.toString();
	}
}
