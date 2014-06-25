package net.etalia.client.domain;

public class PageQuery extends Page {

	private SearchCriteria searchCriteria;
	private Boolean free;

	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}
	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	public Boolean getFree() {
		return free;
	}
	public void setFree(Boolean free) {
		this.free = free;
	}

}
