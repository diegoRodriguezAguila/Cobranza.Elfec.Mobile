package com.elfec.cobranza.model.validations;

import org.apache.commons.validator.routines.InetAddressValidator;
public class IpStringValidationRule implements IValidationRule<String> {

	@Override
	public boolean isValid(String objectToValidate, String... params) {
		return InetAddressValidator.getInstance().isValidInet4Address(objectToValidate);
	}

	@Override
	public String getErrorMessage(String fieldName, boolean isMaleGender) {
		return (isMaleGender?"El ":"La ")+fieldName+" tiene que ser una IPv4 válid"+(isMaleGender?"o":"a");
	}

}
