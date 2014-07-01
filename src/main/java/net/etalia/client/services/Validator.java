package net.etalia.client.services;

import java.util.List;

public class Validator {

	public static class ValidationMessage {
		public String field;
		public String message;
		public String[] parameters;
	}
	
	public void init() {}
	
	public void cleanRules() {}
	
	public void addRules(String json) {}
	
	public List<ValidationMessage> validate(Object bean) {
		return null;
	}
	
	public List<ValidationMessage> validateGroup(Object bean, String... groups) {
		return null;
	}
	
	public List<ValidationMessage> validateFields(Object bean, String... fields) {
		return null;
	}
	
}
