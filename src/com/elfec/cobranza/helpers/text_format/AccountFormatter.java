package com.elfec.cobranza.helpers.text_format;

/**
 * Clase con métodos para formatear una número cuenta
 * @author drodriguez
 *
 */
public class AccountFormatter {

	/**
	 * Formatea una cuenta sin guiones a una con guiones con tamaño de 10 digitos fijo
	 * @param accountNumber ej. 530048208
	 * @return ej. 05-300-582-08
	 */
	public static String formatAccountNumber(String accountNumber)
	{
		StringBuilder account=new StringBuilder(accountNumber);
		if(account.length()==9)
		{
			account.insert(0, "0");
		}
		account.insert(2, "-");
		account.insert(6, "-");
		account.insert(10, "-");
		return account.toString();
	}
	/**
	 * Desformatea una cuenta con guiones de tamaño fijo de 10 digitos a 
	 * una cadena de numeros sin ceros por delante
	 * @param accountNumber ej. 01-235-668-24
	 * @return ej 123566824
	 */
	public static String unformatAccountNumber(String accountNumber)
	{
		if(accountNumber.length()>0 && accountNumber.charAt(0)=='0')
			accountNumber = accountNumber.substring(1, accountNumber.length());
		return accountNumber.replace("-", "").trim();
	}
}
