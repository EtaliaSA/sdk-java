package net.etalia.client.validation;

import java.io.Reader;
import java.util.List;

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
	
	/**
	 * Parses a set of rules to create a Validator for the given class.
	 * @param json A reader with rules in json format.
	 */
	public void parseRules(Class<?> beanClass, Reader json) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Creates a validator initialized with the common rules for the given class.
	 * @param clazz Entity class that serves as a basis for the validation.
	 * @return A validator initialized with proper rules. 
	 */
	public Validator getValidator(Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}
}
