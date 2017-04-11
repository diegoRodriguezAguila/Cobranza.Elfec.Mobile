package com.elfec.cobranza.helpers.security;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Clase que se encarga de encriptar y desencriptar con AES como algoritmo de
 * encriptación
 * @author drodriguez
 *
 */
public class AES {

	/**
	 * Encripta una cadena con la clave provista
	 * @param key
	 * @param strToEncrypt
	 * @return
	 */
	public static String encrypt(String key, String strToEncrypt)
	{
		try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");        
            cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(key));        
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")),Base64.DEFAULT);
        
        }
        catch (Exception e)
        {           
            System.out.println("Error while encrypting: "+e.toString());
        }
        return null;
	}
	
	/**
	 * Desencripta una cadena con la clave provista
	 * @param key
	 * @param strToDecrypt
	 * @return
	 */
	public static String decrypt(String key, String strToDecrypt)
	{
		try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");           
            cipher.init(Cipher.DECRYPT_MODE, getKeySpec(key));
            return new String(cipher.doFinal(Base64.decode(strToDecrypt,Base64.DEFAULT)));          
        }
        catch (Exception e)
        {         
            System.out.println("Error while decrypting: "+e.toString());
        }
        return null;
	}
	
	/**
	 * Genera un keyspec con la clave provista
	 * @param strKey
	 * @return
	 */
	private static SecretKeySpec getKeySpec(String strKey)
	{
        MessageDigest sha;
		try {
			byte[] key = strKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
	        key = Arrays.copyOf(key, 16); // use only first 128 bit
	        return new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] defaultKey = {1,4,76,11,48,123,0,52,2,8,4,3,15,7,86,23};
		return new SecretKeySpec(defaultKey, "AES");
	}
}
