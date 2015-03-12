package com.elfec.cobranza.model.exceptions;

public class SupplyNotFoundException extends Exception{

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 9140053834055215880L;
	private String nus;
	private String accountNumber;
	
	public SupplyNotFoundException(String nus, String accountNumber) {
		super();
		this.nus = nus;
		this.accountNumber = accountNumber;
	}

	@Override
	public String getMessage()
	{
		StringBuilder str = new StringBuilder("No se encontró ningun suministro ");
		if((nus!=null && !nus.isEmpty()) && (accountNumber!=null && !accountNumber.isEmpty()))
			str.append("con el NUS: <b>").append(nus).append("</b> y la cuenta: <b>").append(accountNumber).append("</b> ");
		if((nus!=null && !nus.isEmpty()) && (accountNumber==null || accountNumber.isEmpty()))
			str.append("con el NUS: <b>").append(nus).append("</b> ");
		if((nus==null || nus.isEmpty()) && (accountNumber!=null && !accountNumber.isEmpty()))
			str.append("con la cuenta: <b>").append(accountNumber).append("</b> ");
		return str.append("que coincida con los términos de búsqueda!").toString();
	}
}
