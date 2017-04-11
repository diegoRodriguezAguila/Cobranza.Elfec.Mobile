package com.elfec.cobranza.model.exceptions;
/**
 * Excepción que se lanza cuando ocurrió un error al realizar la exportación de información
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
		return "Ocurrió un error al exportar uno de los registros! Información extra: "+registryInfo;
	}

}
