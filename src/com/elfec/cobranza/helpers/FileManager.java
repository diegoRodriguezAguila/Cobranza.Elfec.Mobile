package com.elfec.cobranza.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
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
	 * Obtiene el directorio de almacenamiento externo, por defecto el que está disponible vía USB
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
	 * Obtiene los directorios de almacenamiento externo, sd card. disponibles
	 * @return File[]
	 */
	public static File[] getAllExternalAppDirectories(Context context)
	{		
		boolean isBeforeKitKat = Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT;
		File[] extAppDirs = isBeforeKitKat? getExternalAppDirectoriesPre4_4(context):
				ContextCompat.getExternalFilesDirs(context, null);
		List<File> extAppDirList = new ArrayList<File>();
		for(File appDir : extAppDirs)
		{
			if(appDir!=null && (isBeforeKitKat || Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(appDir))))
			{
				extAppDirList.add(appDir);
			    if (! appDir.exists()){
			        if (! appDir.mkdirs()){
			            Log.e("App Directory failed","Failed to create directory: "+appDir.getPath());
			            return null;
			        }
			    }
			}
		}
	    return extAppDirList.toArray(new File[extAppDirList.size()]);
	}
	/**
	 * Obtiene los directorios de almacenamiento externo, sd card.
	 * Funciona solo en versiones de android pre kit kat
	 * @return File[]
	 */
	private static File[] getExternalAppDirectoriesPre4_4(Context context) {
		String appdata = context.getFilesDir().getPath().replaceFirst("data", "Android");
		String externalpath = new String();
		String internalpath = new String();
		Runtime runtime = Runtime.getRuntime();
		try {
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (!line.contains("secure") && !line.contains("asec")) {
					if (line.matches(reg)) {// external card
						String columns[] = line.split(" ");
						if (columns != null && columns.length > 1) {
							externalpath = externalpath.concat(columns[1]);
						}
					} else if (line.contains("fuse")) {// internal storage
						String columns[] = line.split(" ");
						if (columns != null && columns.length > 1) {
							internalpath = internalpath.concat(columns[1]);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		internalpath+=appdata;
		externalpath+=externalpath.isEmpty()?"":appdata;
		return externalpath.isEmpty()?new File[]{new File(internalpath)}
					:new File[]{new File(internalpath), new File(externalpath)};
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
			if(createIfNotExists && !file.exists())
				file.createNewFile();
			if(file.exists())
				return file;
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		return null;
	}
	
	/**
	 * Obtiene el File del archivo solicitado del almacenamiento externo proporcionado como extAppDirectory
	 * @param extAppDirectory el almacenamiento externo donde se abrirá el archivo
	 * @param fileName
	 * @param createIfNotExists true si se debe crear si no existe el archivo
	 * @return el FileputStream del archivo solicitado, si no existe null o el archivo vacio
	 */
	public static File getExternalFile(File extAppDirectory, String fileName, boolean createIfNotExists)
	{
		File file = new File(extAppDirectory.getPath()+File.separator+fileName);
		try 
		{
			if(createIfNotExists && !file.exists())
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
			if(createIfNotExists && !file.exists())
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
	 * Obtiene el output stream sobreescribiendo el archivo por defecto	 
	 * @return FileOutputStream
	 */
	public static FileOutputStream getFileOutputStream(File file)
	{
		return getFileOutputStream(file, false);
	}
	
	/**
	 * Obtiene el output stream
	 * @param file
	 * @param append indica si se debe escribir a continuación en el archivo (true) o si se debe sobreescribir (false)
	 * @return FileOutputStream
	 */
	public static FileOutputStream getFileOutputStream(File file, boolean append)
	{
		try {
			if(file!=null)
				return new FileOutputStream(file, append);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
