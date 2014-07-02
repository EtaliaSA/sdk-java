package net.etalia.client.domain;

public class PublicationStandard extends Publication {

	private Media logo;
	private Media header;
	private Boolean published;
	private Long publishedDate;
	private Boolean free;
	private User owner;

	public Media getLogo() {
		return logo;
	}
	public void setLogo(Media logo) {
		this.logo = logo;
	}
	public Media getHeader() {
		return header;
	}
	public void setHeader(Media header) {
		this.header = header;
	}
	public Boolean getPublished() {
		return published;
	}
	public void setPublished(Boolean published) {
		this.published = published;
	}
	public Long getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(Long publishedDate) {
		this.publishedDate = publishedDate;
	}
	public Boolean getFree() {
		return free;
	}
	public void setFree(Boolean free) {
		this.free = free;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}

}
