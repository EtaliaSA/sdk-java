package net.etalia.client.domain;

public abstract class Page extends Entity {

	private Publication publication;

	public Publication getPublication() {
		return publication;
	}
	public void setPublication(Publication publication) {
		this.publication = publication;
	}

}
