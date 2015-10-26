package com.elfec.cobranza.model.enums;

/**
 * Describe los estados de un usuario
 * @author drodriguez
 *
 */
public enum UserStatus {
	/**
	 * Estado de usuario inactivo
	 */
	INACTIVE,
	/**
	 * Estado de usuario activo
	 */
	ACTIVE;
	/**
	 * Obtiene el estado del usuario equivalente al short provisto
	 * @param status
	 * @return
	 */
	public static UserStatus get(short status)
	{
		return UserStatus.values()[status];
	}
	
	/**
	 * Convierte el estado del usuario actual a su short equivalente
	 * @return Short equivalente al estado
	 */
	public short toShort()
	{
		return (short)this.ordinal();
	}
}
