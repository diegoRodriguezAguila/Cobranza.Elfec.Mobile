package com.elfec.cobranza.helpers.text_format;

import java.util.List;

import android.text.Html;
import android.text.Spanned;

/**
 * Formatea las listas de errores
 * @author drodriguez
 *
 */
public class ErrorListFormatter {

	/**
	 * Formatea una lista de errores una lista de errores en html
	 * @param errors
	 * @return
	 */
	public Spanned fotmatHTMLFromErrors(List<Exception> errors)
	{
		StringBuilder str = new StringBuilder("<font color='#006086'><b>");
		int size = errors.size();
		if(size==1)
			return Html.fromHtml(str.append(errors.get(0).getMessage()).append("</b></font>").toString());
		for (int i = 0; i < size; i++) {
			str.append("● ").append(errors.get(i).getMessage());
			str.append((i<size-1?"<br/>":""));
		}
		str.append("</b></font>");
		return Html.fromHtml(str.toString());
	}
}
