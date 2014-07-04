package net.etalia.client.http.httpclient;

import java.lang.reflect.Type;

import net.etalia.client.http.Call;
import net.etalia.client.http.Caller;
import net.etalia.client.http.Caller.HttpMethod;

import org.apache.http.client.HttpClient;

public class HttpClientCaller<Serv> extends Caller<Serv> {

	protected HttpClient httpClient;
	protected String baseUrl;
	
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	
	public HttpClientCaller(Class<Serv> serviceInterface) {
		super(serviceInterface);
	}

	@Override
	protected <X> Call<X> createCall(Type type, HttpMethod method, String path) {
		return new HttpClientCall<X>(this, type, method, path);
	}

	
	
}
