package net.etalia.client.domain;

public class StampPublication extends Entity {

	private User owner;

	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}

}
