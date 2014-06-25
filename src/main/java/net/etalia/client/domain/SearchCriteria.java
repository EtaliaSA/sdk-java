package net.etalia.client.domain;

import java.util.LinkedHashSet;
import java.util.Set;

public class SearchCriteria {

	String tags; // comma sep
	Set<User> authorsFilter = new LinkedHashSet<User>();
	Boolean invertAuthorsFilter;
	Set<Publication> publicationsFilter = new LinkedHashSet<Publication>();
	Boolean invertPublicationsFilter;
	Set<StampArticle> stampsFilter = new LinkedHashSet<StampArticle>();
	Long minDateFilter = null;
	Long maxDateFilter = null;
	Long recencyFilter = null;
	Language lang;
	String userProfileSimilarityId;
	Boolean vm18 = null;

	Long popularityBoost = 0L;
	Long recencyBoost = 5L;
	Long profileBoost = 2L;
	Long relevancyBoost = 3L;

	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public Set<User> getAuthorsFilter() {
		return authorsFilter;
	}
	public void setAuthorsFilter(Set<User> authorsFilter) {
		this.authorsFilter = authorsFilter;
	}
	public Set<Publication> getPublicationsFilter() {
		return publicationsFilter;
	}
	public void setPublicationsFilter(Set<Publication> publicationsFilter) {
		this.publicationsFilter = publicationsFilter;
	}
	public Set<StampArticle> getStampsFilter() {
		return stampsFilter;
	}
	public void setStampsFilter(Set<StampArticle> stampsFilter) {
		this.stampsFilter = stampsFilter;
	}
	public Long getMinDateFilter() {
		return minDateFilter;
	}
	public void setMinDateFilter(Long minDateFilter) {
		this.minDateFilter = minDateFilter;
	}
	public Long getMaxDateFilter() {
		return maxDateFilter;
	}
	public void setMaxDateFilter(Long maxDateFilter) {
		this.maxDateFilter = maxDateFilter;
	}
	public Long getRecencyFilter() {
		return recencyFilter;
	}
	public void setRecencyFilter(Long recencyFilter) {
		this.recencyFilter = recencyFilter;
	}
	public Language getLang() {
		return lang;
	}
	public void setLang(Language lang) {
		if (lang == null) {
			lang = Language.en;
		}
		this.lang = lang;
	}
	public String getUserProfileSimilarityId() {
		return userProfileSimilarityId;
	}
	public void setUserProfileSimilarityId(String userProfileSimilarityId) {
		this.userProfileSimilarityId = userProfileSimilarityId;
	}
	public Boolean getVm18() {
		return vm18;
	}
	public void setVm18(Boolean vm18) {
		this.vm18 = vm18;
	}
	public Long getPopularityBoost() {
		return popularityBoost;
	}
	public void setPopularityBoost(Long popularityBoost) {
		this.popularityBoost = popularityBoost;
	}
	public Long getRelevancyBoost() {
		return relevancyBoost;
	}
	public void setRelevancyBoost(Long relevancyBoost) {
		this.relevancyBoost = relevancyBoost;
	}
	public Long getProfileBoost() {
		return profileBoost;
	}
	public void setProfileBoost(Long profileBoost) {
		this.profileBoost = profileBoost;
	}
	public Long getRecencyBoost() {
		return recencyBoost;
	}
	public void setRecencyBoost(Long recencyBoost) {
		this.recencyBoost = recencyBoost;
	}
	public Boolean getInvertAuthorsFilter() {
		return invertAuthorsFilter;
	}
	public void setInvertAuthorsFilter(Boolean invertAuthorsFilter) {
		this.invertAuthorsFilter = invertAuthorsFilter;
	}
	public Boolean getInvertPublicationsFilter() {
		return invertPublicationsFilter;
	}
	public void setInvertPublicationsFilter(Boolean invertPublicationsFilter) {
		this.invertPublicationsFilter = invertPublicationsFilter;
	}

}
