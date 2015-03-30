package com.elfec.cobranza.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

/**
 * Clase que sirve para acceder a los archivos de la aplicación, ya sean del almacenamiento interno u externo
 * @author drodriguez
 *
 */
public class FileManager {

	/**
	 * Nombre del directorio de la aplicación
	 */
	public static final String APP_DIR = "Elfec Cobranza Móvil";
	
	/**
	 * Obtiene el directorio de almacenamiento externo 
	 * @return File
	 */
	public static File getExternalAppDirectory()
	{
		File appDir = new File(Environment.getExternalStorageDirectory()+File.separator+APP_DIR);
	    if (! appDir.exists()){
	        if (! appDir.mkdirs()){
	            Log.e("App Directory failed","Failed to create directory: "+APP_DIR);
	            return null;
	        }
	    }
	    return appDir;
	}
	
	/**
	 * Obtiene el FileInputStream del archivo solicitado del almacenamiento externo
	 * @param fileName
	 * @param createIfNotExists true si se debe crear si no existe el archivo
	 * @return el FileInputStream del archivo solicitado, si no existe null o el archivo vacio
	 */
	public static FileInputStream getExternalFileInput(String fileName, boolean createIfNotExists)
	{
		File file = new File(FileManager.getExternalAppDirectory().getPath()+File.separator+fileName);
		try 
		{
			if(createIfNotExists)
				file.createNewFile();
			if(file.exists())
				return new FileInputStream(file);
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		return null;
	}
}
