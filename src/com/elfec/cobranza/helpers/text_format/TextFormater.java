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
			{
				int start = 0;
				for (int i = 0; i < word.length(); i++) {
					if((word.charAt(i)>='a' && word.charAt(i)<='z') || (word.charAt(i)>='A' && word.charAt(i)<='Z'))
					{
						start = i;
						break;
					}
				}
				result.append(word.substring(0, start).toLowerCase(Locale.getDefault()))
				.append(Character.toUpperCase(word.charAt(start)))
				.append(word.substring(start+1).toLowerCase(Locale.getDefault()));
			}
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
