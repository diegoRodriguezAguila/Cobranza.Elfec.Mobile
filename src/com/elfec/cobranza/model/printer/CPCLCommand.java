package com.elfec.cobranza.model.printer;




/**
 * Representa un comando en lenguaje CPCL
 * @author drodriguez
 *
 */
public class CPCLCommand {
	/**
	 * Constante de fin de l�nea
	 */
	private static final String ENDL = "\r\n";
	/**
	 * Constante de espacio
	 */
	private static final String SP = " ";
	/**
	 * Comando de texto
	 */
	private static final String TEXT = "T";
	/**
	 * Comando de texto multilinea
	 */
	private static final String ML = "ML";
	/**
	 * Comando de fin de texto multilinea
	 */
	private static final String ENDML = "ENDML";
	/**
	 * Comando para asignar codificaci�n
	 */
	private static final String ENCODING = "ENCODING";
	/**
	 * Comando set bold
	 */
	private static final String SETBOLD = "SETBOLD";
	/**
	 * Comando de definici�n del espacio entre letras
	 */
	private static final String SPACING = "SETSP";
	/**
	 * Comando de impresi�n
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
	 * Define los tipos de justificaci�n
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
	/**
	 * Define los tipos de codificaci�n del texto
	 * @author drodriguez
	 *
	 */
	public enum Encoding{
		/**
		 * Codificaci�n ASCII
		 */
		ASCII("ASCII"), 
		/**
		 * Codificaci�n UTF-8
		 */
		UTF_8("UTF-8");
		private String valueStr;
		private Encoding(String valueStr)
		{	this.valueStr = valueStr;}
		@Override public String toString(){
			return this.valueStr;
		}
	};
	/**
	 * La fuente que se asigna con sus getters y setters
	 * para usarse en textos sin necesidad de indicar la fuente
	 */
	private String font;
	/**
	 * El comando que est� siendo construido
	 */
	private StringBuilder command;
	
	/**
	 * Inicializa el comando con offset=0 y los valores proporcionados
	 * @param horizontalRes resoluci�n en puntos/pulgada
	 * @param verticalRes resoluci�n en puntos/pulgada
	 * @param height resoluci�n en unidad
	 */
	public CPCLCommand(int horizontalRes, int verticalRes, double height)
	{
		this(0, horizontalRes, verticalRes, height);
	}
	
	/**
	 * Inicializa el comando con los valores proporcionados
	 * @param offset margen que todo el label tendra
	 * @param horizontalRes resoluci�n en puntos/pulgada
	 * @param verticalRes resoluci�n en puntos/pulgada
	 * @param height resoluci�n en unidad
	 */
	public CPCLCommand(int offset, int horizontalRes, int verticalRes, double height)
	{
		command = new StringBuilder("! ").append(offset).append(SP).append(horizontalRes)
				.append(SP).append(verticalRes).append(SP).append(height).append(" 1\r\n");
	}

	/**
	 * Asigna la fuente para utilizar en los textos en los que no se necesita definir la fuente
	 * @param font la fuente, puede ser una interna o externa que se encuentre en la impresora
	 */
	public CPCLCommand setFont(String font) {
		this.font = font;
		return this;
	}

