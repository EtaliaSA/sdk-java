package net.etalia.client.services;

import java.util.List;
import java.util.Map;

/**
 * This class validates a view.
 * <p>
 * An instance of this class must be obtained from {@link ValidatorFactory#getViewValidator(Class)} 
 * or {@link ValidatorFactory#getViewValidator(java.io.Reader)}.
 * </p>
 * <p>
 * If obtained by {@link ValidatorFactory#getViewValidator(Class)}, it will be configured with all the
 * rules for the given domain class. If obtained by {@link ValidatorFactory#getViewValidator(java.io.Reader)}
 * it will be configured based on the view configuration json.
 * </p>
 * <p>
 * In both cases, more rules can be added using {@link #addRulesFromJson(String)} to parse a json or
 * {@link #addRule(String, String, String, String)} to hard-code a validation rule.
 * </p>
 * 
 * <p>
 * The simplest usage scenario is :
 * <pre>
 * ValidatorFactory factory = new ValidatorFactory();
 * ViewValidator val = factory.getViewValidator(User.class);
 * List&lt;ValidationMessage&gt; errors = val.validate(userBean);
 * if (errors.size() > 0) {
 *   // Do proper actions to report to the user the errors
 * }
 * </pre>
 * </p>
 * <p>
 * For custom configuration :
 * <pre>
 * ViewValidator val = factory.getViewValidator(User.class);
 * val.addRule("password2",".*","i","WRONG_PASS2");
 * List&lt;ValidationMessage&gt; errors = val.validate(userBean);
 * errors.addAll(val.validate("password2",password2Field.getText());
 * </pre>
 * </p>
 * 
 * @author Simone Gianni <simoneg@apache.org>
 */
public class ViewValidator {

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
	 * Parses a json representing rules, and add the rules to the current rule set. The current
	 * rule set is composed of rules loaded from classpath during init and rules added by this method. 
	 * @param json The json string to parse rules from.
	 */
	public void addRulesFromJson(String json) {}
	
	/**
	 * Add a specific rule for a property of this view.
	 * @param property Name of the property
	 * @param regexp The regular expression for the validation
	 * @param modifier The regular expression modifiers
	 * @param message The message to return in case of error
	 */
	public void addRule(String property, String regexp, String modifier, String message) {}
	
	/**
	 * Configure the property groups this validator will work on.
	 * @param groups Names of the property groups.
	 */
	public void setGroups(String... groups) {}
	
	/**
	 * Configure the properties this validator will work on.
	 * @param properties Names of the properties.
	 */
	public void setProperties(String... properties) {}
	
	/**
	 * Validates an object. This method will validate the object checking the groups or properties
	 * configured with {@link #setGroups(String...)} and/or {@link #setProperties(String...)}, or all 
	 * of them if none of those methods where called. 
	 * @param bean Bean to validate
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validate(Object bean) {
		return null;
	}
	
	/**
	 * Validates property values represented as a Map of "property name"-&gt;"property value".
	 * @param properties Property values in a map.
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validate(Map<String,String> properties) {
		return null;
	}

	/**
	 * Validates a single property for the given value. 
	 * @param propertyName Name of the property.
	 * @param value String value of the property
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validate(String propertyName, String value) {
		return null;
	}
	
}
