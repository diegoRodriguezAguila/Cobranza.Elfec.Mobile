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
			if(word.length()>3)
				result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase(Locale.getDefault()));
			else result.append(word);
			if(word!=words[words.length-1])
				result.append(" ");
		}
		return result.toString();
	}
}
