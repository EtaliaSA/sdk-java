package net.etalia.client.validation;

import java.util.Arrays;

/**
 * Represents a validation message, usually a violation of validation rules.
 */
public class ValidationMessage {
	/**
	 * The property name (as per java beans specification) of the property having a validation issue
	 */
	public String property;
	/**
	 * The message (usually i18n key) for the user violation
	 */
	public String message;
	/**
	 * Parameters for i18n to the user
	 */
	public String[] parameters;

	public ValidationMessage() {}
	
	public ValidationMessage(String property, String message, String[] parameters) {
		this.property = property;
		this.message = message;
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		return "ValidationMessage [property=" + property + ", message=" + message + ", parameters=" + Arrays.toString(parameters) + "]";
	}
	
	
	
}