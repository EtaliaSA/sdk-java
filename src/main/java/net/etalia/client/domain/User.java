package net.etalia.client.domain;

import java.util.HashMap;
import java.util.Map;

public class User {

	private String id;
	private String title;
	private Map<String, Object> extraData = new HashMap<String, Object>();
	private String email;
	private String firstname;
	private String lastname;
	private String facebook;
	private UserGender gender;
	private Long birthdate;
	private UserProfile defaultProfile;
	private Language lang;
	private Media photo;
	private Media cover;
	private String visibleTitle;

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
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserGender getGender() {
		return gender;
	}
	public void setGender(UserGender gender) {
		this.gender = gender;
	}
	public Long getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Long birthdate) {
		this.birthdate = birthdate;
	}
	public UserProfile getDefaultProfile() {
		return defaultProfile;
	}
	public void setDefaultProfile(UserProfile defaultProfile) {
		this.defaultProfile = defaultProfile;
	}
	public Language getLang() {
		return lang;
	}
	public void setLang(Language lang) {
		this.lang = lang;
	}
	public Media getPhoto() {
		return photo;
	}
	public void setPhoto(Media photo) {
		this.photo = photo;
	}
	public Media getCover() {
		return cover;
	}
	public void setCover(Media cover) {
		this.cover = cover;
	}
	public String getVisibleTitle() {
		return visibleTitle;
	}
	public void setVisibleTitle(String visibleTitle) {
		this.visibleTitle = visibleTitle;
	}

	public enum UserGender {
		_male(),
		_female();
		private UserGender() {
		}
	}

}
