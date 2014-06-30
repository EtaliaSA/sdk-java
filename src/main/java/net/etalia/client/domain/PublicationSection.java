package net.etalia.client.domain;

import java.util.LinkedList;
import java.util.List;

public class PublicationSection extends Entity {

	private List<Publication> internals = new LinkedList<Publication>();
	private List<Publication> externals = new LinkedList<Publication>();
	private Language lang;
	private Boolean main = Boolean.FALSE;

	public List<Publication> getInternals() {
		return internals;
	}
	public void setInternals(List<Publication> internals) {
		this.internals = internals;
	}
	public List<Publication> getExternals() {
		return externals;
	}
	public void setExternals(List<Publication> externals) {
		this.externals = externals;
	}
	public Language getLang() {
		return lang;
	}
	public void setLang(Language lang) {
		this.lang = lang;
	}
	public Boolean getMain() {
		return main;
	}
	public void setMain(Boolean main) {
		this.main = main;
	}

}
