package net.etalia.client.http.javanet;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.etalia.client.http.Auther;
import net.etalia.client.utils.URIBuilder;
import net.etalia.client.utils.URIBuilder.NameValuePair;
import net.etalia.client.utils.Utils;

public class JavanetAuther extends Auther {

	private final static Logger log = Logger.getLogger(JavanetAuther.class.getName());

	private JavanetCaller<?> caller;

	public JavanetAuther(JavanetCaller<?> caller) {
		super(caller);
		this.caller = caller;
	}
	
	@Override
	public Map<String, Boolean> check() {
		String uri = Utils.pathConcat(this.caller.getBaseUrl(), "/authcheck");
		if (System.getProperty("embedWebPort") != null)
			uri = uri.replace("${embedWebPort}", System.getProperty("embedWebPort"));
		
		HttpURLConnection conn = null;
		byte[] reqPayload = URIBuilder.format(Arrays.asList(new NameValuePair("check", getCheckPayload())), URIBuilder.UTF_8).getBytes();
		byte[] payload = null;
		
		try {
			conn = (HttpURLConnection) new URL(uri).openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
	
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			if (token != null) conn.setRequestProperty("Authorization", "Etalia " + token);

			OutputStream os = conn.getOutputStream();
			os.write(reqPayload);
			os.flush();
			os.close();
			
			int respcode = conn.getResponseCode();
			InputStream is = conn.getErrorStream();
			if (is == null) {
				is = conn.getInputStream();
			}
			if (is != null) {
				payload = Utils.toByteArray(is);
				Utils.closeQuietly(is);
				if (payload != null && payload.length == 0) payload = null;
			}
			if (respcode != 200) {
				throw new IllegalStateException("Authcheck call failed with " + respcode + " " + JavanetCall.httpCallTrace(conn, reqPayload, payload));
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Error executing HTTP call" + JavanetCall.httpCallTrace(conn, reqPayload, null), e);
		}

		if (log.isLoggable(Level.FINEST)) {
			log.finest(JavanetCall.httpCallTrace(conn, reqPayload, payload));
		}

		return super.parseResponse(new String(payload));
	}

}
