package net.etalia.client.validation;

import java.util.List;
import java.util.Map;

public class RegexpRule extends SinglePropertyRule {

	private String regexp;
	private String modifiers;
	private String message;
	
	public RegexpRule(String property, String regexp, String modifiers, String message) {
		super(property);
		this.regexp = regexp;
		this.modifiers = modifiers;
		this.message = message;
	}

	public List<ValidationMessage> validate(Map<String, String> properties, Validator validator) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
