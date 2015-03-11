package com.elfec.cobranza.helpers.text_format;

import java.util.Locale;

/**
 * Se encarga de formatear cadenas
 * @author drodriguez
 *
 */
public class TextFormater {

	/**
	 * Convierte una cadena a su versión con la primera mayuscula y luego minúsculas para cada palabra,
	 * las palabras tienen que tener el tamaño minimo para ser formateadas, caso contrario
	 * se las ignora
	 * @param line
	 * @param minSize
	 * @return
	 */
	public static String capitalize(String line, int minSize)
	{
		StringBuilder result=new StringBuilder();
		String[] words=line.split(" ");
		for(String word:words)
		{
			if(word.length()>minSize)
				result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase(Locale.getDefault()));
			else result.append(word);
			if(word!=words[words.length-1])
				result.append(" ");
		}
		return result.toString();
	}
	
	/**
	 * Convierte una cadena a su versión con la primera mayuscula y luego minúsculas para cada palabra, sin importar su tamaño
	 * se las ignora
	 * @param line
	 * @param minSize
	 * @return
	 */
	public static String capitalize(String line)
	{
		return capitalize(line, 0);
	}
}
