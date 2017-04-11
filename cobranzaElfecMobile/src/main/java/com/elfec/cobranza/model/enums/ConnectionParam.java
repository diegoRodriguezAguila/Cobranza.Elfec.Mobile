package com.elfec.cobranza.model.enums;

/**
 * Se utiliza para las llaves de los parámetros de la configuración de
 * conexión a la base de datos
 * @author drodriguez
 *
 */
public enum ConnectionParam
{
	/**
	 * Ip del host de conexión
	 */
	IP("ip"),
	/**
	 * Puerto de conexión
	 */
	PORT("port"),
	/**
	 * Servicio Oracle de conexión
	 */
	SERVICE("service"),
	/**
	 * Rol que debe habilitar la aplicación
	 */
	ROLE("role"),
	/**
	 * La contraseña del rol 
	 */
	PASSWORD("password");
	private String value;
	
	private ConnectionParam(String value)
	{
		this.value = value;
	}
	
	@Override public String toString()
	{
		return this.value;
	}
}