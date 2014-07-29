package net.etalia.client.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.etalia.jalia.ObjectMapper;

public abstract class Auther {

	private static ObjectMapper om = new ObjectMapper();
	
	private Map<String,Call<?>> calls = new HashMap<String,Call<?>>();
	private Map<String,String> inverse = new HashMap<String,String>();
	protected String token;

	protected Auther() {}
	
	public Auther addCall(String name, Call<?> call) {
		calls.put(name, call);
		return this;
	}
	
	public Auther allAsToken(String token) {
		this.token = token;
		return this;
	}
	
	protected String getCheckPayload() {
		List<String> checks = new ArrayList<String>(calls.size());
		for (Entry<String, Call<?>> entry : calls.entrySet()) {
			Call<?> call = entry.getValue();
			StringBuilder sb = new StringBuilder();
			sb.append(call.getMethod().name());
			sb.append(' ');
			sb.append(call.getPath());
			inverse.put(sb.toString(), entry.getKey());
			checks.add(sb.toString());
		}
		return om.writeValueAsString(checks);
	}
	
	public abstract Map<String,Boolean> check();

	protected Map<String, Boolean> parseResponse(String resp) {
		List<Map<String,Object>> pairs = om.readValue(resp, List.class);
		Map<String,Boolean> ret = new HashMap<String,Boolean>();
		for (Map<String,Object> rp : pairs) {
			String rurl = (String)rp.get("url");
			rurl = inverse.get(rurl);
			if (rurl == null) continue;
			Boolean ok = (Boolean) rp.get("ok");
			if (ok == null) continue;
			if (ok == true) {
				ret.put(rurl, true);
				continue;
			}
			String error = (String) rp.get("error");
			if (error == null) continue;
			if (error.equals("500")) continue;
			ret.put(rurl, false);
		}
		for (String k : calls.keySet()) {
			if (ret.containsKey(k)) continue;
			ret.put(k, null);
		}
		return ret;
	}
}
