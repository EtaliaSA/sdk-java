package net.etalia.client.domain;

public class StampArticle extends Entity {

	private User owner;

	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}

}
