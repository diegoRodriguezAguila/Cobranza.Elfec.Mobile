package com.elfec.cobranza.business_logic;

import android.graphics.Bitmap;

import com.elfec.cobranza.helpers.ImageInternalAccess;
import com.elfec.cobranza.helpers.PreferencesManager;
import com.elfec.cobranza.model.downloaders.ImageDownloader;
import com.elfec.cobranza.model.events.OnImageDownloadFinished;

/**
 * Maneja las operaciones de negocio relacionadas con las imágenes para las
 * facturas
 * la de encabezado y la de pié
 * @author drodriguez
 *
 */
public class ReceiptImagesManager {

	//TEST PRUPOUSES
	public static final String url="http://elfcob04/offline_logos/";
	
	public static final String headerImageName = "factura_logo.jpg";
	public static final String footerImageName = "banner_horizontal.jpg";
	
	/**
	 * Importa las imagenes para la cebecera y el pié de la factura, solo si es necesario importarlos
	 */
	public static void importReceiptImages()
	{
		if(!PreferencesManager.instance().isReceiptImagesDownloaded())
			new ImageDownloader(new OnImageDownloadFinished() {			
				@Override
				public void downloadFinished(boolean succes) {
					PreferencesManager.instance().setReceiptImagesDownloaded(succes);
				}
			}).execute(url+headerImageName, url+footerImageName);
	}
	
	/**
	 * Obtiene la imagen que se usa de encabezado
	 * @return
	 */
	public static Bitmap getHeaderImage()
	{
		return ImageInternalAccess.loadImageFromStorage(headerImageName);
	}
}
