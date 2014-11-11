package net.etalia.client.json;

import java.util.Collection;

import net.etalia.client.domain.Entity;
import net.etalia.jalia.DefaultOptions;
import net.etalia.jalia.JsonContext;
import net.etalia.jalia.ObjectMapper;

public class JsonEntityFieldsContext extends JsonContext {
	
	private JaliaDomainFactory factory = null;

	public JsonEntityFieldsContext(ObjectMapper mapper, JaliaDomainFactory factory) {
		super(mapper);
		this.factory = factory;
	}
	
	public boolean entering(String fieldName, Collection<String> defaults) {
		Object last = factory.lastObject;
		if (!(last instanceof Entity)) return super.entering(fieldName, defaults);
		boolean ret = super.entering(fieldName, ((Entity)last).getJsonUsedFields());
		if (ret) {
			super.putLocalStack(DefaultOptions.INCLUDE_EMPTY.toString(), true);
			super.putLocalStack(DefaultOptions.INCLUDE_NULLS.toString(), true);
		}
		return ret;
	}

}
