package com.elfec.cobranza.business_logic;

import com.elfec.cobranza.model.validations.IValidationRule;
import com.elfec.cobranza.model.validations.ValidationRulesFactory;
import com.elfec.cobranza.model.validations.ValidationsAndParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Se encarga de validar un campo con las reglas y parametros de validación definidos
 * @author Diego
 *
 */
public class FieldValidator {
	/**
	 * Realiza la validación de un campo y recopila los mensajes de error arrojados por cada una de las validaciones
	 * @param fieldName
	 * @param isMaleGender
	 * @param fieldValue
	 * @param validationRules
	 * @param validationParams, la lista de reglas de validación 
	 * @return
	 */
	public static <T> List<String> validate(String fieldName, boolean isMaleGender, T fieldValue, List<IValidationRule<T>> validationRules, String[] validationParams)
	{
		List<String> validationErrors = new ArrayList<String>();
		int size = validationRules.size();
		for (int i = 0; i < size; i++) {
			if(!validationRules.get(i).isValid(fieldValue, validationParams[i]!=null? validationParams[i].split(","):null))
			{
				validationErrors.add(validationRules.get(i).getErrorMessage(fieldName, isMaleGender));
			}
		} 
		return validationErrors;
	}
	/**
	 * Realiza la validación de un campo y recopila los mensajes de error arrojados por cada una de las validaciones
	 * @param fieldName
	 * @param fieldIsMaleGender
	 * @param fieldValue
	 * @param validationRules, las reglas de validación pero como cadena
	 * @return
	 */
	public static List<String> validate(String fieldName, boolean fieldIsMaleGender, String fieldValue, 
			String validationRules)
	{
		ValidationsAndParams<String> validationRulesAndParams = ValidationRulesFactory.createValidationRulesWithParams(validationRules);
		return FieldValidator.validate(fieldName, fieldIsMaleGender, fieldValue, 
				validationRulesAndParams.getValidationRules(), validationRulesAndParams.getParams());
	}
}
