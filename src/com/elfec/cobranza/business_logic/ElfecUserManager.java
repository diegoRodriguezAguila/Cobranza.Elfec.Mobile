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
import com.elfec.cobranza.model.exceptions.UnassignedCashDeskException;
import com.elfec.cobranza.remote_data_access.DeviceRDA;
import com.elfec.cobranza.remote_data_access.UserRDA;
import com.elfec.cobranza.remote_data_access.connection.OracleDatabaseConnector;

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
			OracleDatabaseConnector.disposeInstance();
			validateRemoteUser(username, password, IMEI, result);
		}
		else
		{
			validateLocalUser(password, result, localUser);
		}
		return result;
	}

	/**
	 * Realiza las validaciones del usuario a nivel remoto
	 * @param username
	 * @param password
	 * @param IMEI
	 * @param result
	 */
	private static void validateRemoteUser(String username, String password,
			String IMEI, DataAccessResult<User> result) {
		result.setRemoteDataAccess(true);
		try {
			User remoteUser = UserRDA.requestUser(username, password);
			if(remoteUser==null || remoteUser.getStatus()!=UserStatus.ACTIVE)
				result.addError(new UnactiveUserException(username));
			validateCashDeskNumber(username, password, result, remoteUser);
			if(DeviceRDA.requestDeviceStatus(username, password, IMEI)==DeviceStatus.UNABLED)
				result.addError(new UnabledDeviceException());
			if(!result.hasErrors())
				result.setResult(remoteUser.synchronizeUser(password));
		} catch (ConnectException e) {
			result.addError(e);
		} catch (SQLException e) {
			e.printStackTrace();
			result.addError(e);
		}
	}

	/**
	 * Realiza las validaciones del usuario a nivel local
	 * @param password
	 * @param result
	 * @param localUser
	 */
	private static void validateLocalUser(String password,
			DataAccessResult<User> result, User localUser) {
		result.setRemoteDataAccess(false);
		if(!localUser.passwordMatch(password))
			result.addError(new InvalidPasswordException());
		result.setResult(localUser);
	}

	/**
	 * Valida que el usuario tenga una caja asignada
	 * @param username
	 * @param password
	 * @param result
	 * @param remoteUser
	 * @throws ConnectException
	 * @throws SQLException
	 */
	private static void validateCashDeskNumber(String username,
			String password, DataAccessResult<User> result, User remoteUser)
			throws ConnectException, SQLException {
		if(remoteUser!=null)
		{
			int cashDeskNumber = UserRDA.requestUserCashDeskNumber(username, password, remoteUser.getCashierId());
			if(cashDeskNumber==-1)
				result.addError(new UnassignedCashDeskException(username));
			else remoteUser.setCashDeskNumber(cashDeskNumber);
		}
	}
}
