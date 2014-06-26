package net.etalia.client.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Journalia {

	private List<Aggregation> data = new LinkedList<Aggregation>();

	public List<Aggregation> getData() {
		return data;
	}
	public void setData(List<Aggregation> data) {
		this.data = data;
	}

	public class Aggregation extends HashMap<String, Object> {
		private String title;
		private List<Publication> data;
	}

	public class SystemAggregation extends Aggregation {
	}

	public class StampAggregation extends Aggregation {
	}

	public class SuggestedAggregation extends Aggregation {
	}

}
