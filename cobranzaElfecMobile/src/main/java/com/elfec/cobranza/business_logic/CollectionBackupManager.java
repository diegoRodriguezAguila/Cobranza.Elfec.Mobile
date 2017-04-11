package com.elfec.cobranza.business_logic;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import org.joda.time.DateTime;

import com.elfec.cobranza.helpers.FileManager;
import com.elfec.cobranza.model.CollectionPayment;
import com.elfec.cobranza.model.WSCollection;
import com.elfec.cobranza.settings.PreferencesManager;

/**
 * Clase de logica de negocio que se encarga de realizar un backup de cualquier cobro que se realize
 * y las transacciones, COBROS y COB_WS
 * @author drodriguez
 *
 */
public class CollectionBackupManager {

	public static final String BACKUP_FILE_NAME = "%d-%d Backup.txt";
	
	/**
	 * Obtiene el nombre del archivo de backup para el periodo dado
	 * @param year
	 * @param month
	 * @return el nombre del archivo de backup dado el periodo
	 */
	public static String getPeriodFileName(int year, int month)
	{
		return String.format(Locale.getDefault(), BACKUP_FILE_NAME, year, month);
	}
	
	/**
	 * Obtiene el nombre del archivo de backup del periodo actual
	 * @return
	 */
	public static String getCurrentPeriodFileName()
	{
		return getPeriodFileName(DateTime.now().getYear(), DateTime.now().getMonthOfYear());
	}
	
	/**
	 * Inserta el backup del pago o la anulación de un cobro, en el archivo de backup de todas las memorias externas.
	 * <i>Este proceso accede multiples veces a distintos almacenamientos de disco duro, por lo que siempre se ejecuta en un
	 * hilo aparte</i>
	 * @param collection
	 * @param transaction
	 * @param isPayment true si es que es un pago, caso contrario se la toma como anulación
	 */
	public static void backupCollecionAction(final CollectionPayment collection, final WSCollection transaction, final boolean isPayment)
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				String fileName = getCurrentPeriodFileName();
				File[] extAppDirs = FileManager.getAllExternalAppDirectories(PreferencesManager.getApplicationContext());
				OutputStream os;
				for (File extAppDir : extAppDirs) {		
					try {
						os = FileManager.getFileOutputStream(FileManager.getExternalFile(extAppDir, fileName, true),true);
						os.write((transaction.toInsertSQL()+";\r\n").getBytes());
						os.write(((isPayment?collection.toInsertSQL():collection.toUpdateSQL())+";\r\n\r\n").getBytes());
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
}
