package com.elfec.cobranza.business_logic;

import java.net.ConnectException;
import java.sql.SQLException;

import com.elfec.cobranza.model.DataAccessResult;
import com.elfec.cobranza.model.User;
import com.elfec.cobranza.model.enums.DeviceStatus;
import com.elfec.cobranza.model.enums.UserStatus;
import com.elfec.cobranza.model.exceptions.InvalidPasswordException;
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
	 * @return El resultado de la validación, que incluye al usuario obtenido y la lista de errores
	 */
	public static DataAccessResult<User> validateUser(String username, String password, String IMEI)
	{
		DataAccessResult<User> result = new DataAccessResult<User>();
		User localUser = User.findByUserName(username);
		if(localUser==null)
		{
			try {
				User remoteUser = UserRemoteDataAccess.requestUser(username, password);
				if(remoteUser==null || remoteUser.getStatus()!=UserStatus.ACTIVE)
					result.addError(new UnactiveUserException(username));
				if(DeviceRemoteDataAccess.requestDeviceStatus(username, password, IMEI)==DeviceStatus.UNABLED)
					result.addError(new UnabledDeviceException());
				result.setResult(remoteUser.synchronizeUser(password));
			} catch (ConnectException e) {
				result.addError(e);
			} catch (SQLException e) {
				result.addError(e);
			}
		}
		else
		{
			if(!localUser.passwordMatch(password))
				result.addError(new InvalidPasswordException());
			result.setResult(localUser);
		}
		return result;
	}
}
