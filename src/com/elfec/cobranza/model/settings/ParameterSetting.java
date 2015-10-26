package com.elfec.cobranza.model.settings;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Define una configuración de la tabla de parámetros
 * @author drodriguez
 *
 */
@Table(name = "ParameterSettings")
public class ParameterSetting extends Model {
	@Column(name = "Key", notNull=true)
	private String key;	
	@Column(name = "Value")
	private String value;
	
	public ParameterSetting() {
		super();
	}
	
	public ParameterSetting(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	public ParameterSetting(String key, int value) {
		super();
		this.key = key;
		this.value = ""+value;
	}
	
	public ParameterSetting(String key, short value) {
		super();
		this.key = key;
		this.value = ""+value;
	}
	
	public ParameterSetting(String key, DateTime value) {
		super();
		this.key = key;
		this.value = value.toString("yyyy-MM-dd HH:mm:ss.S");
	}

	public String getKey() {
		return key;
	}
	
	public String getStringValue() {
		return value;
	}

	public int getIntValue() {
		return Integer.parseInt(value);
	}
	
	public short getShortValue() {
		return Short.parseShort(value);
	}
	
	public DateTime getDateTimeValue() {
		return DateTime.parse(value, 
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S"));
	}
}
