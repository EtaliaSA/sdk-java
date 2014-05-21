package net.etalia.client.json;

import javax.annotation.PostConstruct;

import net.etalia.jalia.ObjectMapper;
import net.etalia.jalia.stream.JsonReader;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class EtaliaObjectMapper extends ObjectMapper {	
	
	public EtaliaObjectMapper(boolean client) {
	}
	
	public EtaliaObjectMapper() {
		this(false);
	}
	
	/**
	 * Perform default Etalia configuration
	 */
	@Override
	@PostConstruct
	public void init() {
		if (super.inited) return;
		JaliaDomainFactory factory = new JaliaDomainFactory();
		super.setEntityFactory(factory);
		super.setEntityNameProvider(factory);
		super.setClassDataFactory(factory);
		
		super.setSendNulls(false);
		super.setSendEmpty(false);
		
		super.init();
	}
	
	@Override
	protected JsonReader configureReader(JsonReader reader) {
		JsonReader ret = super.configureReader(reader);
		// Set the reader lenient, we want to accept some errors
		ret.setLenient(true);
		return ret;
	}
		
}
