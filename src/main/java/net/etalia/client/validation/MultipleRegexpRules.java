package net.etalia.client.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.etalia.client.json.EtaliaObjectMapper;
import net.etalia.jalia.ObjectMapper;

public class MultipleRegexpRules extends SinglePropertyRule {

	private RuleSet subrules = new RuleSet();
	
	MultipleRegexpRules(String property, List<Map<String, Object>> parsed) {
		super(property);
		for (Map<String,Object> val : parsed) {
			String message = (String) val.get("message");
			if (message == null) message = "ERROR";
			List<String> groupsLst = (List<String>) val.get("groups");
			String[] groups = null;
			if (groupsLst != null) {
				groups = groupsLst.toArray(new String[groupsLst.size()]);
			}
			
			Map<String,List<String>> res = (Map<String, List<String>>) val.get("re");
			for (List<String> re : res.values()) {
				// TODO parameters
				RegexpRule rule = new RegexpRule(property, re.get(0), re.get(1), message);
				rule.setGroups(groups);
				this.subrules.addRule(rule);
			}
		}
	}

	public List<ValidationMessage> validate(Map<String, String> properties, Validator validator) {
		if (!this.applies(validator)) return Collections.emptyList();
		return this.subrules.validate(properties, validator);
	}

}
