package net.etalia.client.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Journalia {

	private List<Section> data = new LinkedList<Section>();

	public List<Section> getData() {
		return data;
	}
	public void setData(List<Section> data) {
		this.data = data;
	}

	private class Section extends HashMap<String, Object> {
		private String title;
		private List<Publication> data;
	}

	private class Static extends Section {
	}

	private class Stamp extends Section {
	}

	private class SuggestedStamp extends Section {
	}

}
