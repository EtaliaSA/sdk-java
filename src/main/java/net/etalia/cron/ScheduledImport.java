package net.etalia.cron;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledImport {

	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static final String PROP_FILES_DIR = "directory";
	public static final String PROP_FILE_FORMAT = "format";
	public static final String PROP_USER = "email";
	public static final String PROP_PASSWORD = "password";
	public static final String PROP_PUBLICATION = "publication";
	public static final String PROP_STAMP = "stamp";
	// BASE URL

	private static Properties properties;
	private static String propertyFile;

	public static String getProperty(String name) {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(new FileInputStream(propertyFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return properties.getProperty(name);
	}

	public static void main(String[] args) {
		if (args.length != 0) {
			propertyFile = args[0];
		} else {
			propertyFile = System.getProperty("user.dir") + File.separator + ".etalia-import.properties";
		}
		String dir = getProperty(PROP_FILES_DIR) != null ? getProperty(PROP_FILES_DIR) : System.getProperty("user.dir");
		ScheduledJob job = new ScheduledJob(dir);
		scheduler.scheduleWithFixedDelay(job, 0, 60, TimeUnit.SECONDS);
	}

}
