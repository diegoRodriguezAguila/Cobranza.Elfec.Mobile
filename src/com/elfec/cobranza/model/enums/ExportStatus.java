package com.elfec.cobranza.model.enums;

public enum ExportStatus {
	/**
	 * Estado de que NO se realizó la exportación
	 */
	NOT_EXPORTED,
	/**
	 * Estado de que si se exportó
	 */
	EXPORTED;
	/**
	 * Obtiene el estado de exportación, equivalente al short provisto
	 * @param status
	 * @return
	 */
	public static ExportStatus get(short status)
	{
		return ExportStatus.values()[status];
	}
	
	/**
	 * Convierte el estado de exportación a su short equivalente
	 * @return Short equivalente al estado
	 */
	public short toShort()
	{
		return (short)this.ordinal();
	}
}
