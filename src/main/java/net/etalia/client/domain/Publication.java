package net.etalia.client.domain;

import java.util.List;

public abstract class Publication extends Entity {

	private List<Page> showedPages;

	public List<Page> getShowedPages() {
		return showedPages;
	}
	public void setShowedPages(List<Page> showedPages) {
		this.showedPages = showedPages;
	}

}
