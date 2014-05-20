package net.etalia.client.domain;

public class Media {

	private String id;
	private String description;
	private String url;
	private MediaType type;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public MediaType getType() {
		return type;
	}
	public void setType(MediaType type) {
		this.type = type;
	}

	public enum MediaType {
		_image(),
		_video();
		private MediaType() {
		}
	}

}
