package net.etalia.client.domain;

import java.util.HashMap;
import java.util.Map;

public class Entity {

	private String id;
	private String title;
	private Map<String, Object> extraData = new HashMap<String, Object>();

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
		return extraData;
	}
	@SuppressWarnings("unchecked")
	public <T> T getExtraData(String name) {
		if (extraData == null) return null;
		return (T)extraData.get(name);
	}
	public <T> T setExtraData(String name, Object value) {
		if (extraData == null) extraData = new HashMap<String, Object>();
		T ret = getExtraData(name);
		getExtraData().put(name, value);
		return ret;
	}

}
