package net.etalia.client.http;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.etalia.jalia.OutField;
import net.etalia.jalia.spring.JaliaParametersFilter;
import net.etalia.client.json.EtaliaObjectMapper;
import net.etalia.client.utils.Utils;
import net.etalia.client.http.Caller.HttpMethod;


/**
 * 
 * AdminApi aa = Call.service(AdminApi.class);
 * Article art = Call.method(aa.method(param,param)).withFields("x","y","z").execute().cast();
 * 
 * Response<?> resp = Call.path("/user/" + id).addParameter("name","Mario").addHeader(...).execute();
 * resp.getHeader();
 * resp.getJsonBody();
 * 
 * @author Simone Gianni <simoneg@apache.org>
 *
 * @param <T>
 */
public abstract class Call<Ret> {

	private static Map<HttpMethod, BitSet> DEFAULT_ACCEPTS = new HashMap<HttpMethod, BitSet>();
	
	static {
		{
			BitSet bs = new BitSet(600);
			bs.set(200);
			DEFAULT_ACCEPTS.put(HttpMethod.GET, bs);
			DEFAULT_ACCEPTS.put(HttpMethod.POST, bs);
		}
		{
			BitSet bs = new BitSet(600);
			bs.set(201);
			DEFAULT_ACCEPTS.put(HttpMethod.POST, bs);
		}
		{
			BitSet bs = new BitSet(600);
			bs.set(204);
			DEFAULT_ACCEPTS.put(HttpMethod.PUT, bs);
			DEFAULT_ACCEPTS.put(HttpMethod.DELETE, bs);
		}
		{
			BitSet bs = new BitSet(600);
			bs.set(200,299);
			DEFAULT_ACCEPTS.put(null, bs);
		}
	}
	
	public static EtaliaObjectMapper eom = new EtaliaObjectMapper(true);
	
	protected Set<String> requestFields = new HashSet<String>();
	protected Type returnType;
	protected HttpMethod method;
	protected String path;
	protected Map<String, Object> requestParameters = new HashMap<String, Object>();
	protected Map<String, String> requestHeaders = new HashMap<String, String>();
	protected byte[] requestBody;
	
	protected BitSet accept = null;
	
	public Call(Type type, HttpMethod method, String path) {
		this.returnType = type;
		this.method = method;
		this.path = path;
	}

	public Call<Ret> withFields(String... fields) {
		if (fields == null) return this;
		requestFields.addAll(Arrays.asList(fields));
		return this;
	}
	
	public Call<Ret> accept(int... statusCodes) {
		if (accept == null) accept = new BitSet(600);
		for (int sc : statusCodes) accept.set(sc);
		return this;
	}
	
	public Call<Ret> acceptRange(int from, int to) {
		if (accept == null) accept = new BitSet(600);
		accept.set(from, to);
		return this;
	}
	
	public boolean isAcceptable(int statusCode) {
		BitSet bs = accept;
		if (bs == null) {
			bs = DEFAULT_ACCEPTS.get(this.method);
		}
		if (bs == null) {
			bs = DEFAULT_ACCEPTS.get(null);
		}
		if (bs == null) return true;
		return bs.get(statusCode);
	}
	
	public Response<Ret> execute() {
		return execute(true);
	}
	
	public abstract Response<Ret> execute(boolean check);

	public Call<Ret> withRequestedFields() {
		OutField fields = JaliaParametersFilter.getFields();
		if (fields == null)
			requestFields.addAll(fields.toStringList());
		return this;
	}

	protected String convertToString(Object obj) {
		String valstr = null;
		if (obj instanceof Class) {
			valstr = ((Class<?>)obj).getName();
		} else {
			// XXX SG maybe using .toString directly is not the best thing to do?
			valstr = obj.toString();
		}
		return valstr;
	}
	
	public Call<Ret> setPathVariable(String name, Object argument) {
		// XXX SG maybe using .toString directly is not the best thing to do?
		String npath = this.path.replaceAll("\\{" + name + "(\\:.*)?\\}", argument.toString());
		Utils.assertFalse("Cannot find parameter {" + name + "} in path " + this.path, npath.equals(this.path));
		this.path = npath;
		return this;
	}	
	
	public Call<Ret> setParameter(String name, Object value) {
		if (value == null) {
			this.requestParameters.remove(name);
		} else {
			this.requestParameters.put(name, value);
		}
		return this;
	}

	public Call<Ret> setHeader(String name, Object value) {
		if (value == null) {
			this.requestHeaders.remove(name);
		} else {
			this.requestHeaders.put(name, convertToString(value));
		}
		return this;
	}

	public Call<Ret> setBody(byte[] body) {
		this.requestBody = body;
		return this;
	}
	
	public Call<Ret> setBody(Object o) {
		try {
			this.setBody(eom.writeValueAsBytes(o, new OutField(true)));
		} catch (Exception e) {
			throw new RuntimeException("Error setting entity as request body", e);
		}
		return this;
	}
	
	public boolean hasParameters() {
		return requestParameters != null && requestParameters.size() > 0;
	}

	public boolean hasBody() {
		return requestBody != null && requestBody.length > 0;
	}
	
	public boolean hasFields() {
		return requestFields != null && requestFields.size() > 0;
	}
	
	public boolean hasHeaders() {
		return requestHeaders != null && requestHeaders.size() > 0;
	}
		
	protected String getFieldsAsString() {
		if (!hasFields()) return null;
		StringBuilder sb = new StringBuilder();
		for (String fld : requestFields) {
			sb.append(fld);
			sb.append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
