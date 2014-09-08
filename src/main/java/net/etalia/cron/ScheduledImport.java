package net.etalia.cron;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ScheduledImport {

	private final static Logger log = Logger.getLogger(ScheduledImport.class.getName());

	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static final String PROP_FILES_DIR = "directory";
	public static final String PROP_FILE_FORMAT = "format";
	public static final String PROP_JOB_DELAY = "job.delay";
	public static final String PROP_PARALLEL_IMPORTS = "parallel-imports";
	public static final String PROP_PASSWORD = "password";
	public static final String PROP_PUBLICATION = "publication";
	public static final String PROP_STAMP = "stamp";
	public static final String PROP_TIMEZONE = "date.timezone";
	public static final String PROP_USER = "email";

	private static Properties properties;
	private static String propertyFile;
	protected static final String CONTENT_API_URL = "https://dev.etalia.net/content-api/";
	protected static final String IMAGE_API_URL = "http://dev.etalia.net:3002/";
	protected static final String CLOUDFRONT_URL = "http://dev.etalia.net:3002/";

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
		if (getProperty(PROP_PARALLEL_IMPORTS) != null) {
			int imports = Integer.parseInt(getProperty(PROP_PARALLEL_IMPORTS));
			for (int i=1; i<=imports; i++) {
				String dir = getProperty(PROP_FILES_DIR + "." + i);
				String publication = getProperty(PROP_PUBLICATION + "." + i);
				String stamp = getProperty(ScheduledImport.PROP_STAMP + "." + i);
				ScheduledJob job = new ScheduledJob(dir, publication, stamp);
				log.info("Scheduling new job for directory: " + dir + " - publication: " + publication + " - stamp: " + stamp);
				scheduler.scheduleWithFixedDelay(job, 0, Long.parseLong(getProperty(PROP_JOB_DELAY, "60")), TimeUnit.SECONDS);
			}
		} else {
			String dir = getProperty(PROP_FILES_DIR) != null ? getProperty(PROP_FILES_DIR) : System.getProperty("user.dir");
			String publication = getProperty(PROP_PUBLICATION);
			String stamp = getProperty(ScheduledImport.PROP_STAMP);
			ScheduledJob job = new ScheduledJob(dir, publication, stamp);
			log.info("Scheduling new job for directory: " + dir + " - publication: " + publication + " - stamp: " + stamp);
			scheduler.scheduleWithFixedDelay(job, 0, Long.parseLong(getProperty(PROP_JOB_DELAY, "60")), TimeUnit.SECONDS);
		}
	}

}
