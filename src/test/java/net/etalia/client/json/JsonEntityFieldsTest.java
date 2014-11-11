package net.etalia.client.json;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import java.util.Arrays;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Media;
import net.etalia.client.domain.SearchCriteria;
import net.etalia.client.domain.User;

import org.junit.Test;

public class JsonEntityFieldsTest {

	@Test
	public void fieldsOnSetter() throws Exception {
		User u = new User();
		assertThat(u.getJsonUsedFields(), hasSize(0));
		u.setFirstname("Simone");
		assertThat(u.getJsonUsedFields(), hasSize(1));		
		assertThat(u.getJsonUsedFields(), hasItem(equalTo("firstname")));
		u.setLastname("Gianni");
		assertThat(u.getJsonUsedFields(), hasSize(2));
		assertThat(u.getJsonUsedFields(), hasItem(equalTo("firstname")));
		assertThat(u.getJsonUsedFields(), hasItem(equalTo("lastname")));
		assertThat(u.getBirthdate(), nullValue());
		assertThat(u.getJsonUsedFields(), hasSize(2));
		assertThat(u.getJsonUsedFields(), hasItem(equalTo("firstname")));
		assertThat(u.getJsonUsedFields(), hasItem(equalTo("lastname")));
	}
	
	@Test
	public void addedToCollection() throws Exception {
		User user = new User();
		{
			SearchCriteria sc = new SearchCriteria();
			assertThat(sc.getJsonUsedFields(), hasSize(0));
		}
		{
			SearchCriteria sc = new SearchCriteria();
			sc.getAuthorsFilter().add(user);
			assertThat(sc.getJsonUsedFields(), hasItem("authorsFilter"));
			assertThat(sc.getJsonUsedFields(), hasSize(1));
		}
		{
			Article a = new Article();
			a.getGallery().add(new Media());
			assertThat(a.getJsonUsedFields(), hasItem("gallery"));
			assertThat(a.getJsonUsedFields(), hasSize(1));
		}
		{
			Article a = new Article();
			a.getGallery().clear();
			assertThat(a.getJsonUsedFields(), hasItem("gallery"));
			assertThat(a.getJsonUsedFields(), hasSize(1));
		}
		{
			Article a = new Article();
			a.getGallery().addAll(Arrays.asList(new Media()));
			assertThat(a.getJsonUsedFields(), hasItem("gallery"));
			assertThat(a.getJsonUsedFields(), hasSize(1));
		}
		{
			Article a = new Article();
			a.getGallery().listIterator().add(new Media());
			assertThat(a.getJsonUsedFields(), hasItem("gallery"));
			assertThat(a.getJsonUsedFields(), hasSize(1));
		}
		{
			Article a = new Article();
			a.addMedia(new Media());
			assertThat(a.getJsonUsedFields(), hasItem("gallery"));
			assertThat(a.getJsonUsedFields(), hasSize(1));
		}
	}
	
	@Test
	public void fieldsOn() throws Exception {
		String json = "{'@entity':'User','firstname':'Simone','lastname':'Gianni'}";
		EtaliaObjectMapper eom = new EtaliaObjectMapper(true);
		User u = eom.readValue(json.replace('\'', '"'));
		assertThat(u.getJsonUsedFields(), hasSize(2));
		assertThat(u.getJsonUsedFields(), hasItem(equalTo("firstname")));
		assertThat(u.getJsonUsedFields(), hasItem(equalTo("lastname")));
	}
	
	@Test
	public void serializeOnlyUsed() throws Exception {
		Article a = new Article();
		a.setTitle("Title");
		a.setBody("Body");
		a.setOriginalUrl(null);
		assertThat(a.getJsonUsedFields(), hasItem(equalTo("title")));
		assertThat(a.getJsonUsedFields(), hasItem(equalTo("body")));
		assertThat(a.getJsonUsedFields(), hasItem(equalTo("originalUrl")));
		assertThat(a.getJsonUsedFields(), hasSize(3));

		EtaliaObjectMapper eom = new EtaliaObjectMapper(true);
		String str = eom.writeValueAsString(a);
		
		System.out.println(str);

		String[] split = str.split(",");
		for (String sp : split) {
			assertThat(sp, either(containsString("body\":")).or(containsString("title\":")).or(containsString("originalUrl\":")).or(containsString("@entity")));
		}
	}

}
