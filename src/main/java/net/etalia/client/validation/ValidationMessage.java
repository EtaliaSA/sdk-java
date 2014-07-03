package net.etalia.client.validation;

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
}