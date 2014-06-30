package net.etalia.client.domain;

public class PageLink extends Page {

	private Boolean free;
	private PageExample page;

	public Boolean getFree() {
		return free;
	}
	public void setFree(Boolean free) {
		this.free = free;
	}
	public PageExample getPage() {
		return page;
	}
	public void setPage(PageExample page) {
		this.page = page;
	}

}
