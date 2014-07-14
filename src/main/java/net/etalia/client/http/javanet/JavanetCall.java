package net.etalia.client.http.javanet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.etalia.client.http.Call;
import net.etalia.client.http.JsonedException;
import net.etalia.client.http.Caller.HttpMethod;
import net.etalia.client.http.Response;
import net.etalia.client.utils.URIBuilder;
import net.etalia.client.utils.Utils;
import net.etalia.client.utils.URIBuilder.NameValuePair;
import net.etalia.jalia.TypeUtil;

public class JavanetCall<Ret> extends Call<Ret> {

	private final static Logger log = Logger.getLogger(JavanetCall.class.getName());
	
	private JavanetCaller<?> caller;

	public JavanetCall(JavanetCaller<?> caller, Type type, HttpMethod method, String path) {
		super(type, method, path);
		this.caller = caller;
	}

	@Override
	public Response<Ret> execute(boolean check) {
		String uri = Utils.pathConcat(this.caller.getBaseUrl(), super.path);
		if (System.getProperty("embedWebPort") != null)
			uri = uri.replace("${embedWebPort}", System.getProperty("embedWebPort"));
		
		HttpURLConnection conn = null;
		byte[] payload = null;
		URIBuilder ub = null;
		try {
			ub = new URIBuilder(uri);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Error parsing the uri " + uri, e);
		}
		
		if (hasFields()) {
			// Always add requested fields as a parameter
			// TODO we should optimize this later, using sets or whatever
			ub.addParameter("outProperties", getFieldsAsString());
			try {
				uri = ub.build().toString();
			} catch (URISyntaxException e) {
				throw new IllegalStateException("Error adding outProperties parameter to the uri " + uri, e);
			}				
		}

		try {
			if (super.method == HttpMethod.GET || super.method == HttpMethod.DELETE) {
				Utils.assertNull("Cannot send a body with a get request", super.requestBody);
				if (hasParameters()) {
					try {
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
						uri = ub.build().toString();
					} catch (URISyntaxException e) {
						throw new IllegalStateException("Error adding parameters to the uri " + uri, e);
					}
				} 
				conn = (HttpURLConnection) new URL(uri).openConnection();
				if (super.method == HttpMethod.GET) {
					conn.setRequestMethod("GET");
				} else {
					conn.setRequestMethod("DELETE");
				}
			} else if (super.method == HttpMethod.POST || super.method == HttpMethod.PUT) {
				Utils.assertFalse("Cannot send both parameters and a body in a POST or PUT", hasParameters() && hasBody());
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
								nvp.add(new NameValuePair(name, convertToString(inval)));
							}
						} else {
							nvp.add(new NameValuePair(name, convertToString(value)));							
						}
					}
					payload = URIBuilder.format(nvp, URIBuilder.UTF_8).getBytes();
				} else if (hasBody()) {
					payload = super.requestBody;
				}
				if (payload == null) payload = new byte[0];
				
				conn = (HttpURLConnection) new URL(uri).openConnection();
				
				if (super.method == HttpMethod.POST) {
					conn.setRequestMethod("POST");
				} else {
					conn.setRequestMethod("PUT");
				}
				conn.setDoOutput(true);
				
				if (!hasParameters() && hasBody()) {
					conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");				
				} else {
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
				}
			}
			
			if (hasHeaders()) {
				for (Entry<String, String> entry : requestHeaders.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
		} catch (Throwable t) {
			throw new RuntimeException("Error preparing call to " + uri, t);
		}
		
		int statusCode = 0; 		
		byte[] respPayload = null;
		try {
			if (payload != null) {
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(payload);
				os.flush();
				os.close();
			}
			
			statusCode = conn.getResponseCode();
			
			InputStream is = conn.getErrorStream();
			if (is == null) {
				is = conn.getInputStream();
			}
			if (is != null) {
				respPayload = Utils.toByteArray(is);
				Utils.closeQuietly(is);
				if (respPayload != null && respPayload.length == 0) respPayload = null;
			}
		} catch (Exception e) {
			throw new RuntimeException("Error executing HTTP call" + httpCallTrace(conn, payload, null), e);
		}
		
		
		if (log.isLoggable(Level.FINEST)) {
			log.finest(httpCallTrace(conn, payload, respPayload));
		}

		
		if (check && !isAcceptable(statusCode)) {
			String contentTypeHeader = conn.getHeaderField("Content-Type");
			if (contentTypeHeader != null && contentTypeHeader.indexOf("json") != -1) {
				Map<String,Object> readValue = null;
				try {
					readValue = eom.readValue(respPayload, new TypeUtil.Specific<Map<String,Object>>(){}.type());
				} catch (Exception e) {
				}
				
				if (readValue != null) throw new JsonedException("Calling\n" + uri + "\nreturned\n" + httpCallTrace(conn, payload, respPayload), readValue, statusCode);
			}
			throw new IllegalStateException("Calling\n" + uri + "\nreturned\n" + httpCallTrace(conn, payload, respPayload));
		}
		
		try {
			return new JavanetResponse<Ret>(conn, respPayload, this.returnType);
		} catch (Throwable e) {
			throw new RuntimeException("Error parsing response from " + uri + "\n" + httpCallTrace(conn, payload, respPayload), e);
		}
	}

	public static String httpCallTrace(HttpURLConnection conn, byte[] reqPayload, byte[] respPayload) {
		StringBuilder trace = new StringBuilder();
		trace.append('\n');
		if (conn != null) {
			trace.append(conn.getRequestMethod());
			trace.append(' ');
			trace.append(conn.getURL().toString());
			// TODO find a way to get the headers back
			//httpHeadersTrace(conn.getRequestProperties(), trace);
			if (reqPayload != null) {
				trace.append(new String(reqPayload));
			}
			trace.append("\n\n");
			trace.append("----------------------------------------------------\n");
			try {
				if (conn.getResponseCode() == -1) {
					trace.append("Invalid response");
				} else {
					trace.append(conn.getResponseCode());
					trace.append(' ');
					trace.append(conn.getResponseMessage());
					trace.append('\n');
					if (respPayload != null) {
						trace.append(new String(respPayload));
					}
				}
			} catch (Throwable t) {
				trace.append("Error on response");
			}
		} else if (respPayload != null) {
			trace.append(new String(respPayload));
		}
		return trace.toString();
	}

	private static void httpHeadersTrace(Map<String,List<String>> headers, StringBuilder trace) {
		for (Entry<String, List<String>> entry : headers.entrySet()) {
			trace.append(entry.getKey());
			trace.append(":");
			trace.append(entry.getValue());
			trace.append('\n');
		}
		trace.append('\n');
	}
	
	private static void httpEntityTrace(HttpURLConnection conn, byte[] payload,
			StringBuilder trace) {
		trace.append('\n');
		Map<String, List<String>> headers = conn.getHeaderFields();
		if (payload != null) {
			trace.append(new String(payload));
		}
	}

}
