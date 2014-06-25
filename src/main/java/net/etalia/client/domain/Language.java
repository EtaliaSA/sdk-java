package net.etalia.client.domain;

import java.util.Locale;

public enum Language {

	it("it"),
	en("en");

	private String isocode; // ISO-639-1
	private Language(String isocode) {
		this.isocode = isocode;
	}
	public String getISOCode() {
		return isocode;
	}
	public static Language byISOCode(String isocode) {
		for(Language l : Language.values()) {
			if(l.isocode.equals(isocode)) {
				return l;
			}
		}
		return null;
	}
	public Locale getLocale() {
		return new Locale(this.isocode);
	}

}
