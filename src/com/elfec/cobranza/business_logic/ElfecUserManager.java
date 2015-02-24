package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.enums.DeviceStatus;
import com.elfec.cobranza.model.enums.UserStatus;
import com.elfec.cobranza.model.exceptions.UnabledDeviceException;
import com.elfec.cobranza.model.exceptions.UnactiveUserException;
import com.elfec.cobranza.remote_data_access.DeviceRemoteDataAccess;
import com.elfec.cobranza.remote_data_access.UserRemoteDataAccess;

/**
 * Se encarga de las operaciones de lógica de negocio sobre el usuario
 * @author drodriguez
 *
 */
public class ElfecUserManager {

	/**
	 * Valida a un usuario, ya sea local o remotamente según el caso
	 * @param username
	 * @param password
	 * @param IMEI
	 * @return
	 */
	public static List<Exception> validateUser(String username, String password, String IMEI)
	{
		List<Exception> errors = new ArrayList<Exception>();
		User localUser = User.findByUserName(username);
		if(localUser==null)
		{
			try {
				User remoteUser = UserRemoteDataAccess.requestUser(username, password);
				if(remoteUser.getStatus()!=UserStatus.ACTIVE)
					errors.add(new UnactiveUserException(username));
				if(DeviceRemoteDataAccess.requestDeviceStatus(username, password, IMEI)==DeviceStatus.UNABLED)
					errors.add(new UnabledDeviceException());
			} catch (ConnectException e) {
				errors.add(e);
			} catch (SQLException e) {
				errors.add(e);
			}
		}
		return errors;
	}
}
