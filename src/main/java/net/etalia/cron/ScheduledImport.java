package net.etalia.cron;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.etalia.client.codec.Digester;
import net.etalia.client.domain.User;
import net.etalia.client.http.Call;
import net.etalia.client.http.Caller;
import net.etalia.client.http.JsonedException;
import net.etalia.client.http.httpclient.HttpClientCaller;
import net.etalia.client.http.httpclient.HttpClientHelper;
import net.etalia.client.services.ContentApi;

import org.apache.http.client.HttpClient;

public class ScheduledImport {

	private final static Logger log = Logger.getLogger(ScheduledImport.class.getName());

	private static ScheduledExecutorService scheduler;

	public static final String PROP_FILES_DIR = "directory";
	public static final String PROP_FILE_FORMAT = "format";
	public static final String PROP_JOB_DELAY = "job.delay";
	public static final String PROP_PARALLEL_IMPORTS = "parallel-imports";
	public static final String PROP_DIRECTORIES = "directories";
	public static final String PROP_PASSWORD = "password";
	public static final String PROP_PUBLICATION = "publication";
	public static final String PROP_STAMP = "stamp";
	public static final String PROP_TIMEZONE = "date.timezone";
	public static final String PROP_USER = "email";

	protected static final String CONTENT_API_URL = "https://api.etalia.net/content-api/";
	protected static final String IMAGE_API_URL = "http://image.etalia.net:81/";
	protected static final String CLOUDFRONT_URL = "http://d2feaz2wbhr9zq.cloudfront.net/";

	private Properties properties;
	private String propertyFile;
	private User user;
	private HttpClientCaller<ContentApi> cApi;

	public String getProperty(String name) {
		return getProperty(name, null);
	}

	public String getProperty(String name, String defaultValue) {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(new FileInputStream(propertyFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (properties.containsKey(name)) {
			return properties.getProperty(name);
		} else {
			return defaultValue;
		}
		
	}

	public User getUser() {
		return user;
	}

	public String getAuthorization() {
		return user.getExtraData("auth");
	}

	public Caller<ContentApi> getCAPI() {
		return cApi;
	}

	public static void main(String[] args) {
		try {
			LogManager.getLogManager().readConfiguration();
		} catch (Exception e) {
			System.err.println("Cannot instantiate LogManager: " + e.getMessage());
		}
		String propertyFile;
		if (args.length != 0) {
			propertyFile = args[0];
		} else {
			propertyFile = System.getProperty("user.dir") + File.separator + ".etalia-import.properties";
		}
		ScheduledImport importer = new ScheduledImport(propertyFile);
		importer.init();
		importer.run();
	}

	public ScheduledImport(String propertyFile) {
		try {
			LogManager.getLogManager().readConfiguration();
		} catch (Exception e) {
			System.err.println("Cannot instantiate LogManager: " + e.getMessage());
		}
		log.info("Using property file: " + propertyFile);
		this.propertyFile = propertyFile;
	}

	public void init() {
		cApi = new HttpClientCaller<ContentApi>(ContentApi.class);
		cApi.setBaseUrl(CONTENT_API_URL);
		HttpClient httpClient = new HttpClientHelper().createDefaultClient(10, 30000);
		/*try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(ScheduledImport.class.getResourceAsStream("/keystore.jks.dev"), "etalia".toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] {"TLSv1"},
					null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			httpClient = new HttpClientHelper().createDefaultClient(10, 30000, sslsf);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Cannot use SSL Certificate", e);
		}*/
		cApi.setHttpClient(httpClient);
		this.authentication();
	}

	public void run() {
		if (getProperty(PROP_DIRECTORIES) != null) {
			int directories = Integer.parseInt(getProperty(PROP_DIRECTORIES));
			int imports = Integer.parseInt(getProperty(PROP_PARALLEL_IMPORTS));
			log.info("Scheduling " + imports + " parallel jobs");
			scheduler = Executors.newScheduledThreadPool(imports);
			log.info("Scanning " + directories + " directories");
			for (int i=1; i<=directories; i++) {
				String dir = getProperty(PROP_FILES_DIR + "." + i);
				String publication = getProperty(PROP_PUBLICATION + "." + i);
				String stamp = getProperty(ScheduledImport.PROP_STAMP + "." + i);
				ScheduledJob job = new ScheduledJob(this, dir, publication, stamp);
				log.info("Scheduling new job for directory: " + dir + " - publication: " + publication + " - stamp: " + stamp);
				scheduler.scheduleWithFixedDelay(job, 0, Long.parseLong(getProperty(PROP_JOB_DELAY, "60")), TimeUnit.SECONDS);
			}
		} else {
			scheduler = Executors.newScheduledThreadPool(1);
			String dir = getProperty(PROP_FILES_DIR) != null ? getProperty(PROP_FILES_DIR) : System.getProperty("user.dir");
			String publication = getProperty(PROP_PUBLICATION);
			String stamp = getProperty(ScheduledImport.PROP_STAMP);
			ScheduledJob job = new ScheduledJob(this, dir, publication, stamp);
			log.info("Scheduling new job for directory: " + dir + " - publication: " + publication + " - stamp: " + stamp);
			scheduler.scheduleWithFixedDelay(job, 0, Long.parseLong(getProperty(PROP_JOB_DELAY, "60")), TimeUnit.SECONDS);
		}
	}

	public <X> X invokeCAPI(X object, String fields) throws Exception {
		return invokeCAPI(object, fields, null);
	}

	public <X> X invokeCAPI(X object, Map<String, String> pathVariables) throws Exception {
		return invokeCAPI(object, null, pathVariables);
	}

	public <X> X invokeCAPI(X object, String fields, Map<String, String> pathVariables) throws Exception {
		Call<X> request = getCAPI().method(object);
		request.setHeader("Authorization", getAuthorization());
		if (fields != null) {
			request.withFields(fields.split(","));
		}
		if (pathVariables != null) {
			for (Map.Entry<String, String> entry : pathVariables.entrySet()) {
				request.setPathVariable(entry.getKey(), entry.getValue());
			}
		}
		X response = null;
		try {
			response = request.execute().cast();
		} catch (JsonedException e) {
			if (e.getStatusCode() == 401) {
				this.authentication();
				try {
					response = request.execute().cast();
				} catch (JsonedException ie) {
					log.severe("Cannot refresh authorization token!");
					throw new Exception("Cannot refresh authorization token!");
				}
			} else {
				log.severe("Cannot invoke server!");
				throw new Exception("Cannot invoke server!");
			}
		}
		return response;
	}

	private void authentication() {
		log.info("Generate Authentication token...");
		String auth = "Etalia_" + getProperty(PROP_USER) + ":" + new Digester().md5(getProperty(PROP_PASSWORD)).toBase64UrlSafeNoPad();
		user = cApi.method(cApi.service().authUser(auth)).withFields("id", "extraData")
						.setHeader("Authorization", auth).execute().cast();
		log.info("Authentication done!");
	}
}
