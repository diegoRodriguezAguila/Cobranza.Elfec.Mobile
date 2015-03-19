package com.elfec.cobranza.business_logic;

import com.elfec.cobranza.helpers.PreferencesManager;
import com.elfec.cobranza.model.downloaders.ImageDownloader;
import com.elfec.cobranza.model.events.OnImageDownloadFinished;

/**
 * Maneja las operaciones de negocio relacionadas con las im�genes para las
 * facturas
 * la de encabezado y la de pi�
 * @author drodriguez
 *
 */
public class ReceiptImagesManager {

	//TEST PRUPOUSES
	public static final String url="http://elfcob04/offline_logos/";
	
	public static final String headerImageName = "factura_logo.jpg";
	public static final String footerImageName = "banner_horizontal.jpg";
	
	/**
	 * Importa las imagenes para la cebecera y el pi� de la factura, solo si es necesario importarlos
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
}
