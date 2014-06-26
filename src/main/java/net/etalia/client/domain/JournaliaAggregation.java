package net.etalia.client.domain;

import java.util.List;

public abstract class JournaliaAggregation<T extends Publication> {

	private String title;
	private List<Publication> data;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Publication> getData() {
		return data;
	}
	public void setData(List<Publication> data) {
		this.data = data;
	}

}
