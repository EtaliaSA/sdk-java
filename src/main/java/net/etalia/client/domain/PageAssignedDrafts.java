package net.etalia.client.domain;

public class PageAssignedDrafts extends Page {

	private PaginationList<Article> articles;

	public PaginationList<Article> getArticles() {
		return articles;
	}

	public void setArticles(PaginationList<Article> articles) {
		this.articles = articles;
	}

}
