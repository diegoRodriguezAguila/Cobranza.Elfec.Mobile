package com.elfec.cobranza.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Se encarga de la lectura y escritura de imágenes en la memoria interna
 * @author drodriguez
 *
 */
public class ImageInternalAccess {
	public static final String IMAGES_DIR = "images";

	/**
	 * Guarda la imagen en el almacenamiento interno con el nombre provisto
	 * @param bitmapImage
	 * @param fileName
	 * @return el path completo del archivo
	 * @throws IOException 
	 */
	public static String saveToInternalSorage(Bitmap bitmapImage, String fileName) throws IOException{      
        // path to /data/data/yourapp/app_data/images
        File directory = PreferencesManager.getApplicationContext().getDir("images", Context.MODE_PRIVATE);     
        // Create images
        File path = new File(directory, fileName);
        FileOutputStream fos = null;     
        fos = new FileOutputStream(path);
        // Use the compress method on the BitMap object to write image to the OutputStream
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.close();
        return directory.getAbsolutePath();
    }
	
	/**
	 * Obtiene la imagen de almacenamiento interno
	 * @param path
	 */
	public static Bitmap loadImageFromStorage(String fileName)
	{
	    try {
	    	// path to /data/data/yourapp/app_data/images
	        File directory = PreferencesManager.getApplicationContext().getDir("images", Context.MODE_PRIVATE);
	        File path = new File(directory, fileName);
	        return BitmapFactory.decodeStream(new FileInputStream(path));
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
