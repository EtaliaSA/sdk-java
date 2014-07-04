package net.etalia.client.domain;

import java.util.Set;

public class Banner extends Entity {

	private String token = "CLICKTOKEN";
	private boolean first;
	private BannerType type;
	private String imgUrl;
	private String htmlCode;
	private String linkUrl;
	private Set<String> annotations;
	
	public Banner() {}
	
	public void composeClickUrl(String base) {
		this.linkUrl = base + "?t=" + this.token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	
	public boolean isFirst() {
		return first;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}
	
	public Banner withToken(String token) {
		this.setToken(token);
		return this;
	}
	
	public BannerType getType() {
		return type;
	}
	public void setType(BannerType type) {
		this.type = type;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public String getHtmlCode() {
		return htmlCode;
	}
	public void setHtmlCode(String htmlCode) {
		this.htmlCode = htmlCode;
	}
	
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String lunkUrl) {
		this.linkUrl = lunkUrl;
	}
	
	public int getWidth() {
		return getType().getWidth();
	}
	
	public int getHeight() {
		return getType().getHeight();
	}
	
	public Set<String> getAnnotations() {
		return annotations;
	}
	public void setAnnotations(Set<String> annotations) {
		this.annotations = annotations;
	}
	
}
