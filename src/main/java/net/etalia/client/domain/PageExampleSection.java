package net.etalia.client.domain;

import java.util.LinkedList;
import java.util.List;

import net.etalia.client.domain.PageExample;

public class PageExampleSection extends Entity {

	private List<PageExample> pages = new LinkedList<PageExample>();

	public List<PageExample> getPages() {
		return pages;
	}
	@SuppressWarnings("unused")
	private void setPages(List<PageExample> pages) {
		this.pages = pages;
	}

}
