package net.etalia.client.domain;

import java.util.LinkedList;
import java.util.List;

public class Article extends Entity {

	private String subtitle;
	private String body;
	private User author;
	private User owner;
	private Publication publication;
	private Language lang;
	private List<Media> gallery = new LinkedList<Media>();
	private List<String> tagFolk = new LinkedList<String>();
	private boolean searchable = true;
	private boolean vm18 = false;
	private String signer;
	private String originalUrl;
	private Long updated;
	private boolean published = false;

	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public Publication getPublication() {
		return publication;
	}
	public void setPublication(Publication publication) {
		this.publication = publication;
	}
	public Language getLang() {
		return lang;
	}
	public void setLang(Language lang) {
		this.lang = lang;
	}
	public void setLang(String lang) {
		if (lang == null) {
			lang = "en";
		}
		this.lang = Language.byISOCode(lang);
	}
	public List<Media> getGallery() {
		return gallery;
	}
	public void setGallery(List<Media> gallery) {
		this.gallery = gallery;
	}
	public List<String> getTagFolk() {
		return tagFolk;
	}
	public void setTagFolk(List<String> tagFolk) {
		this.tagFolk = tagFolk;
	}
	public boolean isSearchable() {
		return searchable;
	}
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	public boolean isVm18() {
		return vm18;
	}
	public void setVm18(boolean vm18) {
		this.vm18 = vm18;
	}
	public String getSigner() {
		return signer;
	}
	public void setSigner(String signer) {
		this.signer = signer;
	}
	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	public Long getUpdated() {
		return updated;
	}
	public void setUpdated(Long updated) {
		this.updated = updated;
	}
	public boolean isPublished() {
		return this.published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}

	public void addMedia(Media media) {
		if (this.gallery == null) {
			this.gallery = new LinkedList<Media>();
		}
		this.gallery.add(media);
	}

}
