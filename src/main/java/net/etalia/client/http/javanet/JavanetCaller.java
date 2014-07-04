package net.etalia.client.http.javanet;

import java.lang.reflect.Type;

import net.etalia.client.http.Call;
import net.etalia.client.http.Caller;

public class JavanetCaller<Serv> extends Caller<Serv> {

	protected String baseUrl;

	public JavanetCaller(Class<Serv> serviceInterface) {
		super(serviceInterface);
	}	
	
	public JavanetCaller(Class<Serv> serviceInterface, String baseUrl) {
		super(serviceInterface);
		this.baseUrl = baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	@Override
	protected <X> Call<X> createCall(Type clazz, HttpMethod method, String path) {
		return new JavanetCall<X>(this, clazz, method, path);
	}

}
