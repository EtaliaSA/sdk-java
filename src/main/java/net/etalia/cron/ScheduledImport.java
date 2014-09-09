package net.etalia.cron;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;

import net.etalia.client.codec.Digester;
import net.etalia.client.domain.User;
import net.etalia.client.http.Caller;
import net.etalia.client.http.httpclient.HttpClientCaller;
import net.etalia.client.http.httpclient.HttpClientHelper;
import net.etalia.client.services.ContentApi;

public class ScheduledImport {

	private final static Logger log = Logger.getLogger(ScheduledImport.class.getName());

	private static ScheduledExecutorService scheduler;

	public static final String PROP_FILES_DIR = "directory";
	public static final String PROP_FILE_FORMAT = "format";
	public static final String PROP_JOB_DELAY = "job.delay";
	public static final String PROP_PARALLEL_IMPORTS = "parallel-imports";
	public static final String PROP_PASSWORD = "password";
	public static final String PROP_PUBLICATION = "publication";
	public static final String PROP_STAMP = "stamp";
	public static final String PROP_TIMEZONE = "date.timezone";
	public static final String PROP_USER = "email";

	protected static final String CONTENT_API_URL = "https://api.etalia.net/content-api/";
	protected static final String IMAGE_API_URL = "http://image.etalia.net:81/";
	protected static final String CLOUDFRONT_URL = "http://d2feaz2wbhr9zq.cloudfront.net/";

	private static Properties properties;
	private static String propertyFile;
	private User user;
	private HttpClientCaller<ContentApi> cApi;

	public static String getProperty(String name) {
		return getProperty(name, null);
	}

	public static String getProperty(String name, String defaultValue) {
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
		if (args.length != 0) {
			propertyFile = args[0];
		} else {
			propertyFile = System.getProperty("user.dir") + File.separator + ".etalia-import.properties";
		}
		log.info("Using property file: " + propertyFile);
		ScheduledImport importer = new ScheduledImport();
		importer.init();
		importer.run();
	}

	public ScheduledImport() {
		try {
			LogManager.getLogManager().readConfiguration();
		} catch (Exception e) {
			System.err.println("Cannot instantiate LogManager: " + e.getMessage());
		}
	}

	public void init() {
		cApi = new HttpClientCaller<ContentApi>(ContentApi.class);
		cApi.setBaseUrl(CONTENT_API_URL);
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(ScheduledImport.class.getResourceAsStream("/keystore.jks"), "etalia".toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] {"TLSv1"},
					null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			HttpClient httpClient = new HttpClientHelper().createDefaultClient(10, 30000, sslsf);
			cApi.setHttpClient(httpClient);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Cannot use SSL Certificate", e);
		}
		log.fine("Generate Authentication token...");
		String auth = "Etalia_" + getProperty(PROP_USER) + ":" + new Digester().md5(getProperty(PROP_PASSWORD)).toBase64UrlSafeNoPad();
		user = cApi.method(cApi.service().authUser(auth)).withFields("id", "extraData")
						.setHeader("Authorization", auth).execute().cast();
		log.log(Level.FINE, "Authentication done!");
	}

	public void run() {
		if (getProperty(PROP_PARALLEL_IMPORTS) != null) {
			int imports = Integer.parseInt(getProperty(PROP_PARALLEL_IMPORTS));
			scheduler = Executors.newScheduledThreadPool(imports);
			for (int i=1; i<=imports; i++) {
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
}
