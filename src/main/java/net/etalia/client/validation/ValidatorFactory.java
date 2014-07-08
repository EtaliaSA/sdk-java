package net.etalia.client.validation;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.etalia.jalia.ObjectMapper;
import net.etalia.jalia.TypeUtil;

/**
 * Creates instances of {@link ViewValidator} based on json configuration.
 * 
 * <p>
 * This object has to be inizialized calling {@link #parseRules(Class, Reader)} before calling
 * {@link #getValidator(Class)} for a given class.  
 * </p>
 * 
 * @author Simone Gianni <simoneg@apache.org>
 */
public class ValidatorFactory {
	
	private Map<Class<?>, RuleSet> rules = new HashMap<Class<?>, RuleSet>();

	/**
	 * Parses a set of rules to create a Validator for the given class.
	 * @param json A reader with rules in json format.
	 */
	public void parseRules(Class<?> beanClass, Reader json) {
		RuleSet brules = new RuleSet();
		ObjectMapper om = new ObjectMapper();
		Map<String,List<Map<String,Object>>> pjson = om.readValue(json, TypeUtil.get(Map.class));
		for (Entry<String, List<Map<String, Object>>> entry : pjson.entrySet()) {
			MultipleRegexpRules mrr = new MultipleRegexpRules(entry.getKey(), entry.getValue());
			brules.addRule(mrr);
		}
		rules.put(beanClass, brules);
	}
	
	/**
	 * Creates a validator initialized with the common rules for the given class.
	 * @param clazz Entity class that serves as a basis for the validation.
	 * @return A validator initialized with proper rules. 
	 */
	public Validator getValidator(Class<?> clazz) {
		RuleSet brules = rules.get(clazz);
		RuleSet nrules = new RuleSet();
		nrules.setParent(brules);
		return new Validator(nrules);
	}
}
