package com.elfec.cobranza.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
	public static String saveToInternalSorage(Context context, Bitmap bitmapImage, String fileName) throws IOException{      
        // path to /data/data/yourapp/app_data/images
        File directory = context.getDir("images", Context.MODE_PRIVATE);     
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
	 * @param context
	 * @param fileName
	 * @return El bitmap de la imagen
	 */
	public static Bitmap loadImageFromStorage(Context context, String fileName)
	{
	    try {
	    	// path to /data/data/yourapp/app_data/images
	        File directory = context.getDir("images", Context.MODE_PRIVATE);
	        File path = new File(directory, fileName);
	        return BitmapFactory.decodeStream(new FileInputStream(path));
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	/**
	 * Obtiene la imagen de la carpetaAssets
	 * @param context
	 * @param fileName
	 * @return el Bitmap
	 */
	public static Bitmap loadImageFromAssets(Context context, String fileName)
	{
	    try {
	        return BitmapFactory.decodeStream(context.getAssets().open(fileName));
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
}
