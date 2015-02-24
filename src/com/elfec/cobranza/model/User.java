package com.elfec.cobranza.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
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
	
	public User(String username, String encryptedPassword, DateTime syncDate, short status) {
		super();
		this.username = username;
		this.encryptedPassword = encryptedPassword;
		this.syncDate = syncDate;
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
}
