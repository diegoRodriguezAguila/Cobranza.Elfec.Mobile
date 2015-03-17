package com.elfec.cobranza.helpers.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import com.elfec.cobranza.model.CoopReceipt;

/**
 * Clase de ayuda para labores sobre recibos
 * @author drodriguez
 *
 */
public class ReceiptsCounter {
	private static NumberFormat nf;
	static
	{
		nf = DecimalFormat.getInstance();
		DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
		customSymbol.setDecimalSeparator(',');
		customSymbol.setGroupingSeparator('.');
		((DecimalFormat)nf).setDecimalFormatSymbols(customSymbol);
		nf.setGroupingUsed(true);
	}
	/**
	 * Calcula el monto total de una lista de recibos
	 * @param receipts
	 * @return
	 */
	public static BigDecimal countTotalAmount(List<CoopReceipt> receipts)
	{
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		for(CoopReceipt receipt : receipts)
		{
			totalAmount = totalAmount.add(receipt.getTotalAmount());
		}
		return totalAmount;
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
