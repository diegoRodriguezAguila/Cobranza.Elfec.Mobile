package com.elfec.cobranza.model.exceptions;

import com.elfec.cobranza.helpers.text_format.AccountFormatter;

public class SupplyNotFoundException extends Exception{

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 9140053834055215880L;
	private String nus;
	private String accountNumber;
	private String clientName;
	private String nit;
	
	public SupplyNotFoundException(String nus, String accountNumber, String clientName, String nit) {
		super();
		this.nus = nus;
		this.accountNumber = accountNumber;
		this.clientName = clientName;
		this.nit = nit;
	}

	@Override
	public String getMessage()
	{
		StringBuilder str = new StringBuilder("No se encontró ningún suministro con");
		if(nus!=null && !nus.isEmpty())
			str.append(" NUS: <b>").append(nus).append("</b>,");
		if(accountNumber!=null && !accountNumber.isEmpty())
			str.append(" cuenta: <b>").append(AccountFormatter.formatAccountNumber(accountNumber)).append("</b>,");
		if(clientName!=null && !clientName.isEmpty())
			str.append(" nombre igual o similar a: <b>").append(clientName).append("</b>,");
		if(nit!=null && !nit.isEmpty())
			str.append(" NIT: <b>").append(nit).append("</b>,");
		str = new StringBuilder(str.substring(0, str.length()-1));
		int lastIndex = str.lastIndexOf(",");
		if(lastIndex!=-1)
			str.replace(lastIndex, lastIndex+1, " y");
		return str.toString();
	}
}
