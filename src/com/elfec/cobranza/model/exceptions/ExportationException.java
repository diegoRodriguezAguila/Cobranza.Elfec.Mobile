package com.elfec.cobranza.model.exceptions;
/**
 * Excepci�n que se lanza cuando ocurri� un error al realizar la exportaci�n de informaci�n
 * @author drodriguez
 *
 */
public class ExportationException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -6650704680138905132L;
	private String registryInfo;
	public ExportationException(String registryInfo)
	{
		this.registryInfo = registryInfo;
	}
	@Override
	public String getMessage()
	{
		return "Ocurri� un error al exportar uno de los registros! Informaci�n extra: "+registryInfo;
	}

}