	/**
	 * Todo lo que venga despues se tratar� en las unidades definidas
	 * @param unitType
	 * @return la instancia de este comando
	 */
	public CPCLCommand inUnit(Unit unitType)
	{
		command.append(unitType.toString()).append(ENDL)
			.append("COUNTRY SPAIN").append(ENDL);
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
	 * Asigna el tipo de codificaci�n a utilizar
	 * @param encodingType
	 * @return
	 */
	public CPCLCommand encoding(Encoding encodingType)
	{
		command.append(ENCODING).append(SP).append(encodingType.toString()).append(ENDL);
		return this;
	}
	
	/**
	 * Justifica el texto que provenga despues
	 * @param justifyType
	 * @param end el punto donde acaba la justificaci�n, si es <b>-1</b> 
	 * se utilizar� el tama�o del label por defecto
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
	 * Pone el texto que venga despues con el espaciado extra entre los caracteres definido 
	 * es necesario volver a llamarlo con valor 0 para que vuelva el texto a la normalidad
	 * @param extraSpacing valor del espaciado extra, en la unidad definida
	 * @return la instancia de este comando
	 */
	public CPCLCommand setSpacing(double extraSpacing)
	{
		command.append(SPACING).append(SP).append(extraSpacing).append(ENDL);
		return this;
	}
	
	/**
	 * Agrega un texto para imprimir con la fuente seteada con el m�todo setFont()
	 * @param size tama�o de la fuente
	 * @param x posici�n en la factura en la unidad definida
	 * @param y posici�n en la factura en la unidad definida
	 * @param text el texto que se quiere imprimir
	 * @return la instancia de este comando
	 */
	public CPCLCommand text(int size, double x, double y, String text)
	{
		if(font==null)
			throw new RuntimeException("Debe definir la fuente con setFont() antes de utilizar el comando text() sin par�metros de fuente");
		text(font, size, x, y, text);
		return this;
	}
	
	/**
	 * Agrega un texto para imprimir
	 * @param innerFont la fuente que se usar� para el texto, es una fuente interna de zebra
	 * @param size tama�o de la fuente
	 * @param x posici�n en la factura en la unidad definida
	 * @param y posici�n en la factura en la unidad definida
	 * @param text el texto que se quiere imprimir
	 * @return la instancia de este comando
	 */
	public CPCLCommand text(int innerFont, int size, double x, double y, String text)
	{
		text(""+innerFont, size, x, y, text);
		return this;
	}
	
	/**
	 * Agrega un texto para imprimir
	 * @param font la fuente que se usar� para el texto, puede ser una fuente interna
	 * o una externa que se encuentra en la impresora
	 * @param size tama�o de la fuente
	 * @param x posici�n en la factura en la unidad definida
	 * @param y posici�n en la factura en la unidad definida
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
	 * Agrega un texto para imprimir, con formato de negrilla y extra espaciado definido,
	 * esto solo afecta a esta linea de texto
	 * @param font la fuente que se usar� para el texto, puede ser una fuente interna
	 * o una externa que se encuentra en la impresora
	 * @param size tama�o de la fuente
	 * @param x posici�n en la factura en la unidad definida
	 * @param y posici�n en la factura en la unidad definida
	 * @param boldLevel nivel de bold
	 * @param extraSpacing espaciado extra entre caracteres
	 * @param text el texto que se quiere imprimir
	 * @return la instancia de este comando
	 */
	public CPCLCommand text(String font, int size, double x, double y, double boldLevel, double extraSpacing, String text)
	{
		setBold(boldLevel);
		setSpacing(extraSpacing);
		text(font, size, x, y, text);
		setBold(0);
		setSpacing(0);
		return this;
	}
	
	/**
	 * Agrega un texto para imprimir, con formato de negrilla y extra espaciado definido,
	 * esto solo afecta a esta linea de texto. Utiliza la fuente asignada en setFont()
	 * o una externa que se encuentra en la impresora
	 * @param size tama�o de la fuente
	 * @param x posici�n en la factura en la unidad definida
	 * @param y posici�n en la factura en la unidad definida
	 * @param boldLevel nivel de bold
	 * @param extraSpacing espaciado extra entre caracteres
	 * @param text el texto que se quiere imprimir
	 * @return la instancia de este comando
	 */
	public CPCLCommand text(int size, double x, double y, double boldLevel, double extraSpacing, String text)
	{
		if(font==null)
			throw new RuntimeException("Debe definir la fuente con setFont() antes de utilizar el comando text() sin par�metros de fuente");
		text(font, size, x, y, boldLevel, extraSpacing, text);
		return this;
	}
	
	/**
	 * Agrega un texto multilinea para imprimir
	 * @param lineHeight el alto de cada l�nea en la unidad definida
	 * @param font la fuente que se usar� para el texto, puede ser una fuente interna
	 * o una externa que se encuentra en la impresora
	 * @param size tama�o de la fuente
	 * @param x posici�n en la factura en la unidad definida
	 * @param y posici�n en la factura en las unidad definida
	 * @param textLines las l�neas de texto a imprimir
	 * @return
	 */
	public CPCLCommand multilineText(double lineHeight, String font, int size, double x, double y, String... textLines)
	{
		command.append(ML).append(SP).append(lineHeight).append(ENDL)
			.append(TEXT).append(SP).append(font).append(SP).append(size).append(SP)
				.append(x).append(SP).append(y).append(ENDL);
		
		for (int i = 0; i < textLines.length; i++) {
			command.append(textLines[i]).append(ENDL);
		}
		
		command.append(ENDML).append(ENDL);
		return this;
	}
	
	/**
	 * Agrega un texto multilinea para imprimir utilizando al fuente seteada con setFont
	 * @param lineHeight el alto de cada l�nea en la unidad definida
	 * @param size tama�o de la fuente
	 * @param x posici�n en la factura en la unidad definida
	 * @param y posici�n en la factura en las unidad definida
	 * @param textLines las l�neas de texto a imprimir
	 * @return
	 */
	public CPCLCommand multilineText(double lineHeight, int size, double x, double y, String... textLines)
	{
		if(font==null)
			throw new RuntimeException("Debe definir la fuente con setFont() antes de utilizar el comando text() sin par�metros de fuente");
		multilineText(lineHeight, font, size, x, y, textLines);
		return this;
	}
	
	/**
	 * A�ade el comando de impresi�n
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
			throw new RuntimeException("Debe llamar a print() antes de toString()");
		return command.toString();
	}
}
