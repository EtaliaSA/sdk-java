package net.etalia.cron;

import static net.etalia.cron.ScheduledImport.CLOUDFRONT_URL;
import static net.etalia.cron.ScheduledImport.CONTENT_API_URL;
import static net.etalia.cron.ScheduledImport.IMAGE_API_URL;
import static net.etalia.cron.ScheduledImport.PROP_FILE_FORMAT;
import static net.etalia.cron.ScheduledImport.PROP_PASSWORD;
import static net.etalia.cron.ScheduledImport.PROP_USER;
import static net.etalia.cron.ScheduledImport.getProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Media;
import net.etalia.client.domain.Publication;
import net.etalia.client.domain.PublicationStandard;
import net.etalia.client.domain.User;
import net.etalia.client.http.Caller;
import net.etalia.client.http.httpclient.HttpClientCaller;
import net.etalia.client.http.httpclient.HttpClientHelper;
import net.etalia.client.http.javanet.MultipartUtility;
import net.etalia.client.json.EtaliaObjectMapper;
import net.etalia.client.services.ContentApi;
import net.etalia.client.codec.Digester;
import net.etalia.nitf.NITFArticleReader;

public class ScheduledJob implements Runnable {

	private final static Logger log = Logger.getLogger(ScheduledJob.class.getName());

	private String dir;
	private String publicationId;
	private String stampId;
	private Caller<ContentApi> cApi;

	public ScheduledJob(String dir, String publication, String stamp) {
		this.dir = dir;
		this.publicationId = publication;
		this.stampId = stamp;
		HttpClientCaller<ContentApi> contentApi = new HttpClientCaller<ContentApi>(ContentApi.class);
		contentApi.setBaseUrl(CONTENT_API_URL);
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
			log.info("Looking for files in " + dir);
			File basedir = new File(dir);
			for (File file : basedir.listFiles(new AcceptedFileFilter())) {
				log.fine("Reading file " + file.getAbsolutePath());
				if (authorization == null) {
					log.fine("Generate Authentication token...");
					String auth = "Etalia_" + getProperty(PROP_USER) + ":" + new Digester().md5(getProperty(PROP_PASSWORD)).toBase64UrlSafeNoPad();//Utils.md5(getProperty(PROP_PASSWORD));
					user = cApi.method(cApi.service().authUser(auth)).withFields("id", "extraData")
								.setHeader("Authorization", auth).execute().cast();
					authorization = user.getExtraData("auth");
					log.log(Level.FINE, "Authentication done!");
				}
				log.info("Reading file " + file.getAbsolutePath());
				try {
					Article article = null;
					if ("NITF".equals(format)) {
						article = new NITFArticleReader().read(new FileInputStream(file));
					}
					article.setPublished(true);
					article.setSearchable(true);
					article.setVm18(false);
					if (publicationId != null) {
						Publication publication = new PublicationStandard();
						publication.setId(publicationId);
						article.setPublication(publication);
					}
					// Upload images
					List<String> imagesToDelete = new ArrayList<String>();
					for (Media media : article.getGallery()) {
						File img = new File(dir + File.separator + media.getUrl());
						if (!img.exists()) {
							article.getGallery().remove(media);
							log.warning("Image " + img.getAbsolutePath() + " not found, skipping.");
							continue;
						}
						MultipartUtility multipart = new MultipartUtility(IMAGE_API_URL + "images", "UTF-8");
						multipart.addFilePart("content", img);
						EtaliaObjectMapper mapper = new EtaliaObjectMapper(true);
						Map<String, Object> response = mapper.readValue(multipart.finish().get(0));
						imagesToDelete.add(media.getUrl());
						media.setUrl(CLOUDFRONT_URL + "images/" + response.get("id"));
						media.setHeight(Long.parseLong(response.get("height").toString()));
						media.setWidth(Long.parseLong(response.get("width").toString()));
					}
					article = cApi.method(cApi.service().addArticle(article)).withFields("id")
									.setHeader("Authorization", authorization).execute().cast();
					log.fine("Article: " + article.getId());
					if (stampId != null) {
						Map<String, String> stamp = new HashMap<String, String>();
						stamp.put("id", stampId);
						stamp.put("article", article.getId());
						cApi.method(cApi.service().addStampArticle(stamp)).setPathVariable("userId", user.getId())
									.setHeader("Authorization", authorization).execute().cast();
					}
					if (file.delete()) {
						// Remove images
						for (String img : imagesToDelete) {
							boolean deleted = new File(dir + File.separator + img).delete();
							if (!deleted) {
								log.warning("Cannot delete image " + img + " from " + dir);
							}
						}
					} else {
						log.warning("Cannot delete file " + file.getAbsolutePath());
					}
				} catch (Exception e) {
					log.log(Level.SEVERE, "Error for file " + file.getAbsolutePath(), e);
					file.renameTo(new File(file.getAbsolutePath() + ".err"));
				}
				log.info("File complete: " + file.getAbsolutePath());
			}
		} catch(Exception e) {
			log.log(Level.SEVERE, "Global error", e);
		}
		log.info("Job Done!");
	}

	private class AcceptedFileFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			String format = getProperty(PROP_FILE_FORMAT);
			if ("NITF".equals(format) && name.endsWith(".xml")) {
				return true;
			}
			return false;
		}
	}

}
