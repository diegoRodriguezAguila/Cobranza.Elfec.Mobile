package com.elfec.cobranza.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.elfec.cobranza.model.exceptions.InitializationException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;

import org.joda.time.DateTime;

/**
 * Maneja las sharedpreferences de toda la aplicación
 * @author Diego
 *
 */
public class PreferencesManager {

	private final String LOGGED_USERNAME = "loggedUsername";
	private final String LOGGED_CASHDESK_NUMBER = "loggedCashdeskNumber";
	private final String LOGGED_CASHDESK_DESC = "loggedCashdeskDescription";
	//ONCE IMPORT DATA
	private final String ALL_ONCE_REQUIRED_DATA_IMPORTED = "allOnceReqDataImported";
	private final String SUPPLY_CATEGORY_TYPES_IMPORTED = "supplyCategoryTypesImported";
	private final String CPT_CALCULATION_BASES_IMPORTED = "conceptCalculationBasesImported";
	private final String PRNT_CALCULATION_BASES_IMPORTED = "printCalculationBasesImported";
	private final String CATEGORIES_IMPORTED = "categoriesImported";
	private final String CONCEPTS_IMPORTED = "conceptsImported";
	private final String BANK_ACCOUNTS_IMPORTED = "bankAccountsImported";
	private final String PERIOD_BANK_ACCOUNTS_IMPORTED = "periodBankAccountsImported";
	private final String ANNULMENT_REASONS_IMPORTED = "annulmentReasonsImported";
	
	//DEFAULT PRINTER
	private final String DEFAULT_PRINTER_ADDRESS = "defaultPrinterAddress";
	private final String DEFAULT_PRINTER_NAME = "defaultPrinterName";
	// IMAGES
	private final String HEADER_IMAGE_DOWNLOAD_DATE = "headerImageDownloadDate";
	private final String FOOTER_IMAGE_DOWNLOAD_DATE = "footerImageDownloadDate";
	
	private final String HEADER_SEND_TO_PRINTER_DATE = "headerSendToPrinterDate";
	private final String FOOTER_SEND_TO_PRINTER_DATE = "footerSendToPrinterDate";
	
	private SharedPreferences preferences;
	
