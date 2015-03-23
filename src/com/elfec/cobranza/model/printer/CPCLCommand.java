package com.elfec.cobranza.model.printer;


/**
 * Representa un comando en lenguaje CPCL
 * @author drodriguez
 *
 */
public class CPCLCommand {
	/**
	 * Constante de fin de línea
	 */
	private static final String ENDL = "\r\n";
	/**
	 * Constante de espacio
	 */
	private static final String SP = " ";
	/**
	 * Comando de texto
	 */
	private static final String TEXT = "TEXT";
	/**
	 * Comando set bold
	 */
	private static final String SETBOLD = "SETBOLD";
	/**
	 * Comando de impresión
	 */
	private static final String PRINT = "PRINT";
	/**
	 * Define las unidades del lenguaje
	 * @author drodriguez
	 *
	 */
	public enum Unit{
		/**
		 * Centimetros
		 */
		IN_CENTIMETERS("IN-CENTIMETERS"), 
		/**
		 * Milimetros
		 */
		IN_MILIMETERS("IN-MILIMETERS");
		private String valueStr;
		private Unit(String valueStr)
		{	this.valueStr = valueStr;}
		@Override public String toString(){
			return this.valueStr;
		}
	};
	/**
	 * Define los tipos de justificación
	 * @author drodriguez
	 *
	 */
	public enum Justify{
		/**
		 * Justificado al centro
		 */
		CENTER("CENTER"), 
		/**
		 * Justificado a la izquierda
		 */
		LEFT("LEFT"),
		/**
		 * Justificado a la derecha
		 */
		RIGHT("RIGHT");
		private String valueStr;
		private Justify(String valueStr)
		{	this.valueStr = valueStr;}
		@Override public String toString(){
			return this.valueStr;
		}
	};
	
	private StringBuilder command;
	
	/**
	 * Inicializa el comando con offset=0 y los valores proporcionados
	 * @param horizontalRes resolución en puntos/pulgada
	 * @param verticalRes resolución en puntos/pulgada
	 * @param height resolución en unidad
	 */
	public CPCLCommand(int horizontalRes, int verticalRes, double height)
	{
		this(0, horizontalRes, verticalRes, height);
	}
	
	/**
	 * Inicializa el comando con los valores proporcionados
	 * @param offset margen que todo el label tendra
	 * @param horizontalRes resolución en puntos/pulgada
	 * @param verticalRes resolución en puntos/pulgada
	 * @param height resolución en unidad
	 */
	public CPCLCommand(int offset, int horizontalRes, int verticalRes, double height)
	{
		command = new StringBuilder("! ").append(offset).append(SP).append(horizontalRes)
				.append(SP).append(verticalRes).append(SP).append(height).append(" 1\r\n");
	}
	
	/**
	 * Todo lo que venga despues se tratará en las unidades definidas
	 * @param unitType
	 * @return la instancia de este comando
	 */
	public CPCLCommand inUnit(Unit unitType)
	{
		command.append(unitType.toString()).append(ENDL);
		return this;
	}
	
	/**
	 * Justifica el texto que provenga despues
	 * @param justifyType
	 * @return la instancia de este comando
	 */
	public CPCLCommand justify(Justify justifyType)
	{
		justify(justifyType, -1);
		return this;
	}
	
	/**
	 * Justifica el texto que provenga despues
	 * @param justifyType
	 * @param end el punto donde acaba la justificación, si es <b>-1</b> 
	 * se utilizará el tamaño del label por defecto
	 * @return la instancia de este comando
	 */
	public CPCLCommand justify(Justify justifyType, double end)
	{
		command.append(justifyType.toString())
			.append(end!=-1?(SP+end):"")
			.append(ENDL);
		return this;
	}
	
	/**
	 * Pone el texto que venga despues al nivel de bold definido, para que el texto sea normal 
	 * es necesario volver a llamarlo con valor 0
	 * @param value valor de bold, en la unidad definida
	 * @return la instancia de este comando
	 */
	public CPCLCommand setBold(double value)
	{
		command.append(SETBOLD).append(SP).append(value).append(ENDL);
		return this;
	}
	
	/**
	 * 
	 * @param font la fuente que se usará para el texto, puede ser una fuente interna
	 * o una externa que se encuentra en la impresora
	 * @param size tamaño de la fuente
	 * @param x posición en la factura en las unidades definidas
	 * @param y posición en la factura en las unidades definidas
	 * @param text el texto que se quiere imprimir
	 * @return la instancia de este comando
	 */
	public CPCLCommand text(String font, int size, double x, double y, String text)
	{
		command.append(TEXT).append(SP).append(font).append(SP).append(size)
			.append(SP).append(x).append(SP).append(y).append(SP).append(text).append(ENDL);
		return this;
	}
	
	/**
	 * Añade el comando de impresión
	 * @return la instancia de este comando
	 */
	public CPCLCommand print()
	{
		command.append(PRINT).append(ENDL);
		return this;
	}
	
	@Override 
	public String toString(){
		if(command.lastIndexOf(PRINT)==-1)
			throw new RuntimeException("Debe llamar a print antes de toString()");
		return command.toString();
	}
}
