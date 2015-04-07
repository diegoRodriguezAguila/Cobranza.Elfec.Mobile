package com.elfec.cobranza.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
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
	 * Obtiene el directorio de almacenamiento externo 
	 * @return File
	 */
	public static File getInternalAppDirectory(Context context)
	{
		File appDir = context.getFilesDir();
	    if (! appDir.exists()){
	        if (! appDir.mkdirs()){
	            Log.e("App Directory failed","Failed to create directory: "+APP_DIR);
	            return null;
	        }
	    }
	    return appDir;
	}
	
	/**
	 * Obtiene el File del archivo solicitado del almacenamiento externo
	 * @param fileName
	 * @param createIfNotExists true si se debe crear si no existe el archivo
	 * @return el FileputStream del archivo solicitado, si no existe null o el archivo vacio
	 */
	public static File getExternalFile(String fileName, boolean createIfNotExists)
	{
		File file = new File(FileManager.getExternalAppDirectory().getPath()+File.separator+fileName);
		try 
		{
			if(createIfNotExists)
				file.createNewFile();
			if(file.exists())
				return file;
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		return null;
	}
	
	/**
	 * Obtiene el File del archivo solicitado del almacenamiento interno
	 * @param context
	 * @param fileName
	 * @param createIfNotExists true si se debe crear si no existe el archivo
	 * @return el File del archivo solicitado, si no existe null o el archivo vacio
	 */
	public static File getInternalFile(Context context, String fileName, boolean createIfNotExists)
	{
		File file = new File(getInternalAppDirectory(context)+File.separator+fileName);
		try 
		{
			if(createIfNotExists)
				file.createNewFile();
			if(file.exists())
				return file;
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		return null;
	}
	
	/**
	 * Obtiene el input stream
	 * @param file
	 * @return FileInputStream
	 */
	public static FileInputStream getFileInputStream(File file)
	{
		try {
			if(file!=null)
				return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Obtiene el output stream
	 * @param file
	 * @return FileOutputStream
	 */
	public static FileOutputStream getFileOutputStream(File file)
	{
		try {
			if(file!=null)
				return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
