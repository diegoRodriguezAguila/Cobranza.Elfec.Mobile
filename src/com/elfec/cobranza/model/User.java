package com.elfec.cobranza.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

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
