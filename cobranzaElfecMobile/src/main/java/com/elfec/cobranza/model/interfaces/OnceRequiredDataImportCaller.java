package com.elfec.cobranza.model.interfaces;

/**
 * Interfaz que sirve para llamar a los diferentes importers de la
 * información que solo se requiere una vez
 * @author drodriguez
 *
 */
public interface OnceRequiredDataImportCaller extends ImportCaller{
	/**
	 * Función que indica si la información solicitada ya se había importado
	 * @return
	 */
	public boolean isAlreadyImported();
	/**
	 * Asigna si es que se importó correctamente la información
	 * @param successfullyImport
	 */
	public void setImportationResult(boolean successfullyImport);
}