	private PreferencesManager(Context context)
	{
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
		
	private static Context context;
	private static PreferencesManager preferencesManager;
	/**
	 * Este método se debe llamar al inicializar la aplicación
	 * @param context
	 */
	public static void initialize(Context context)
	{
		PreferencesManager.context = context;
	}
	/**
	 * Obtiene el contexto de la aplicación
	 * @return el contexto de la aplicación
	 */
	public static Context getApplicationContext()
	{
		return PreferencesManager.context;
	}
	
	public static PreferencesManager instance()
	{
		if(preferencesManager==null)
		{
			if(context==null)
				throw new InitializationException();
			preferencesManager = new PreferencesManager(context);
		}
		return preferencesManager;
	}
	
	/**
	 * Obtiene el usuario logeado actual
	 * @return null si es que ninguno se ha logeado
	 */
	public String getLoggedUsername()
	{
		return preferences.getString(LOGGED_USERNAME, null);
	}
	
	/**
	 * Asigna el usuario logeado actual, sobreescribe cualquier usuario que haya sido logeado antes
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setLoggedUsername(String loggedUsername)
	{
		preferences.edit().putString(LOGGED_USERNAME, loggedUsername).commit();
		return this;
	}	
	
	/**
	 * Obtiene el numero de caja del usuario logeado actual
	 * @return -1 si es que ninguno se ha logeado, o si no se asignó el valor
	 */
	public int getLoggedCashdeskNumber()
	{
		return preferences.getInt(LOGGED_CASHDESK_NUMBER, -1);
	}
	
	/**
	 * Asigna el numero de caja del usuario logeado actual
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setLoggedCashdeskNumber(int loggedCashdeskNumber)
	{
		preferences.edit().putInt(LOGGED_CASHDESK_NUMBER, loggedCashdeskNumber).commit();
		return this;
	}	

	/**
	 * Obtiene la descripción de la caja del usuario logeado actual
	 * @return -1 si es que ninguno se ha logeado, o si no se asignó el valor
	 */
	public String getLoggedCashdeskDesc()
	{
		return preferences.getString(LOGGED_CASHDESK_DESC, null);
	}
	
	/**
	 * Asigna la descripción de la caja  del usuario logeado actual
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setLoggedCashdeskDesc(String loggedCashdeskNumber)
	{
		preferences.edit().putString(LOGGED_CASHDESK_DESC, loggedCashdeskNumber).commit();
		return this;
	}
	
	/**
	 * Obtiene la fecha en que se descargó la imagen del header
	 * @return fecha en la fecha en que se descargó la imagen del footer
	 */
	public DateTime getHeaderImageDownloadDate()
	{
		return new DateTime(preferences.getLong(HEADER_IMAGE_DOWNLOAD_DATE, 0));
	}	
	/**
	 * Asigna la fecha en que se descargó la imagen del header
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setHeaderImageDownloadDate(DateTime date)
	{
		preferences.edit().putLong(HEADER_IMAGE_DOWNLOAD_DATE, date.toDate().getTime()).commit();
		return this;
	}
	
	/**
	 * Obtiene la fecha en que se envió la imagen del header a la impresora
	 * @return fecha en la fecha en que se descargó la imagen del footer
	 */
	public DateTime getHeaderSentToPrinterDate()
	{
		return new DateTime(preferences.getLong(HEADER_SEND_TO_PRINTER_DATE, 0));
	}	
	/**
	 * Asigna la fecha en que se envió la imagen del header a la impresora
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setHeaderSentToPrinterDate(DateTime date)
	{
		preferences.edit().putLong(HEADER_SEND_TO_PRINTER_DATE, date.toDate().getTime()).commit();
		return this;
	}
	
	/**
	 * Obtiene la fecha en que se descargó la imagen del footer
	 * @return fecha en la fecha en que se descargó la imagen del footer
	 */
	public DateTime getFooterImageDownloadDate()
	{
		return new DateTime(preferences.getLong(FOOTER_IMAGE_DOWNLOAD_DATE, 0));
	}	
	/**
	 * Asigna la fecha en que se descargó la imagen del footer
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setFooterImageDownloadDate(DateTime date)
	{
		preferences.edit().putLong(FOOTER_IMAGE_DOWNLOAD_DATE, date.toDate().getTime()).commit();
		return this;
	}
	
	/**
	 * Obtiene la fecha en que se envió la imagen del footer a la impresora
	 * @return fecha en la fecha en que se descargó la imagen del footer
	 */
	public DateTime getFooterSentToPrinterDate()
	{
		return new DateTime(preferences.getLong(FOOTER_SEND_TO_PRINTER_DATE, 0));
	}	
	/**
	 * Asigna la fecha en que se envió la imagen del footer a la impresora
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setFooterSentToPrinterDate(DateTime date)
	{
		preferences.edit().putLong(FOOTER_SEND_TO_PRINTER_DATE, date.toDate().getTime()).commit();
		return this;
	}
	
	/**
	 * Indica si toda la información que solo debe ser importada una vez la ha sido
	 * @return true si es que ya se importó toda
	 */
	public boolean isAllOnceReqDataImported()
	{
		return preferences.getBoolean(ALL_ONCE_REQUIRED_DATA_IMPORTED, false);
	}	
	/**
	 * Asigna si toda la información que solo debe ser importada una vez la ha sido
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setAllOnceReqDataImported(boolean isImported)
	{
		preferences.edit().putBoolean(ALL_ONCE_REQUIRED_DATA_IMPORTED, isImported).commit();
		return this;
	}
	
	/**
	 * Indica si los TIPOS_CATEG_SUM han sido importados
	 * @return true si es que ya se importó
	 */
	public boolean isSupplyCategoryTypesImported()
	{
		return preferences.getBoolean(SUPPLY_CATEGORY_TYPES_IMPORTED, false);
	}	
	/**
	 * Asigna si los TIPOS_CATEG_SUM han sido importados
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setSupplyCategoryTypesImported(boolean isImported)
	{
		preferences.edit().putBoolean(SUPPLY_CATEGORY_TYPES_IMPORTED, isImported).commit();
		return this;
	}
	
	/**
	 * Indica si los GBASES_CALC_CPTOS han sido importados
	 * @return true si es que ya se importó
	 */
	public boolean isConceptCalculationBasesImported()
	{
		return preferences.getBoolean(CPT_CALCULATION_BASES_IMPORTED, false);
	}	
	/**
	 * Asigna si los GBASES_CALC_CPTOS han sido importados
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setConceptCalculationBasesImported(boolean isImported)
	{
		preferences.edit().putBoolean(CPT_CALCULATION_BASES_IMPORTED, isImported).commit();
		return this;
	}
	
	/**
	 * Indica si los GBASES_CALC_IMP han sido importados
	 * @return true si es que ya se importó
	 */
	public boolean isPrintCalculationBasesImported()
	{
		return preferences.getBoolean(PRNT_CALCULATION_BASES_IMPORTED, false);
	}	
	/**
	 * Asigna si los GBASES_CALC_IMP han sido importados
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setPrintCalculationBasesImported(boolean isImported)
	{
		preferences.edit().putBoolean(PRNT_CALCULATION_BASES_IMPORTED, isImported).commit();
		return this;
	}
	
	/**
	 * Indica si las CATEGORIAS han sido importadas
	 * @return true si es que ya se importó
	 */
	public boolean isCategoriesImported()
	{
		return preferences.getBoolean(CATEGORIES_IMPORTED, false);
	}	
	/**
	 * Asigna si las CATEGORIAS han sido importadas
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setCategoriesImported(boolean isImported)
	{
		preferences.edit().putBoolean(CATEGORIES_IMPORTED, isImported).commit();
		return this;
	}
	
	/**
	 * Indica si los CONCEPTOS han sido importados
	 * @return true si es que ya se importó
	 */
	public boolean isConceptsImported()
	{
		return preferences.getBoolean(CONCEPTS_IMPORTED, false);
	}	
	/**
	 * Asigna si los CONCEPTOS han sido importados
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setConceptsImported(boolean isImported)
	{
		preferences.edit().putBoolean(CONCEPTS_IMPORTED, isImported).commit();
		return this;
	}
	
	/**
	 * Indica si las BAN_CTAS han sido importadas
	 * @return true si es que ya se importó
	 */
	public boolean isBankAccountsImported()
	{
		return preferences.getBoolean(BANK_ACCOUNTS_IMPORTED, false);
	}	
	/**
	 * Asigna si las BAN_CTAS han sido importadas
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setBankAccountsImported(boolean isImported)
	{
		preferences.edit().putBoolean(BANK_ACCOUNTS_IMPORTED, isImported).commit();
		return this;
	}
	
	/**
	 * Indica si los BAN_CTAS_PER han sido importados
	 * @return true si es que ya se importó
	 */
	public boolean isPeriodBankAccountsImported()
	{
		return preferences.getBoolean(PERIOD_BANK_ACCOUNTS_IMPORTED, false);
	}	
	/**
	 * Asigna si los BAN_CTAS_PER han sido importados
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setPeriodBankAccountsImported(boolean isImported)
	{
		preferences.edit().putBoolean(PERIOD_BANK_ACCOUNTS_IMPORTED, isImported).commit();
		return this;
	}
	
	/**
	 * Indica si los MOTIVOS_ANULACION han sido importados
	 * @return true si es que ya se importó
	 */
	public boolean isAnnulmentReasonsImported()
	{
		return preferences.getBoolean(ANNULMENT_REASONS_IMPORTED, false);
	}	
	/**
	 * Asigna si los MOTIVOS_ANULACION han sido importados
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setAnnulmentReasonsImported(boolean isImported)
	{
		preferences.edit().putBoolean(ANNULMENT_REASONS_IMPORTED, isImported).commit();
		return this;
	}
	
	//DEFAULT PRINTER
	/**
	 * Obtiene la impresora por defecto
	 * @return la impresora por defecto elegida por el usuario, null si es que nunca se eligió una
	 */
	public DiscoveredPrinterBluetooth getDefaultPrinter()
	{
		String address = preferences.getString(DEFAULT_PRINTER_ADDRESS, null);
		if(address==null)
			return null;
		String friendlyName = preferences.getString(DEFAULT_PRINTER_NAME, null);
		return new DiscoveredPrinterBluetooth(address, friendlyName);
	}	
	/**
	 * Asigna lla impresora por defecto
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setDefaultPrinter(DiscoveredPrinterBluetooth defaultPrinter)
	{
		preferences.edit().putString(DEFAULT_PRINTER_ADDRESS, defaultPrinter.address).commit();
		preferences.edit().putString(DEFAULT_PRINTER_NAME, defaultPrinter.friendlyName).commit();
		return this;
	}
	
	/**
	 * Borra las preferencias guardadas de la información que se debe importar
	 * solo una vez 
	 */
	public void wipeOnceRequiredDataPreferences()
	{
		preferences.edit().remove(ALL_ONCE_REQUIRED_DATA_IMPORTED)
						  .remove(SUPPLY_CATEGORY_TYPES_IMPORTED)
						  .remove(CPT_CALCULATION_BASES_IMPORTED)
						  .remove(PRNT_CALCULATION_BASES_IMPORTED)
						  .remove(CATEGORIES_IMPORTED)
						  .remove(CONCEPTS_IMPORTED)
						  .remove(BANK_ACCOUNTS_IMPORTED)
						  .remove(PERIOD_BANK_ACCOUNTS_IMPORTED)
						  .remove(ANNULMENT_REASONS_IMPORTED).commit();
	}
}
