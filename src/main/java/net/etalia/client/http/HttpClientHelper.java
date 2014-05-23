package net.etalia.client.http;

import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;

public class HttpClientHelper {

	public HttpClient createDefaultClient(int connections, int timeout) {
		PoolingClientConnectionManager cm = 
			new PoolingClientConnectionManager(
				SchemeRegistryFactory.createDefault(),
				timeout, TimeUnit.MILLISECONDS
			);
		cm.setDefaultMaxPerRoute(connections);
		cm.setMaxTotal(connections);
		
		DefaultHttpClient ret = new DefaultHttpClient(cm);
		
		ret.getParams().setParameter("http.socket.timeout", timeout);
		ret.getParams().setParameter("http.connection.timeout", timeout);
		//ret.getParams().setParameter("http.connection-manager.timeout", timeout);
		ret.getParams().setParameter("http.protocol.head-body-timeout", timeout);
		return ret;
	}

	public HttpClient createDefaultClient(int connections, int timeout, SSLConnectionSocketFactory sslsf) {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setSSLSocketFactory(sslsf);
		clientBuilder.setMaxConnPerRoute(connections);
		clientBuilder.setMaxConnTotal(connections);
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		requestBuilder.setConnectTimeout(timeout);
		requestBuilder.setConnectionRequestTimeout(timeout);
		clientBuilder.setDefaultRequestConfig(requestBuilder.build());
		CloseableHttpClient client = clientBuilder.build();
		return client;
	}
}
