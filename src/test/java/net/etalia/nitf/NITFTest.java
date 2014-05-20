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
		Assert.assertEquals("Etalia test title", art.getTitle());
		Assert.assertEquals("Etalia test subtitle", art.getSubtitle());
		Assert.assertEquals("it", art.getLang().getISOCode());
		Assert.assertEquals(1, art.getGallery().size());
		Assert.assertEquals(20, art.getBody().length());
	}

}
