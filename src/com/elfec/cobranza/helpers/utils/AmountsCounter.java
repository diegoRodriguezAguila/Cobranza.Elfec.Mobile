package com.elfec.cobranza.helpers.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.elfec.cobranza.helpers.text_format.AttributePicker;

/**
 * Clase de ayuda para labores sobre recibos
 * @author drodriguez
 *
 */
public class AmountsCounter {
	private static NumberFormat nf;
	static
	{
		nf = DecimalFormat.getInstance(Locale.getDefault());
		DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
		customSymbol.setDecimalSeparator('.');
		customSymbol.setGroupingSeparator(',');
		((DecimalFormat)nf).setDecimalFormatSymbols(customSymbol);
		nf.setGroupingUsed(true);
	}
	/**
	 * Calcula el monto total de una lista de recibos
	 * @param receipts
	 * @return
	 */
	public static <T>BigDecimal countTotalAmount(List<T> objects, AttributePicker<BigDecimal, T> amountGetter)
	{
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		for(T obj : objects)
		{
			totalAmount = totalAmount.add(amountGetter.pickAttribute(obj));
		}
		return totalAmount;
	}
	
	/**
	 * Obtiene un bigdecimal y la formatea
	 * @param amount
	 * @return el bigdecimal formateado
	 */
	public static String formatBigDecimal(BigDecimal amount)
	{
		nf.setMinimumFractionDigits(2);
		String formatedBigDecimal = nf.format(amount.setScale(2, RoundingMode.FLOOR));
		nf.setMinimumFractionDigits(0);
		return formatedBigDecimal;
	}
	
	/**
	 * Obtiene la parte entera de un bigdecimal y la formatea
	 * @param amount
	 * @return
	 */
	public static String formatIntAmount(BigDecimal amount)
	{
		return nf.format(amount.toBigInteger().doubleValue());
	}
	
	/**
	 * Obtiene la parte decimal de un bigdecimal y lo formatea
	 * @param amount
	 * @return
	 */
	public static String formatDecimalAmount(BigDecimal amount)
	{
		String decimal = (amount.remainder(BigDecimal.ONE).multiply(new BigDecimal("100"))
				.setScale(0, RoundingMode.CEILING).toString());
		return decimal.equals("0")?"00":decimal;
	}
	
	/**
	 * Formatea un entero
	 * @param quantity
	 * @return
	 */
	public static String formatInteger(int quantity)
	{
		return nf.format(quantity);
	}
}
