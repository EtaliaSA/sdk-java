package net.etalia.client.http.httpclient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.etalia.client.http.Call;
import net.etalia.client.http.Response;
import net.etalia.client.json.EtaliaObjectMapper;
import net.etalia.jalia.BeanJsonDeSer;
import net.etalia.jalia.JsonDeSer;
import net.etalia.jalia.TypeUtil;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class HttpClientResponse<X> implements Response<X> {

	private Type type;
	private HttpResponse httpresp;
	private byte[] payload;
	
	private boolean consumed = false;
	private X value;

	public HttpClientResponse(HttpResponse httpresp, byte[] payload, Type type) {
		this.httpresp = httpresp;
		this.payload = payload;
		this.type = type;
	}
	
	public X cast() {
		if (!consumed) {
			consumed = true;
			value = Call.eom.readValue(payload, TypeUtil.get(type));
		}
		return value;
	}

	public int getStatusCode() {
		return this.httpresp.getStatusLine().getStatusCode();
	}

	public Map<String, String> getHeaders() {
		Header[] allHeaders = this.httpresp.getAllHeaders();
		Map<String,String> ret = new HashMap<String, String>();
		for (Header header : allHeaders) {
			ret.put(header.getName(), header.getValue());
		}
		return ret;
	}

	public Map<String, Object> asMap() {
		EtaliaObjectMapper eom = new EtaliaObjectMapper(true);
		eom.init();
		Iterator<JsonDeSer> iter = eom.getRegisteredDeSers().iterator();
		while (iter.hasNext()) {
			if (iter.next() instanceof BeanJsonDeSer) {
				iter.remove();
				break;
			}
		}

		return eom.readValue(payload);
	}
	
}
