package com.elfec.cobranza.model.interfaces;

/**
 * Interfaz que sirve para llamar a los diferentes importers de la
 * informaci�n que solo se requiere una vez
 * @author drodriguez
 *
 */
public interface OnceRequiredDataImportCaller extends ImportCaller{
	/**
	 * Funci�n que indica si la informaci�n solicitada ya se hab�a importado
	 * @return
	 */
	public boolean isAlreadyImported();
	/**
	 * Asigna si es que se import� correctamente la informaci�n
	 * @param successfullyImport
	 */
	public void setImportationResult(boolean successfullyImport);
}
