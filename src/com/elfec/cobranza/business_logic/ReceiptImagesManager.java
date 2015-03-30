package com.elfec.cobranza.business_logic;

import android.graphics.Bitmap;

import com.elfec.cobranza.helpers.ImageInternalAccess;
import com.elfec.cobranza.model.downloaders.ImageDownloader;
import com.elfec.cobranza.model.events.OnImageDownloadFinished;
import com.elfec.cobranza.settings.PreferencesManager;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.ZebraPrinter;

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
	
	/**
	 * El nombre de la imagen del encabezado de la factura
	 */
	public static final String HEADER_IMAGE_NAME = "factura_logo.png";
	/**
	 * El nombre con el que se guardó la imagen de encabezado en la impresora
	 */
	public static final String HEADER_IMAGE_IN_PRINTER_NAME = "HEADER.PCX";
	
	private static final int headerWidth = 770;
	private static final int headerHeight = 318;
	
	/**
	 * El nombre de la imagen del pie de la factura
	 */
	public static final String FOOTER_IMAGE_NAME = "banner_pie.png";
	/**
	 * El nombre con el que se guardó la imagen de pie en la impresora
	 */
	public static final String FOOTER_IMAGE_IN_PRINTER_NAME = "FOOTER.PCX";
	private static final int footerWidth = 770;
	private static final int footerHeight = 440;
	
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
			}).execute(url+HEADER_IMAGE_NAME, url+FOOTER_IMAGE_NAME);
	}
	
	/**
	 * Obtiene la imagen que se usa de encabezado
	 * @return Bitmap
	 */
	public static Bitmap getHeaderImage()
	{
		return ImageInternalAccess.loadImageFromStorage(HEADER_IMAGE_NAME);
	}
	
	/**
	 * Obtiene la imagen que se usa de pie
	 * @return Bitmap
	 */
	public static Bitmap getFooterImage()
	{
		return ImageInternalAccess.loadImageFromStorage(FOOTER_IMAGE_NAME);
	}
	
	/**
	 * Realiza las validaciones necesarias de fecha de la imagen de encabezado
	 * y si es necesario la envía a la impresora pasada en los parámetros
	 * @param printer
	 * @throws ZebraIllegalArgumentException 
	 * @throws ConnectionException 
	 */
	public static void sendHeaderImageIfNecesary(ZebraPrinter printer) throws ConnectionException, ZebraIllegalArgumentException
	{
		if(!isImageOnPrinter(printer, HEADER_IMAGE_IN_PRINTER_NAME))
		{
			Bitmap header = getHeaderImage();
			printer.storeImage(HEADER_IMAGE_IN_PRINTER_NAME, new ZebraImageAndroid(header), headerWidth, headerHeight);
			if(header!=null)
				header.recycle();
		}
	}
	
	/**
	 * Realiza las validaciones necesarias de fecha de la imagen de pie
	 * y si es necesario la envía a la impresora pasada en los parámetros
	 * @param printer
	 * @throws ZebraIllegalArgumentException 
	 * @throws ConnectionException 
	 */
	public static void sendFooterImageIfNecesary(ZebraPrinter printer) throws ConnectionException, ZebraIllegalArgumentException
	{
		if(!isImageOnPrinter(printer, FOOTER_IMAGE_IN_PRINTER_NAME))
		{
			Bitmap footer = getFooterImage();
			printer.storeImage(FOOTER_IMAGE_IN_PRINTER_NAME, new ZebraImageAndroid(footer), footerWidth, footerHeight);
			if(footer!=null)
				footer.recycle();
		}
	}
	
	/**
	 * Verifica si la imagen con el nombre proporcionado se encuentra en la impresora
	 * @param printer
	 * @param imageName
	 * @return true/false
	 * @throws ZebraIllegalArgumentException 
	 * @throws ConnectionException 
	 */
	private static boolean isImageOnPrinter(ZebraPrinter printer, String imageName) throws ConnectionException, ZebraIllegalArgumentException
	{
		String[] printerImages = printer.retrieveFileNames(new String[]{"PCX"});	
		for (int i = 0; i < printerImages.length; i++) {
			if(printerImages[i].equals(imageName))
				return true;
		}
		return false;
	}
}
