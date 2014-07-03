package net.etalia.client.validation;

import java.util.List;
import java.util.Map;


/**
 * This class validates a view.
 * <p>
 * An instance of this class must be obtained from {@link ValidatorFactory#getViewValidator(Class)} 
 * and it will be configured with all the rules for the given domain class.
 * </p>
 * <p>
 * More rules can be added using {@link #addRule(Rule)} to add a view-specific validation rule.
 * </p>
 * 
 * <p>
 * The simplest usage scenario is :
 * <pre>
 * ValidatorFactory factory = new ValidatorFactory();
 * factory.parseRules(User.class, json);
 * // ....
 * Validator val = factory.getValidator(User.class);
 * val.addRule(new WhateverRule());
 * // ...
 * List&lt;ValidationMessage&gt; errors = val.validate(userPropertyMap);
 * if (errors.size() > 0) {
 *   // Do proper actions to report to the user the errors
 * }
 * </pre>
 * </p>
 * 
 * @author Simone Gianni <simoneg@apache.org>
 */
public class Validator implements Rule {

	/**
	 * Adds a rule to this validator.
	 * @param rule The rule instance.
	 */
	public void addRule(Rule rule) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Configure the property groups this validator will work on.
	 * @param groups Names of the property groups.
	 */
	public void setGroups(String... groups) {
		// TODO Auto-generated method stub		
	}
	
	/**
	 * Configure the properties this validator will work on.
	 * @param properties Names of the properties.
	 */
	public void setProperties(String... properties) {
		// TODO Auto-generated method stub		
	}
	
	/**
	 * Checks whether the given property is being validated, given what has been configured 
	 * by {@link #setProperties(String...)} and {@link #setGroups(String...)}.
	 * @param propertyName
	 * @return
	 */
	public boolean hasProperty(String propertyName) {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * Validates property values represented as a Map of "property name"-&gt;"property value".
	 * @param properties Property values in a map.
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validate(Map<String,String> properties) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//@Override
	public List<ValidationMessage> validate(Map<String,String> properties, Validator parent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Validates a single property for the given value. 
	 * @param propertyName Name of the property.
	 * @param value String value of the property
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validate(String propertyName, String value) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
