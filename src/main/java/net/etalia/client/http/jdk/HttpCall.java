package net.etalia.client.http.jdk;

import gumi.builders.UrlBuilder;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.etalia.client.http.Call;
import net.etalia.client.http.Caller.HttpMethod;
import net.etalia.client.http.Response;
import net.etalia.client.utils.Utils;

public class HttpCall<X> extends Call<X> {

	private final static Logger log = Logger.getLogger(HttpCall.class.getName());

	private HttpCaller caller;

	public HttpCall(HttpCaller caller, Type type, HttpMethod method, String path) {
		super(type, method, path);
		this.caller = caller;
	}

	@Override
	public Response<X> execute(boolean check) {
		String uri = Utils.pathConcat(this.caller.getBaseUrl(), super.path);
		if (System.getProperty("embedWebPort") != null)
			uri = uri.replace("${embedWebPort}", System.getProperty("embedWebPort"));

		//HttpUriRequest message = null;
		
		UrlBuilder ub = UrlBuilder.fromString(uri);
		
		if (hasFields()) {
			// Always add requested fields as a parameter
			// TODO we should optimize this later, using sets or whatever
			ub.addParameter("outProperties", getFieldsAsString());
			uri = ub.toString();
		}
		
		if (super.method == HttpMethod.GET || super.method == HttpMethod.DELETE) {
			Utils.assertNull("Cannot send a body with a get request", super.requestBody);
			if (hasParameters()) {
				for (Entry<String, Object> entry : super.requestParameters.entrySet()) {
					Object value = entry.getValue();
					if (value == null) continue;
					if (value.getClass().isArray()) {
						// This may not return what is expected
						value = Arrays.asList(value);
					}
					if (value instanceof Collection) {
						for (Object inval : ((Collection<Object>)value)) {
							ub.addParameter(entry.getKey(), convertToString(inval));
						}
					} else {
						ub.addParameter(entry.getKey(), convertToString(value));
					}
				}
				uri = ub.toString();
			} 
			if (super.method == HttpMethod.GET) {
				//message = new HttpGet(uri);
			} else {
				//message = new HttpDelete(uri);
			}
		}
/*		} else if (super.method == HttpMethod.POST || super.method == HttpMethod.PUT) {
			Utils.assertFalse("Cannot send both parameters and a body in a POST or PUT", hasParameters() && hasBody());
			byte[] payload = null;
			if (hasParameters()) {
				List<NameValuePair> nvp = new ArrayList<NameValuePair>();
				for (Entry<String, Object> entry : super.requestParameters.entrySet()) {
					Object value = entry.getValue();
					if (value == null) continue;
					if (value.getClass().isArray()) {
						// This may not return what is expected
						value = Arrays.asList((Object[])value);
					}
					String name = entry.getKey();
					if (name.endsWith("[]")) {
						name = name.substring(0, name.length() - 2);
					}
					
					if (value instanceof Collection) {
						for (Object inval : ((Collection<Object>)value)) {
							nvp.add(new BasicNameValuePair(name, convertToString(inval)));
						}
					} else {
						nvp.add(new BasicNameValuePair(name, convertToString(value)));
					}
				}
				payload = URLEncodedUtils.format(nvp, "utf8").getBytes();
			} else if (hasBody()) {
				payload = super.requestBody;
			}
			if (payload == null) payload = new byte[0];
			
			HttpEntity entity = new ByteArrayEntity(payload);
			if (super.method == HttpMethod.POST) {
				HttpPost post = new HttpPost(uri);
				post.setEntity(entity);
				message = post;
			} else {
				HttpPut put = new HttpPut(uri);
				put.setEntity(entity);
				message = put;
			}
			
			if (!hasParameters() && hasBody()) {
				message.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
			} else {
				message.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");
			}
		}
		
		if (hasHeaders()) {
			for (Entry<String, String> entry : requestHeaders.entrySet()) {
				message.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		HttpResponse httpresp = null;
		byte[] payload = null;
		try {
			httpresp = caller.getHttpClient().execute(message);
			// If there is an entity, read it now and consume it, to avoid connection leaks
			HttpEntity entity = httpresp.getEntity();
			if (entity != null) {
				payload = EntityUtils.toByteArray(entity);
				EntityUtils.consumeQuietly(entity);
			}
			log.log(Level.FINE, httpCallTrace(message, requestBody, httpresp, payload));
		} catch (Exception e) {
			throw new RuntimeException("Error executing HTTP call" + httpCallTrace(message, requestBody, httpresp, null), e);
		}
		
		
		int statusCode = httpresp.getStatusLine().getStatusCode();
		if (check && (statusCode < 200 || statusCode > 299)) {
			Header respContentType = httpresp.getFirstHeader(HttpHeaders.CONTENT_TYPE);
			if (respContentType != null && respContentType.getValue().indexOf("json") != -1) {
				Map<String,Object> readValue = null;
				try {
					readValue = eom.readValue(payload, new TypeUtil.Specific<Map<String,Object>>(){}.type());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (readValue != null) throw new JsonedException("Calling\n" + uri + "\nreturned\n" + httpresp.getStatusLine()+ httpCallTrace(message, requestBody, httpresp, payload), readValue, statusCode);
			}
			throw new IllegalStateException("Calling\n" + uri + "\nreturned\n" + httpresp.getStatusLine() + httpCallTrace(message, requestBody, httpresp, payload));
		}
		
		try {
			return new HttpClientResponse<X>(httpresp, payload, this.returnType);
		} catch (Throwable e) {
			throw new RuntimeException("Error parsing response from " + uri + "\n" + httpresp.getStatusLine() + httpCallTrace(message, requestBody, httpresp, null), e);
		}
		*/
		
		
		// TODO Auto-generated method stub
		return null;
	}

}
