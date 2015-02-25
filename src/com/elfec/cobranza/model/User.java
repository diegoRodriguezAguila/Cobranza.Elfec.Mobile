package com.elfec.cobranza.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.elfec.cobranza.helpers.security.AES;
import com.elfec.cobranza.model.enums.UserStatus;

/**
 * Se guardan los usuarios localmente
 * @author drodriguez
 *
 */
@Table(name = "Users")
public class User extends Model{
	@Column(name = "Username", notNull=true)
	public String username;
	
	@Column(name = "EncryptedPassword", notNull=true)
	public String encryptedPassword;
	
	@Column(name = "SyncDate")
	public DateTime syncDate;
	
	@Column(name = "Status", notNull=true)
	public short status;
	
	public User() {
		super();
	}	
	
	public User(String username, String encryptedPassword, short status) {
		super();
		this.username = username;
		this.encryptedPassword = encryptedPassword;
		this.status = status;
	}
	
	//#region Getters y Setters
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public DateTime getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(DateTime syncDate) {
		this.syncDate = syncDate;
	}

	public UserStatus getStatus() {
		return UserStatus.get(status);
	}

	public void setStatus(UserStatus status) {
		this.status = status.toShort();
	}
	
	//#endregion

	/**
	 * Accede a la base de datos y obtiene el usuario que corresponde al nombre de usuario provisto
	 * @param username el nombre de usuario para buscar
	 * @return El usuario con el nombre de usuario correspondiente
	 * **/
	public static User findByUserName(String username) {
	    return new Select()
	        .from(User.class).where("Username=?", username)
	        .executeSingle();
	}
	
	/**
	 * Sincroniza a un usuario, encripta su password, lo guarda localmente y le asigna
	 * una fecha de sincronización
	 * @param password (el password sin encriptar)
	 * @return el usuario sincronizado
	 */
	public User synchronizeUser(String password)
	{
		syncDate = DateTime.now();
		encryptedPassword = AES.encrypt(generateUserKey(), password);
		save();
		return this;
	}
	
	/**
	 * Verifica si es que el password proporcionado coincide con el del usuario
	 * @param testPassword
	 * @return
	 */
	public boolean passwordMatch(String testPassword)
	{
		return encryptedPassword.equals(AES.encrypt(generateUserKey(), testPassword));
	}
	
	/**
	 * Genera la clave correspondiente a este usuario, utiliza la fecha
	 * de sincronización y el nombre de usuario para crearla, así que esta ya deberían estar asignados
	 * @return
	 */
	private final String generateUserKey()
	{
		StringBuilder str = new StringBuilder();
		str.append(syncDate.getMillis()).insert(5, ((char)((int)str.charAt(5))*14));
		str.append(syncDate.getMillis()).insert(6, ((char)((int)str.charAt(1))*9));
		str.append(syncDate.getMillis()).insert(3, username.charAt(0));
		return str.toString();
	}
}
