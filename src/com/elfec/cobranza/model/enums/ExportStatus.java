package com.elfec.cobranza.model.enums;

public enum ExportStatus {
	/**
	 * Estado de que NO se realiz� la exportaci�n
	 */
	NOT_EXPORTED,
	/**
	 * Estado de que si se export�
	 */
	EXPORTED;
	/**
	 * Obtiene el estado de exportaci�n, equivalente al short provisto
	 * @param status
	 * @return
	 */
	public static ExportStatus get(short status)
	{
		return ExportStatus.values()[status];
	}
	
	/**
	 * Convierte el estado de exportaci�n a su short equivalente
	 * @return Short equivalente al estado
	 */
	public short toShort()
	{
		return (short)this.ordinal();
	}
}
