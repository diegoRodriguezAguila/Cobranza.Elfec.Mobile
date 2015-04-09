package com.elfec.cobranza.model.enums;
/**
 * Describe los estados de la carga/descarga de datos
 * @author drodriguez
 *
 */
public enum DataExchangeStatus {
	/**
	 * Estado que indica que una ruta ha sido importada del servidor al teléfono
	 */
	IMPORTED,
	/**
	 * Estado que indica que una ruta ya ha sido exportada del telefono al servidor
	 */
	EXPORTED;
	/**
	 * Obtiene el estado de carga/descarga de datos, equivalente al short provisto
	 * @param status
	 * @return
	 */
	public static DataExchangeStatus get(short status)
	{
		return DataExchangeStatus.values()[status-1];
	}
	
	/**
	 * Convierte el estado de carga/descarga de datos a su short equivalente
	 * @return Short equivalente al estado
	 */
	public short toShort()
	{
		return (short) (this.ordinal()+1);
	}
}
