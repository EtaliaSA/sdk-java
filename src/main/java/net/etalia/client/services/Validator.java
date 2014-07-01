package net.etalia.client.services;

import java.util.List;

/**
 * Validates object instances before sending it to the backend and for giving the user 
 * a feedback on validation rules.
 * 
 * <p>
 * This object is initialized with a default set of validation rules loaded from the 
 * classpath. More rules can then be added using {@link #addRules(String)}, or resetted
 * to an empty set using {@link #cleanRules()} (and then adding totally new ones using again 
 * {@link #addRules(String)}).
 * </p>
 * <p>
 * The simplest usage scenario is :
 * <code>
 * Validator val = new Validator();
 * val.init();
 * List&lt;ValidationMessage&gt; errors = val.validate(bean);
 * if (errors.size() > 0) {
 *   // Do proper actions to report to the user the errors
 * }
 * </code>
 * </p>
 * <p>
 * This class is thread safe, so a single instance of this class can be initialized once and used
 * many times by different threads. 
 * </p>
 * 
 * @author Simone Gianni <simoneg@apache.org>
 */
public class Validator {

	/**
	 * Represents a validation message, usually a violation of validation rules.
	 */
	public static class ValidationMessage {
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
	
	/**
	 * Initializes the service, loading default rules from the classpath if available.
	 */
	public void init() {}
	
	/**
	 * Empties the current rule set, then use {@link #addRules(String)} to add new rules. 
	 */
	public void cleanRules() {}
	
	/**
	 * Parses a json representing rules, and add the rules to the current rule set. The current
	 * rule set is composed of rules loaded from classpath during init and rules added by this method. 
	 * @param json The json string to parse rules from.
	 */
	public void addRules(String json) {}
	
	/**
	 * Validates an object. This method will fully validate the object, all fields in all groups.
	 * @param bean Bean to validate
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validate(Object bean) {
		return null;
	}

	/**
	 * Validates specific groups of fields for the given object. 
	 * @param bean Bean to validate
	 * @param groups Names of groups to validate
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validateGroup(Object bean, String... groups) {
		return null;
	}

	/**
	 * Validates specific properties for the given object.
	 * @param bean Bean to validate
	 * @param properties Names of properties to validate
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validateProperties(Object bean, String... properties) {
		return null;
	}
	
}
