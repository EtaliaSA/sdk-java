package net.etalia.client.domain;

public class User extends Entity {

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

	// Boolean to handle field visibility
	private Boolean showFullname;
	private Boolean showGender;
	private Boolean showBirthdate;
	private Boolean showEmail;

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

	public Boolean getShowBirthdate() {
		return this.showBirthdate;
	}
	public void setShowBirthdate(Boolean showBirthdate) {
		this.showBirthdate = showBirthdate;
	}
	public Boolean getShowEmail() {
		return this.showEmail;
	}
	public void setShowEmail(Boolean showEmail) {
		this.showEmail = showEmail;
	}
	public Boolean getShowGender() {
		return this.showGender;
	}
	public void setShowGender(Boolean showGender) {
		this.showGender = showGender;
	}
	public Boolean getShowFullname() {
		return this.showFullname;
	}
	public void setShowFullname(Boolean showFullname) {
		this.showFullname = showFullname;
	}

	public enum UserGender {
		_male(),
		_female();
		private UserGender() {
		}
	}

}
