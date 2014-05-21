package net.etalia.cron;

import static net.etalia.cron.ScheduledImport.PROP_FILE_FORMAT;
import static net.etalia.cron.ScheduledImport.PROP_PASSWORD;
import static net.etalia.cron.ScheduledImport.PROP_USER;
import static net.etalia.cron.ScheduledImport.getProperty;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Publication;
import net.etalia.client.domain.User;
import net.etalia.client.http.Caller;
import net.etalia.client.http.HttpClientCaller;
import net.etalia.client.http.HttpClientHelper;
import net.etalia.client.services.ContentApi;
import net.etalia.client.utils.Utils;
import net.etalia.nitf.NITFArticleReader;

public class ScheduledJob implements Runnable {

	private final static Logger log = Logger.getLogger(ScheduledJob.class.getName());

	private String dir;
	private Caller<ContentApi> cApi;

	public ScheduledJob(String dir) {
		this.dir = dir;
		HttpClientCaller<ContentApi> contentApi = new HttpClientCaller<ContentApi>(ContentApi.class);
		contentApi.setBaseUrl("https://dev.etalia.net/content-api/");
		contentApi.setHttpClient(new HttpClientHelper().createDefaultClient(1, 30000));
		this.cApi = contentApi;
	}

	public void run() {
		User user = null;
		String authorization = null;
		String format = getProperty(PROP_FILE_FORMAT);
		File basedir = new File(dir);
		for (File file : basedir.listFiles()) {
			if (authorization == null) {
				String auth = "Etalia_" + getProperty(PROP_USER) + ":" + Utils.md5(getProperty(PROP_PASSWORD));
				user = cApi.method(cApi.service().authUser(auth)).withFields("id", "extraData")
							.setHeader("Authorization", auth).execute().cast();
				authorization = user.getExtraData("auth");
			}
			log.log(Level.FINE, "Reading file " + file.getAbsolutePath());
			try {
				Article article = null;
				if ("NITF".equals(format)) {
					article = new NITFArticleReader().read(new FileInputStream(file));
				}
				String publicationId = ScheduledImport.getProperty(ScheduledImport.PROP_PUBLICATION);
				if (publicationId != null) {
					Publication publication = new Publication();
					publication.setId(publicationId);
					article.setPublication(publication);
				}
				article = cApi.method(cApi.service().addArticle(article)).withFields("id")
						.setHeader("Authorization", authorization).execute().cast();
				String stampId = ScheduledImport.getProperty(ScheduledImport.PROP_STAMP);
				if (stampId != null) {
					Map<String, String> stamp = new HashMap<String, String>();
					stamp.put("id", stampId);
					stamp.put("article", article.getId());
					cApi.method(cApi.service().addStampArticle(stamp)).setPathVariable("userId", user.getId())
					.setHeader("Authorization", authorization).execute().cast();
				}
				if (!file.delete()) {
					log.log(Level.WARNING, "Cannot delete file " + file.getAbsolutePath());
				}
			} catch (Exception e) {
				log.log(Level.SEVERE, "Error for file " + file.getAbsolutePath(), e);
			}
		}
	}

}
