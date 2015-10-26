package com.elfec.cobranza.model.enums;
/**
 * Describe los estados del dispositivo actual
 * @author drodriguez
 *
 */
public enum DeviceStatus {
	/**
	 * Estado de dispositivo inhabilitado para el uso del sistema
	 */
	UNABLED,
	/**
	 * Estado de dispositivo habilitado para el uso del sistema
	 */
	ENABLED;
	/**
	 * Obtiene el estado del dispositivo, equivalente al short provisto
	 * @param status
	 * @return
	 */
	public static DeviceStatus get(short status)
	{
		return DeviceStatus.values()[status];
	}
	
	/**
	 * Convierte el estado del dispositivo a su short equivalente
	 * @return Short equivalente al estado
	 */
	public short toShort()
	{
		return (short)this.ordinal();
	}
}
