package net.etalia.client.validation;

public abstract class SinglePropertyRule implements Rule {

	private String property;

	public String getProperty() {
		return property;
	}

	public SinglePropertyRule(String property) {
		this.property = property;
	}
	
}
