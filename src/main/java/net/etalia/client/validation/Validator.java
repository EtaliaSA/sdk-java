package net.etalia.client.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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

	private RuleSet rules = null;
	private Set<String> properties = null;
	private Set<String> groups = null;
	
	public Validator(RuleSet brules) {
		if (brules != null) {
			this.rules = brules;
		} else {
			this.rules = new RuleSet(); 
		}
	}
	
	/**
	 * Adds a rule to this validator.
	 * @param rule The rule instance.
	 */
	public void addRule(Rule rule) {
		rules.addRule(rule);
	}
	
	/**
	 * Configure the property groups this validator will work on.
	 * @param groups Names of the property groups.
	 */
	public void setGroups(String... groups) {
		if (groups.length == 0) {
			this.groups = null;
			return;
		}
		this.groups = new HashSet<String>(Arrays.asList(groups));
	}
	
	/**
	 * Configure the properties this validator will work on.
	 * @param properties Names of the properties.
	 */
	public void setProperties(String... properties) {
		if (properties.length == 0) {
			this.properties = null;
			return;
		}
		this.properties = new HashSet<String>(Arrays.asList(properties));
	}
	
	/**
	 * Checks whether the given property is being validated, given what has been configured 
	 * by {@link #setProperties(String...)} and {@link #setGroups(String...)}.
	 * @param propertyName
	 * @return
	 */
	public boolean hasProperty(String propertyName) {
		if (this.properties == null) return true;
		return this.properties.contains(propertyName);
	}
	
	public boolean hasGroup(String group) {
		if (this.groups == null) return true;
		return this.groups.contains(group);
	}
	
	/**
	 * Validates property values represented as a Map of "property name"-&gt;"property value".
	 * @param properties Property values in a map.
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validate(Map<String,String> properties) {
		return validate(properties, this);
	}
	
	//@Override
	public List<ValidationMessage> validate(Map<String,String> properties, Validator parent) {
		return rules.validate(properties, parent);
	}

	/**
	 * Validates a single property for the given value. 
	 * @param propertyName Name of the property.
	 * @param value String value of the property
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validate(String propertyName, String value) {
		Validator subv = new Validator(this.rules);
		subv.setProperties(propertyName);
		Map<String,String> vals = new HashMap<String, String>();
		vals.put(propertyName, value);
		return subv.validate(vals);
	}
	
}
