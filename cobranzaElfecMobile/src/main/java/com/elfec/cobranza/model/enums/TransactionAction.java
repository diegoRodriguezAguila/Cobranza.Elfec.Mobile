package com.elfec.cobranza.model.enums;


/**
 * Define los distintos tipos de acciones de las transacciones
 * @author drodriguez
 *
 */
public enum TransactionAction {
	PAYMENT("COBRANZA"),
	ANNULMENT("ANULACION_COBRANZA");
	private String string;
	private TransactionAction(String string)
	{
		this.string = string;
	}
	
	@Override
	public String toString() {
        return string;
    }
	
	/**
	 * Obtiene la TransactionAction correspondiente a la cadena provista
	 * a ninguna accion definida
	 * @param actionAsString
	 * @return TransactionAction correspondiente a la cadena, null si la cadena no corresponde 
	 */
	public static TransactionAction get(String actionAsString)
	{
		TransactionAction[] actions = TransactionAction.values();
		for (int i = 0; i < actions.length; i++) {
			if(actions[i].toString().equals(actionAsString))
				return actions[i];
		}
		return null;
	}
}
