package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.User;
import com.elfec.cobranza.remote_data_access.UserRemoteDataAccess;

/**
 * Se encarga de las operaciones de lógica de negocio sobre el usuario
 * @author drodriguez
 *
 */
public class ElfecUserManager {

	public static List<Exception> validateUser(String username, String password)
	{
		List<Exception> errors = new ArrayList<Exception>();
		User localUser = User.findByUserName(username);
		if(localUser==null)
		{
			try {
				User remoteUser = UserRemoteDataAccess.requestUser(username, password);
				if(remoteUser.)
			} catch (ConnectException e) {
				errors.add(e);
			} catch (SQLException e) {
				errors.add(e);
			}
		}
		return errors;
	}
}
