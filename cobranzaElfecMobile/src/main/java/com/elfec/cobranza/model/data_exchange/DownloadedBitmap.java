package com.elfec.cobranza.model.data_exchange;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Clase utilizada por el descargador de imagenes, representa un bitmap descargado donde se tiene su URL 
 * de la que proviene
 * @author drodriguez
 *
 */
public class DownloadedBitmap {
	private Bitmap image;
	private URL imageUrl;
	
	public DownloadedBitmap(Bitmap image, URL imageUrl) {
		this.image = image;
		this.imageUrl = imageUrl;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public URL getImageUrl() {
		return imageUrl;
	}
	
	/**
	 * Obtiene el nombre de la imagen con su extensión
	 * @return
	 */
	public String getImageName()
	{
		String fileURL = imageUrl.getFile();
		return fileURL.substring( fileURL.lastIndexOf('/')+1, fileURL.length() );
	}

	public void setImageUrl(URL imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
