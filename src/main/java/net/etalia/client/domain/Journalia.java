package net.etalia.client.domain;

import java.util.LinkedList;
import java.util.List;

public class Journalia {

	private List<JournaliaAggregation<Publication>> data = new LinkedList<JournaliaAggregation<Publication>>();

	public List<JournaliaAggregation<Publication>> getData() {
		return data;
	}
	public void setData(List<JournaliaAggregation<Publication>> data) {
		this.data = data;
	}

}
