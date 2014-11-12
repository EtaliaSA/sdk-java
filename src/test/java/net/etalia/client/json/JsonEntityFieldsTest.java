package net.etalia.client.json;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import java.util.Arrays;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Media;
import net.etalia.client.domain.PublicationStandard;
import net.etalia.client.domain.SearchCriteria;
import net.etalia.client.domain.StampArticle;
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
	
	@Test
	public void extraData() throws Exception {
		Article a = new Article();
		a.setExtraData("prova", "extraroba");
		
		assertThat((String)a.getExtraData("prova"), equalTo("extraroba"));
		
		EtaliaObjectMapper eom = new EtaliaObjectMapper(true);
		String str = eom.writeValueAsString(a);
		
		System.out.println(str);

		String[] split = str.split(",");
		for (String sp : split) {
			assertThat(sp, 
					either(containsString("@entity\":"))
					.or(containsString("extraData\":"))
				);
		}
		assertThat(str, containsString("extraroba"));
	}
	
	@Test
	public void cascading() throws Exception {
		SearchCriteria sc = new SearchCriteria();
		sc.setProfileBoost(5l);
		
		User user = new User();
		user.setId("u1234");
		user.setTitle("Mantitolo");
		sc.getAuthorsFilter().add(user);
		
		PublicationStandard p = new PublicationStandard();
		p.setId("p1234");
		p.setTitle("Pubblico");
		sc.getPublicationsFilter().add(p);
		
		EtaliaObjectMapper eom = new EtaliaObjectMapper(true);
		String str = eom.writeValueAsString(sc);
		
		System.out.println(str);
		
		String[] split = str.split(",");
		for (String sp : split) {
			assertThat(sp, 
					either(containsString("@entity\":"))
					.or(containsString("authorsFilter\":"))
					.or(containsString("id\":\"u1234"))
					.or(containsString("title\":\"Mantitolo"))
					.or(containsString("profileBoost\":"))
					.or(containsString("publicationsFilter\":"))
					.or(containsString("id\":\"p1234"))
					.or(containsString("title\":\"Pubblico"))
				);
		}
	}
	
	@Test
	public void npeParsing() throws Exception {
		String json = "{\"@entity\":\"PublicationStandard\",\"id\":\"p10da88fd-bd06-4111-ba53-edc715c1887f\",\"header\":{\"@entity\":\"Media\",\"id\":\"maa8b0299-e968-4f7e-a4f7-c543e0fb57e4\",\"url\":\"http://d2feaz2wbhr9zq.cloudfront.net/images/21943b21-e182-48d8-baa3-a44452a49055.png\"},\"logo\":{\"@entity\":\"Media\",\"id\":\"me50aed50-b6bc-402b-9b0d-6d0cc7426582\",\"url\":\"http://d2feaz2wbhr9zq.cloudfront.net/images/bde92905-8589-40fa-9916-1e272c1b68ae.png\"},\"owner\":{\"@entity\":\"User\",\"id\":\"u6318e63d-66d6-4505-b1a5-cee795141b8d\",\"visibleTitle\":\"Piero Bozzolo\"},\"published\":true,\"title\":\"the Petecocoon Bulletin For Geeks\",\"showedPages\":[{\"@entity\":\"PageQuery\",\"id\":\"dc817658c-7be3-4fbf-a60e-c0948e8a0b04\",\"free\":true,\"title\":\"Spazio\"},{\"@entity\":\"PageQuery\",\"id\":\"d04a02c72-7234-4f22-84e1-7c12eee764aa\",\"free\":true,\"title\":\"Tecnologia\"},{\"@entity\":\"PageQuery\",\"id\":\"d724e9442-aefe-46d9-ac30-84631770ea97\",\"free\":true,\"title\":\"Matematica\"},{\"@entity\":\"PageQuery\",\"id\":\"d4191a4f9-0274-40c9-bfb0-7c9644d0b355\",\"free\":true,\"title\":\"Chimica\"},{\"@entity\":\"PageQuery\",\"id\":\"d876f71be-f898-4650-b1d9-7d2a824c598e\",\"free\":true,\"title\":\"Fisica\"},{\"@entity\":\"PageQuery\",\"id\":\"dd6ee38de-9fd9-477b-a19c-0f65d355e0bd\",\"free\":true,\"title\":\"Informatica\"},{\"@entity\":\"PageQuery\",\"id\":\"d84ccd02c-58c9-482c-8c31-723381405e0f\",\"free\":true,\"title\":\"Elettronica\"},{\"@entity\":\"PageQuery\",\"id\":\"d502e5db5-9e95-4037-8dea-c143e628a555\",\"free\":true,\"title\":\"Videogames\"},{\"@entity\":\"PageQuery\",\"id\":\"dccdd8442-6c69-47ba-b254-a822825f0dba\",\"free\":true,\"title\":\"Anime e cartoni animati\"},{\"@entity\":\"PageQuery\",\"id\":\"d993bec06-68f6-4540-9404-1f82ada59019\",\"free\":true,\"title\":\"Inside Magazine: Le Scienze\"},{\"@entity\":\"PageQuery\",\"id\":\"d2ac8e4b1-ff86-462b-befa-51ebf4c1f120\",\"free\":true,\"title\":\"Inside Magazine: National Geographic ITA\"},{\"@entity\":\"PageLink\",\"id\":\"L57a16aa5-3e34-416c-897f-665b3228d06b\",\"free\":true,\"title\":\"_PAGELINK_DEFAULT_TITLE\",\"visibleTitle\":\"Scienza\"},{\"@entity\":\"PageQuery\",\"id\":\"d3ddc9903-ae69-42fd-bab7-aabedbe91e14\",\"free\":true,\"title\":\"Archivio\"},{\"@entity\":\"PageLink\",\"id\":\"L89fa34ae-78c9-4b17-9f47-98b91dabc3b8\",\"free\":true,\"title\":\"_PAGELINK_DEFAULT_TITLE_01\",\"visibleTitle\":\"Internet\"},{\"@entity\":\"PageLink\",\"id\":\"L46cfde10-f5c5-425e-a59e-41a5f06eb7f3\",\"free\":true,\"title\":\"_PAGELINK_DEFAULT_TITLE_02\",\"visibleTitle\":\"Scienza\"}]}";
		EtaliaObjectMapper eom = new EtaliaObjectMapper(true);
		PublicationStandard pub = eom.readValue(json);
		assertThat(pub, notNullValue());
		
		assertThat(pub.getJsonUsedFields(), hasItem("logo"));
		assertThat(pub.getJsonUsedFields(), hasItem("title"));
		assertThat(pub.getJsonUsedFields(), hasItem("owner"));
		assertThat(pub.getJsonUsedFields(), hasItem("published"));
		assertThat(pub.getJsonUsedFields(), hasItem("showedPages"));
		assertThat(pub.getJsonUsedFields(), hasItem("header"));
	}

}
