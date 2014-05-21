package net.etalia.client.domain;

import java.util.Collections;
import java.util.Map;

public class User {

	private String id;
	private String title;
	private Map<String, Object> extraData;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Map<String,Object> getExtraData() {
		if (this.extraData == null) return Collections.emptyMap();
		return Collections.unmodifiableMap(this.extraData);
	}
	@SuppressWarnings("unchecked")
	public <T> T getExtraData(String name) {
		if (extraData == null) return null;
		return (T)extraData.get(name);
	}

}
