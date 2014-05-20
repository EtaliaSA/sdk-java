package net.etalia.cron;

import java.io.File;
import java.io.FileInputStream;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Publication;
import net.etalia.nitf.NITFArticleReader;

public class ScheduledJob implements Runnable {

	private String dir;

	public ScheduledJob(String dir) {
		this.dir = dir;
	}

	public void run() {
		//TODO: get authentication token
		String format = ScheduledImport.getProperty(ScheduledImport.PROP_FILE_FORMAT);
		File basedir = new File(dir);
		for (File file : basedir.listFiles()) {
			System.out.println("Reading file " + file.getAbsolutePath());
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
				//TODO: save article
				String stampId = ScheduledImport.getProperty(ScheduledImport.PROP_STAMP);
				if (stampId != null) {
					//TODO: apply stamp
				}
				if (!file.delete()) {
					System.err.println("Cannot delete file " + file.getAbsolutePath());
				}
			} catch (Exception e) {
				System.err.println("Error for file " + file.getAbsolutePath());
				e.printStackTrace(System.err);
			}
		}
	}

}
