package net.etalia.client.validation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RegexpRule extends SinglePropertyRule {

	private String regexp;
	private Pattern pattern;
	private String modifiers;
	private String message;
	private String[] parameters;
	
	public RegexpRule(String property, String regexp, String modifiers, String message, String... parameters) {
		super(property);
		this.regexp = regexp;
		this.modifiers = modifiers;
		this.message = message;
		this.parameters = parameters;
	}

	public List<ValidationMessage> validate(Map<String, String> properties, Validator validator) {
		if (!this.applies(validator)) return Collections.emptyList();
		
		String val = properties.get(getProperty());
		if (val == null) val = "";
		if (pattern == null) {
			int flags = 0;
			if (modifiers != null) {
				if (modifiers.indexOf('i') != -1) flags &= Pattern.CASE_INSENSITIVE;
				if (modifiers.indexOf('m') != -1) flags &= Pattern.MULTILINE;
			}
			pattern = Pattern.compile(regexp, flags);
		}
		if (pattern.matcher(val).matches()) return Collections.emptyList();
		
		return Arrays.asList(new ValidationMessage(getProperty(), message, parameters));
	}
	
	
	
}
