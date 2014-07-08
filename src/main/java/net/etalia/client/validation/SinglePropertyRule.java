package net.etalia.client.validation;

public abstract class SinglePropertyRule implements Rule {

	
	private String property;
	private String[] groups;

	public String getProperty() {
		return property;
	}

	public SinglePropertyRule(String property) {
		this.property = property;
	}
	
	public void setGroups(String[] groups) {
		this.groups = groups;
	}
	public String[] getGroups() {
		return groups;
	}
	
	protected boolean applies(Validator val) {
		if (val == null) return true;
		if (!val.hasProperty(property)) return false;
		if (groups == null) return true;
		for (String group : groups) {
			if (val.hasGroup(group)) return true;
		}
		return false;
	}
}
