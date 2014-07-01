package net.etalia.client.domain;

public class JournaliaStampAggregation<T extends Publication> extends JournaliaAggregation<T> {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
