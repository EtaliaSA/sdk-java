package net.etalia.client.validation;

import java.util.List;
import java.util.Map;

public interface Rule {

	/**
	 * Validates property values represented as a Map of "property name"-&gt;"property value".
	 * @param properties Property values in a map.
	 * @param validator The validator this rule is being called by.
	 * @return A list of ValidationMessage, an empty list if no validation issues are found
	 */
	public List<ValidationMessage> validate(Map<String,String> properties, Validator validator);
	
	
}
