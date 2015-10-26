package com.elfec.cobranza.business_logic.data_exchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.elfec.cobranza.helpers.ImageInternalAccess;
import com.elfec.cobranza.model.data_exchange.DownloadedBitmap;
import com.elfec.cobranza.model.events.OnImageDownloadFinished;
import com.elfec.cobranza.settings.PreferencesManager;

/**
 * Descarga una o más imágenes de internet y las guarda en la memoria interna
 * con el mismo nombre que tenía remotamente
 * @author drodriguez
 *
 */
public class ImageDownloader extends AsyncTask<String, Void, List<DownloadedBitmap>> {
	/**
	 * Evento que se llama al finalizar la descarga de imágenes
	 */
	private OnImageDownloadFinished imageDownloadCallback;
	
    public ImageDownloader(OnImageDownloadFinished imageDownloadCallback) {
		super();
		this.imageDownloadCallback = imageDownloadCallback;
	}

	protected List<DownloadedBitmap> doInBackground(String... urls) {
    	List<DownloadedBitmap> images = new ArrayList<DownloadedBitmap>();
    	URL downloadURL;
    	try {
	        for (int i = 0; i < urls.length; i++) {
				downloadURL = new URL(urls[i]);
	        	images.add( new DownloadedBitmap (downloadImage(downloadURL), downloadURL));
			}
        } catch (Exception e) {
			e.printStackTrace();
			images.clear();
			imageDownloadCallback.downloadFinished(false);
			imageDownloadCallback = null;
        }
        return images;
    }
    
    /**
     * Descarga una imagen desde internet dada una URL
     * @param url
     * @return
     * @throws IOException 
     */
    private Bitmap downloadImage(URL url) throws IOException
    {
    	Bitmap mIcon = null;
            InputStream in = url.openStream();
            mIcon = BitmapFactory.decodeStream(in);
        return mIcon;
    }

    protected void onPostExecute(List<DownloadedBitmap> result) {
    	try {
	    	for(DownloadedBitmap downBitmap : result)
	    	{
				ImageInternalAccess.saveToInternalSorage(PreferencesManager.getApplicationContext(), downBitmap.getImage(), downBitmap.getImageName());			
	    	}
    	} catch (IOException e) {
			e.printStackTrace();
			imageDownloadCallback.downloadFinished(false);
			imageDownloadCallback = null;
		}
    	if(imageDownloadCallback!=null)
    		imageDownloadCallback.downloadFinished(true);
    }
}
