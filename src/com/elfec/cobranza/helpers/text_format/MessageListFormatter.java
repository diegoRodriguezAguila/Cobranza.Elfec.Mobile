package com.elfec.cobranza.helpers.text_format;

import java.util.List;

import android.text.Html;
import android.text.Spanned;

/**
 * Formatea las listas de errores
 * @author drodriguez
 *
 */
public class MessageListFormatter {

	/**
	 * Formatea una lista de errores una lista de errores en html
	 * @param errors
	 * @return
	 */
	public static Spanned fotmatHTMLFromErrors(List<Exception> errors)
	{
		StringBuilder str = new StringBuilder();
		int size = errors.size();
		if(size==1)
			return Html.fromHtml(str.append(errors.get(0).getMessage()).toString());
		for (int i = 0; i < size; i++) {
			str.append("● ").append(errors.get(i).getMessage());
			str.append((i<size-1?"<br/>":""));
		}
		return Html.fromHtml(str.toString());
	}
	
	/**
	 * Formatea una lista de mensajes una lista (en cadena) en html
	 * @param messages
	 * @return
	 */
	public static Spanned fotmatHTMLFromStringList(List<String> messages)
	{
		StringBuilder str = new StringBuilder();
		int size = messages.size();
		if(size==1)
			return Html.fromHtml(str.append(messages.get(0)).toString());
		for (int i = 0; i < size; i++) {
			str.append("● ").append(messages.get(i));
			str.append((i<size-1?"<br/>":""));
		}
		return Html.fromHtml(str.toString());
	}
}
