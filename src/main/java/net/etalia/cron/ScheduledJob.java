package net.etalia.cron;

import static net.etalia.cron.ScheduledImport.CLOUDFRONT_URL;
import static net.etalia.cron.ScheduledImport.IMAGE_API_URL;
import static net.etalia.cron.ScheduledImport.PROP_FILE_FORMAT;
import static net.etalia.cron.ScheduledImport.PROP_TIMEZONE;
import static net.etalia.cron.ScheduledImport.getProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Media;
import net.etalia.client.domain.Publication;
import net.etalia.client.domain.PublicationStandard;
import net.etalia.client.http.javanet.MultipartUtility;
import net.etalia.client.json.EtaliaObjectMapper;
import net.etalia.nitf.NITFArticleReader;

public class ScheduledJob implements Runnable {

	private final static Logger log = Logger.getLogger(ScheduledJob.class.getName());

	private ScheduledImport imp;
	private String dir;
	private String publicationId;
	private String stampId;

	public ScheduledJob(ScheduledImport imp, String dir, String publication, String stamp) {
		this.imp = imp;
		this.dir = dir;
		this.publicationId = publication;
		this.stampId = stamp;
	}

	public void run() {
		try {
			String format = getProperty(PROP_FILE_FORMAT);
			log.info("Looking for files in " + dir);
			File basedir = new File(dir);
			for (File file : basedir.listFiles(new AcceptedFileFilter())) {
				log.info("Reading file " + file.getAbsolutePath());
				try {
					Article article = null;
					if ("NITF".equals(format)) {
						NITFArticleReader reader = new NITFArticleReader();
						reader.setTimeZone(TimeZone.getTimeZone(getProperty(PROP_TIMEZONE)));
						article = reader.read(new FileInputStream(file));
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
					article = imp.getCAPI().method(imp.getCAPI().service().addArticle(article)).withFields("id")
									.setHeader("Authorization", imp.getAuthorization()).execute().cast();
					log.fine("Article: " + article.getId());
					if (stampId != null) {
						Map<String, String> stamp = new HashMap<String, String>();
						stamp.put("id", stampId);
						stamp.put("article", article.getId());
						imp.getCAPI().method(imp.getCAPI().service().addStampArticle(stamp))
									.setPathVariable("userId", imp.getUser().getId())
									.setHeader("Authorization", imp.getAuthorization()).execute().cast();
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
					log.info("File complete: " + file.getAbsolutePath());
				} catch (Exception e) {
					log.log(Level.SEVERE, "Error for file " + file.getAbsolutePath(), e);
					file.renameTo(new File(file.getAbsolutePath() + ".err"));
				}
			}
		} catch(Exception e) {
			log.log(Level.SEVERE, "Global error", e);
		}
		log.info("Job Done for dir: " + dir);
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
