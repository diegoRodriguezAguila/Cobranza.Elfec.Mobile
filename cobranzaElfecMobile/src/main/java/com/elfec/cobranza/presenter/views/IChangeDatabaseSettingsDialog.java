package com.elfec.cobranza.presenter.views;

import java.util.List;
/**
 * Abstracción del dialogo de configuración de conexión a la base de datos
 * @author drodriguez
 *
 */
public interface IChangeDatabaseSettingsDialog {
	
	public String getIp();
	public String getIpValidationRules();
	public void setIp(String ip);
	public void setIpFieldErrors(List<String> errors);
	
	public String getPort();
	public String getPortValidationRules();
	public void setPort(String port);
	public void setPortFieldErrors(List<String> errors);
	
	public String getService();
	public String getServiceValidationRules();
	public void setService(String service);
	public void setServiceFieldErrors(List<String> errors);
	
	public String getRole();
	public String getRoleValidationRules();
	public void setRole(String role);
	public void setRoleFieldErrors(List<String> errors);
	
	public String getRolePassword();
	
	/**
	 * Notifica al usuario que existen errores en los campos
	 */
	public void notifyErrorsInFields();
	/**
	 * Muestra al usuario errores en el guardado de campos
	 */
	public void showSaveErrors(List<Exception> errors);
	
	/**
	 * Notifica al usuario que se guardaron las configuraciones exitosamente
	 */
	public void notifySettingsSavedSuccesfully();
}
