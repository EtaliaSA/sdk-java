package net.etalia.cron;

import static net.etalia.cron.ScheduledImport.PROP_FILE_FORMAT;
import static net.etalia.cron.ScheduledImport.PROP_PASSWORD;
import static net.etalia.cron.ScheduledImport.PROP_USER;
import static net.etalia.cron.ScheduledImport.getProperty;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Publication;
import net.etalia.client.domain.User;
import net.etalia.client.http.Caller;
import net.etalia.client.http.httpclient.HttpClientCaller;
import net.etalia.client.http.httpclient.HttpClientHelper;
import net.etalia.client.services.ContentApi;
import net.etalia.client.codec.Digester;
import net.etalia.nitf.NITFArticleReader;

public class ScheduledJob implements Runnable {

	private final static Logger log = Logger.getLogger(ScheduledJob.class.getName());

	private String dir;
	private Caller<ContentApi> cApi;

	public ScheduledJob(String dir) {
		this.dir = dir;
		HttpClientCaller<ContentApi> contentApi = new HttpClientCaller<ContentApi>(ContentApi.class);
		contentApi.setBaseUrl("https://dev.etalia.net/content-api/");
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(getClass().getResourceAsStream("/keystore.jks"), "etalia".toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] {"TLSv1"},
					null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			HttpClient httpClient = new HttpClientHelper().createDefaultClient(10, 30000, sslsf);
			contentApi.setHttpClient(httpClient);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Cannot use SSL Certificate", e);
		}
		this.cApi = contentApi;
	}

	public void run() {
		try {
			User user = null;
			String authorization = null;
			String format = getProperty(PROP_FILE_FORMAT);
			File basedir = new File(dir);
			log.log(Level.FINE, "Looking for files in " + dir);
			for (File file : basedir.listFiles()) {
				log.log(Level.FINE, "Reading file " + file.getAbsolutePath());
				if (authorization == null) {
					log.log(Level.FINE, "Generate Authentication token...");
					String auth = "Etalia_" + getProperty(PROP_USER) + ":" + new Digester().md5(getProperty(PROP_PASSWORD)).toBase64UrlSafeNoPad();//Utils.md5(getProperty(PROP_PASSWORD));
					user = cApi.method(cApi.service().authUser(auth)).withFields("id", "extraData")
								.setHeader("Authorization", auth).execute().cast();
					authorization = user.getExtraData("auth");
					log.log(Level.FINE, "Authentication done!");
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
					log.log(Level.FINE, "AUTHORIZATION " + authorization);
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
		} catch(Exception e) {
			log.log(Level.SEVERE, "Global error", e);
		}
		log.log(Level.FINE, "Job Done!");
	}

}
