package net.etalia.client.json;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Media;
import net.etalia.client.domain.Publication;
import net.etalia.client.domain.User;
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
		map(Media.class, "Media");
		map(Publication.class, "Publication");
		map(User.class, "User");
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
		ret.ignoreGetter("id");
		ret.ignoreSetter("id");
		classDataFactory.cache(clazz, ret);
		return ret;
	}
	
	/**
	 * Uses {@link Persistent#getId()} to fetch the id.
	 */
	public String getId(Object entity, JsonContext context) {
		if (entity instanceof Article) {
			return ((Article) entity).getId();
		} else if (entity instanceof Media) {
			return ((Media) entity).getId();
		} else if (entity instanceof Publication) {
			return ((Publication) entity).getId();
		} else if (entity instanceof User) {
			return ((User) entity).getId();
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
