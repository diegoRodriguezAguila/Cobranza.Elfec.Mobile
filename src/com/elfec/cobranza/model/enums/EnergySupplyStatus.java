package com.elfec.cobranza.model.enums;

/**
 * Define los estados de los suministros
 * @author drodriguez
 *
 */
public enum EnergySupplyStatus {
	/**
	 * <b>1</b> Estado normal
	 */
	NORMAL("Normal"),
	/**
	 * <b>2</b> Estado que indica que el tramite para conexi�n del suministro
	 * ya fue hecho
	 */
	CONNECTION_PENDING("Pendiente de conexi�n"),
	/**
	 * <b>3</b> Estado que indica que se va a desconectar el suministro de energ�a
	 */
	DISCONNECTION_PENDING("Pendiente de desconexi�n"),
	/**
	 * <b>4</b> Estado de que se cort� el suministro de energ�a por falta de pago
	 */
	SUSPENDED("Suspendido"),
	/**
	 * <b>5</b> Estado que indica que el suministro se di� de baja
	 */
	DISMISSED("Dado de baja"),
	/**
	 * <b>6</b> Estado que indica que el suministro es incrobrable
	 */
	IRRECOVERABLE("Incobrable"),
	/**
	 * <b>7</b> Estado de que se desconect� el suministro
	 */
	DISCONNECTED("Cortado por mora");
	
	private String string;
	private EnergySupplyStatus(String string)
	{
		this.string = string;
	}
	@Override
	public String toString() {
        return string;
    }
	/**
	 * Obtiene el estado del suministro, equivalente al short provisto
	 * @param status
	 * @return
	 */
	public static EnergySupplyStatus get(short status)
	{
		return EnergySupplyStatus.values()[status-1];
	}
	
	/**
	 * Convierte el estado del suministro a su short equivalente
	 * @return Short equivalente al estado
	 */
	public short toShort()
	{
		return (short)(this.ordinal()+1);
	}
}
