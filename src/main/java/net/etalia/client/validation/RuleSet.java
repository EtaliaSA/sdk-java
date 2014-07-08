package net.etalia.client.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RuleSet implements Rule {

	private RuleSet parent = null;
	private Collection<Rule> rules = new ArrayList<Rule>();
	
	public void addRule(Rule rule) {
		rules.add(rule);
	}

	public void setParent(RuleSet parent) {
		this.parent = parent;
	}
	
	public List<ValidationMessage> validate(Map<String, String> properties, Validator validator) {
		List<ValidationMessage> ret = new ArrayList<ValidationMessage>();
		if (parent != null) ret.addAll(parent.validate(properties, validator));
		for (Rule rule : rules) {
			ret.addAll(rule.validate(properties, validator));
		}
		return ret;
	}

}
