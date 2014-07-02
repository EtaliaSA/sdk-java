package net.etalia.client.services;

import java.io.Reader;
import java.util.List;

/**
 * Creates instances of {@link ViewValidator} based on json configuration.
 * 
 * <p>
 * This object is initialized with a default set of validation rules loaded from the 
 * classpath.
 * </p>
 * 
 * @author Simone Gianni <simoneg@apache.org>
 */
public class ValidatorFactory {
	
	public ValidatorFactory() {
		init();
	}

	/**
	 * Initializes the service, loading default rules from the classpath if available.
	 */
	public void init() {}
	
	/**
	 * Initializes the service, loading default rules from the given reader.
	 * @param json A reader with rules in json format.
	 */
	public void init(Reader json) {}
	
	public ViewValidator getViewValidator(Class<?> clazz) {
		return null;
	}
	
	public ViewValidator getViewValidator(Reader json) {
		return null;
	}
	
}
