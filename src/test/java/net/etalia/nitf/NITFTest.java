package net.etalia.nitf;

import org.junit.Assert;
import net.etalia.client.domain.Article;

import org.junit.Test;

public class NITFTest {

	@Test
	public void read() throws Exception {
		NITFArticleReader reader = new NITFArticleReader();
		Article art = reader.read(getClass().getResourceAsStream("article1.nitf"));
		Assert.assertNotNull(art);
		Assert.assertEquals("Liga: Real pareggia, rimonta compromessa", art.getTitle());
		Assert.assertEquals("Blancos a -4 dall'Atletico e a -1 dal Barca a 2 turni dalla fine", art.getSubtitle());
		Assert.assertEquals("it", art.getLang().getISOCode());
		Assert.assertEquals(1, art.getGallery().size());
		Assert.assertEquals(465, art.getBody().length());
	}

}
