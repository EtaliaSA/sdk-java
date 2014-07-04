package net.etalia.client.http.javanet;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.etalia.client.http.Call;
import net.etalia.client.http.Response;
import net.etalia.client.json.EtaliaObjectMapper;
import net.etalia.jalia.BeanJsonDeSer;
import net.etalia.jalia.JsonDeSer;
import net.etalia.jalia.TypeUtil;

public class JavanetResponse<X> implements Response<X> {

	private final static Logger log = Logger.getLogger(JavanetResponse.class.getName());	
	
	private HttpURLConnection conn;
	private byte[] payload;
	private Type type;
	
	private boolean consumed = false;
	private X value;	

	public JavanetResponse(HttpURLConnection conn, byte[] respPayload, Type returnType) {
		this.conn = conn;
		this.payload = respPayload;
		this.type = returnType;
	}

	//@Override
	public X cast() {
		if (!consumed) {
			consumed = true;
			try {
				EtaliaObjectMapper eom = new EtaliaObjectMapper(true);
	
				if (log.isLoggable(Level.FINE)) {
					log.fine("Deserialize: "+ new String(payload));
				}
				
				value = Call.eom.readValue(payload, TypeUtil.get(type));				
			} catch (Exception e) {
				throw new RuntimeException("Error parsing body of response" + JavanetCall.httpCallTrace(null, null, payload), e);
			}
		}
		return value;
	}

	//@Override
	public Map<String, Object> asMap() {
		try {
			EtaliaObjectMapper eom = new EtaliaObjectMapper(true);

			if (log.isLoggable(Level.FINE)) {
				log.fine("Deserialize to map : " + new String(payload));
			}

			eom.init();
			Iterator<JsonDeSer> iter = eom.getRegisteredDeSers().iterator();
			while (iter.hasNext()) {
				if (iter.next() instanceof BeanJsonDeSer) {
					iter.remove();
					break;
				}
			}

			return eom.readValue(payload);
		} catch (Exception e) {
			throw new RuntimeException("Error parsing body of response" + JavanetCall.httpCallTrace(null, null, payload), e);
		}
	}

	//@Override
	public int getStatusCode() {
		try {
			return conn.getResponseCode();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	//@Override
	public Map<String, String> getHeaders() {
		Map<String, List<String>> allHeaders = conn.getHeaderFields();
		Map<String,String> headers = new HashMap<String,String>();
		for (Entry<String, List<String>> entry : allHeaders.entrySet()) {
			headers.put(entry.getKey(), entry.getValue().get(0));
		}
		return headers;
	}

}
