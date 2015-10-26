package com.elfec.cobranza.model.events;

/**
 * Listener para cuando se cambia el estado del bluetooth
 * @author drodriguez
 *
 */
public interface BluetoothStateListener {
	/**
	 * Se ejecuta cuando el bluetooth se enciende
	 */
	public void onBluetoothTurnedOn();
	/**
	 * Se ejecuta cuando el bluetooth se apaga
	 */
	public void onBluetoothTurnedOff();
}
