package net.etalia.client.json;

import java.util.HashMap;
import java.util.Map;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Entity;
import net.etalia.client.domain.Journalia;
import net.etalia.client.domain.JournaliaStampAggregation;
import net.etalia.client.domain.JournaliaSuggestedAggregation;
import net.etalia.client.domain.JournaliaSystemAggregation;
import net.etalia.client.domain.Media;
import net.etalia.client.domain.PageExample;
import net.etalia.client.domain.PageLink;
import net.etalia.client.domain.PageQuery;
import net.etalia.client.domain.PaginationList;
import net.etalia.client.domain.PublicationOwner;
import net.etalia.client.domain.PublicationSection;
import net.etalia.client.domain.PublicationStamps;
import net.etalia.client.domain.PublicationStandard;
import net.etalia.client.domain.SearchCriteria;
import net.etalia.client.domain.StampArticle;
import net.etalia.client.domain.StampPublication;
import net.etalia.client.domain.User;
import net.etalia.client.domain.UserProfile;
import net.etalia.jalia.EntityFactory;
import net.etalia.jalia.EntityNameProvider;
import net.etalia.jalia.JsonClassData;
import net.etalia.jalia.JsonClassDataFactory;
import net.etalia.jalia.JsonClassDataFactoryImpl;
import net.etalia.jalia.JsonContext;

/**
 * Implements Etalia names, entity factory and class data factory.
 * 
 * 
 * @author Simone Gianni <simoneg@apache.org>
 */
public class JaliaDomainFactory implements EntityNameProvider, EntityFactory, JsonClassDataFactory {

	private JsonClassDataFactoryImpl classDataFactory = new JsonClassDataFactoryImpl();
	private Map<Class<?>, String> nameMappingsByClass = new HashMap<Class<?>, String>();
	private Map<String, Class<?>> nameMappingsByName = new HashMap<String, Class<?>>();
	
	{
		map(Article.class, "Article");
		map(Journalia.class, "Journalia");
		map(JournaliaSystemAggregation.class, "SystemAggregation");
		map(JournaliaStampAggregation.class, "StampAggregation");
		map(JournaliaSuggestedAggregation.class, "SuggestedAggregation");
		map(Media.class, "Media");
		map(PageExample.class, "PageExample");
		map(PageLink.class, "PageLink");
		map(PageQuery.class, "PageQuery");
		map(PaginationList.class, "PaginationList");
		map(PublicationOwner.class, "PublicationOwner");
		map(PublicationSection.class, "PublicationSection");
		map(PublicationStamps.class, "PublicationStamps");
		map(PublicationStandard.class, "PublicationStandard");
		map(SearchCriteria.class, "SearchCriteria");
		map(StampArticle.class, "StampArticle");
		map(StampPublication.class, "StampPublication");
		map(User.class, "User");
		map(UserProfile.class, "UserProfile");
	}
	
	private void map(Class<?> clazz, String name) {
		nameMappingsByClass.put(clazz, name);
		nameMappingsByName.put(name, clazz);
	}

	/**
	 * Uses default domain mappings for Etalia
	 */
	public String getEntityName(Class<?> clazz) {
		String ret = nameMappingsByClass.get(clazz);
		if (ret == null) {
			// TODO parse an annotation?
		}
		return ret;
	}

	/**
	 * Uses default domain mappings for Etalia
	 */
	public Class<?> getEntityClass(String name) {
		return nameMappingsByName.get(name);
	}

	
	public JsonClassData getClassData(Class<?> clazz, JsonContext context) {
		JsonClassData ret = classDataFactory.getClassData(clazz, context);
		if (!ret.isNew()) return ret;
		classDataFactory.cache(clazz, ret);
		return ret;
	}
	
	/**
	 * Uses {@link Persistent#getId()} to fetch the id.
	 */
	public String getId(Object entity, JsonContext context) {
		if (entity instanceof Entity) {
			return ((Entity) entity).getId();
		} else if (entity instanceof Media) {
			return ((Media) entity).getId();
		}
		return null;
	}

	public Object buildEntity(Class<?> clazz, String id, JsonContext context) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public Object prepare(Object obj, boolean serializing, JsonContext context) {
		return obj;
	}

	public Object finish(Object obj, boolean serializing, JsonContext context) {
		return obj;
	}

}
