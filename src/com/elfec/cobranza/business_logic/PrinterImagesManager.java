package com.elfec.cobranza.business_logic;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.graphics.Bitmap;

import com.elfec.cobranza.business_logic.data_exchange.ImageDownloader;
import com.elfec.cobranza.helpers.ImageInternalAccess;
import com.elfec.cobranza.model.events.OnImageDownloadFinished;
import com.elfec.cobranza.settings.ParameterSettingsManager;
import com.elfec.cobranza.settings.ParameterSettingsManager.ParamKey;
import com.elfec.cobranza.settings.PreferencesManager;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.ZebraPrinter;

/**
 * Maneja las operaciones de negocio relacionadas con las imágenes para las
 * facturas y reportes
 * la de encabezado y la de pié
 * @author drodriguez
 *
 */
public class PrinterImagesManager {
	
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
	 * Nombre de la imagen de logo que sale en los reportes
	 */
	public static final String REP_LOGO_NAME = "REP_LOGO.png";
	/**
	 * Nombre de la imagen de logo que sale en los reportes, del archivo en impresora
	 */
	public static final String REP_LOGO_IN_PRINTER_NAME = "REP_LOGO.PCX";
	
	private static final int repLogoWidth = 160;
	private static final int repLogoHeight = 42;
	
	/**
	 * Importa las imagenes para la cebecera y el pié de la factura, solo si es necesario importarlos
	 */
	public static void importReceiptImages()
	{
		String url = ParameterSettingsManager.getParameter(ParamKey.IMAGES_SERVER).getStringValue();
		if(url.lastIndexOf("/") != url.length()-1)
			url+="/";
			List<String> urlList = new ArrayList<String>();
			if(hasToImportHeader())
				urlList.add(url+HEADER_IMAGE_NAME);
			if(hasToImportFooter())
				urlList.add(url+FOOTER_IMAGE_NAME);
			
			String[] urls = new String[urlList.size()];
			urls = urlList.toArray(urls);
			ImageDownloader imageDownloader = new ImageDownloader(new OnImageDownloadFinished() {			
				@Override
				public void downloadFinished(boolean succes) {
					if(succes)
					{
						if(hasToImportHeader())
							 PreferencesManager.instance().setHeaderImageDownloadDate(DateTime.now());
						if(hasToImportFooter())
							PreferencesManager.instance().setFooterImageDownloadDate(DateTime.now());
					}					
				}
			});
			if(urls.length>0)
				imageDownloader.execute(urls);
	}
	
	/**
	 * Indica si se debe o no importar el header
	 * @return true/false
	 */
	private static boolean hasToImportHeader()
	{
		DateTime lastDownloadDate = PreferencesManager.instance().getHeaderImageDownloadDate();
		DateTime headerDate = ParameterSettingsManager.getParameter(ParamKey.HEADER_IMG_DATE).getDateTimeValue();
		return (Days.daysBetween(lastDownloadDate, headerDate).getDays()>0);
	}
	
	/**
	 * Indica si se debe o no importar el footer
	 * @return true/false
	 */
	private static boolean hasToImportFooter()
	{
		DateTime lastDownloadDate = PreferencesManager.instance().getFooterImageDownloadDate();
		DateTime footerDate = ParameterSettingsManager.getParameter(ParamKey.FOOTER_IMG_DATE).getDateTimeValue();
		return (Days.daysBetween(lastDownloadDate, footerDate).getDays()>0);
	}
	
	/**
	 * Indica si se debe o no enviar el header a la impresora
	 * @return true/false
	 */
	private static boolean hasToSendHeaderToPrinter()
	{
		DateTime lastDownloadDate = PreferencesManager.instance().getHeaderSentToPrinterDate();
		DateTime headerDate = ParameterSettingsManager.getParameter(ParamKey.HEADER_IMG_DATE).getDateTimeValue();
		return (Days.daysBetween(lastDownloadDate, headerDate).getDays()>0);
	}
	
	/**
	 * Indica si se debe o no enviar el footer a la impresora
	 * @return true/false
	 */
	private static boolean hasToSendFooterToPrinter()
	{
		DateTime lastDownloadDate = PreferencesManager.instance().getFooterSentToPrinterDate();
		DateTime footerDate = ParameterSettingsManager.getParameter(ParamKey.FOOTER_IMG_DATE).getDateTimeValue();
		return (Days.daysBetween(lastDownloadDate, footerDate).getDays()>0);
	}
	
	/**
	 * Obtiene la imagen que se usa de encabezado
	 * @return Bitmap
	 */
	public static Bitmap getHeaderImage()
	{
		return ImageInternalAccess.loadImageFromStorage(PreferencesManager.getApplicationContext(), HEADER_IMAGE_NAME);
	}
	
	/**
	 * Obtiene la imagen que se usa de pie
	 * @return Bitmap
	 */
	public static Bitmap getFooterImage()
	{
		return ImageInternalAccess.loadImageFromStorage(PreferencesManager.getApplicationContext(), FOOTER_IMAGE_NAME);
	}
	
	/**
	 * Obtiene el logo que se usa arriba a la izquierda de los reportes
	 * @return Bitmap
	 */
	public static Bitmap getReportLogoImage()
	{
		return ImageInternalAccess.loadImageFromAssets(PreferencesManager.getApplicationContext(), REP_LOGO_NAME);
	}
	
	/**
	 * Realiza las validaciones necesarias y si es necesario envía a la impresora
	 * @param printer
	 * @throws ZebraIllegalArgumentException 
	 * @throws ConnectionException 
	 */
	public static void sendReportLogoImageIfNecesary(ZebraPrinter printer) throws ConnectionException, ZebraIllegalArgumentException
	{
		if(!isImageOnPrinter(printer, REP_LOGO_IN_PRINTER_NAME))
		{
			Bitmap reportLogo = getReportLogoImage();
			printer.storeImage(REP_LOGO_IN_PRINTER_NAME, new ZebraImageAndroid(reportLogo), repLogoWidth, repLogoHeight);
			if(reportLogo!=null)
				reportLogo.recycle();
		}
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
		if(!isImageOnPrinter(printer, HEADER_IMAGE_IN_PRINTER_NAME) || hasToSendHeaderToPrinter())
		{
			Bitmap header = getHeaderImage();
			printer.storeImage(HEADER_IMAGE_IN_PRINTER_NAME, new ZebraImageAndroid(header), headerWidth, headerHeight);
			if(header!=null)
				header.recycle();
			if(hasToSendHeaderToPrinter())
				PreferencesManager.instance().setHeaderSentToPrinterDate(DateTime.now());
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
		if(!isImageOnPrinter(printer, FOOTER_IMAGE_IN_PRINTER_NAME) || hasToSendFooterToPrinter())
		{
			Bitmap footer = getFooterImage();
			printer.storeImage(FOOTER_IMAGE_IN_PRINTER_NAME, new ZebraImageAndroid(footer), footerWidth, footerHeight);
			if(footer!=null)
				footer.recycle();
			if(hasToSendFooterToPrinter())
				PreferencesManager.instance().setFooterSentToPrinterDate(DateTime.now());
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
