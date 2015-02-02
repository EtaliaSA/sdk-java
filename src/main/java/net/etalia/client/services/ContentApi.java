package net.etalia.client.services;

import java.util.List;
import java.util.Map;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Journalia;
import net.etalia.client.domain.PageExampleSection;
import net.etalia.client.domain.PaginationList;
import net.etalia.client.domain.Publication;
import net.etalia.client.domain.PublicationSection;
import net.etalia.client.domain.StampArticle;
import net.etalia.client.domain.StampPublication;
import net.etalia.client.domain.User;
import net.etalia.client.domain.Page;
import net.etalia.client.domain.Media;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public interface ContentApi {

	// ========== USER ==========

	/**
	 * Enrolls a user
	 * @param user The user data
	 * @return "A JSON {id}"
	 */
	@RequestMapping(value="/user", method=RequestMethod.POST)
	public @ResponseBody @ResponseStatus(HttpStatus.CREATED) Map<String,String> addUser(@RequestBody User user);

	/**
	 * Logs a user in, based on HTTP Header
	 * <code>Authorization: Etalia_<email></code>
	 * 
	 * @return the User that has been logged in, in extradata.auth there is the authentication string to use
	 */
	@RequestMapping(value="/user", method=RequestMethod.GET)
	public @ResponseBody User authUser(@RequestHeader("Authorization") String authorization);

	/**
	 * Logs a user in, based on HTTP Header
	 * <code>FBAuthorization: FB_AUTH_TOKEN</code>
	 * 
	 * @return the User that has been logged in, in extradata.auth there is the authentication string to use
	 */
	@RequestMapping(value="/user/fb", method=RequestMethod.GET)
	public @ResponseBody User authFBUser(@RequestHeader("Authorization") String authorization);

	@RequestMapping(value="/token/forgotPassword", method=RequestMethod.POST)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void forgotPassword(@RequestBody Map<String, String> data);

	@RequestMapping(value="/user/{id}", method=RequestMethod.GET)
	public @ResponseBody User getUser(@PathVariable("id") String userId);

	@RequestMapping(value="/user/{id}", method=RequestMethod.PUT)
	public @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT) void updateUser(@RequestBody User user);

	@RequestMapping(value="/user/{userId}/photo", method=RequestMethod.PUT)
	public @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT) void updateUserPhoto(
																		@PathVariable(value="userId") String userId,
																		@RequestBody Media photo);

	@RequestMapping(value="/user/{userId}/cover", method=RequestMethod.PUT)
	public @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT) void updateUserCover(
																		@PathVariable(value="userId") String userId,
																		@RequestBody Media cover);

	@RequestMapping(value="/user/{userId}/changePassword", method=RequestMethod.PUT)
	public @ResponseBody User updatePassword(@RequestBody Map<String, String> data);

	@RequestMapping(value="/token/changeEmail", method=RequestMethod.PUT)
	public @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT) void changeEmail(@RequestBody Map<String, String> data);

	@RequestMapping(value="/user/{userId}/fb", method=RequestMethod.DELETE)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void removeFacebookUser(@PathVariable(value="userId") String userId);

	// ========== ARTICLE ==========

	/**
	 * Create a new content article, 
	 * @param article
	 * @return
	 */
	@RequestMapping(value="/content/article", method=RequestMethod.POST)
	public @ResponseBody @ResponseStatus(HttpStatus.CREATED) Article addArticle(@RequestBody Article article);

	@RequestMapping(value="/content/article/{id}", method=RequestMethod.GET)
	public @ResponseBody Article getArticle(@PathVariable(value="id") String articleId);

	@RequestMapping(value="/content/article/{articleId}", method=RequestMethod.PUT)
	public @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT) void updateArticle(@RequestBody Article article);

	@RequestMapping(value="/content/article/{articleId}", method=RequestMethod.DELETE)
	public @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT) void removeArticle(@PathVariable(value="articleId") String articleId);

	// ========== PUBLICATION ==========

	@RequestMapping(value="/journalia", method=RequestMethod.GET)
	public @ResponseBody Journalia journalia();

	@RequestMapping(value="/publication", method=RequestMethod.POST)
	public @ResponseBody @ResponseStatus(HttpStatus.CREATED) Publication addPublication(@RequestBody Publication publication);

	@RequestMapping(value="/publication/{id}", method=RequestMethod.GET)
	public @ResponseBody Publication getPublication(@PathVariable(value="id") String publicationId);

	@RequestMapping(value="/publication/{id}", method=RequestMethod.PUT)
	public @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT) void updatePublication(@RequestBody Publication publication);

	@RequestMapping(value="/publication/{id}", method=RequestMethod.DELETE)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void removePublication(@PathVariable(value="id") String publicationId);

	@RequestMapping(value="/publication/{publicationId}/page", method=RequestMethod.POST)
	public @ResponseBody @ResponseStatus(HttpStatus.CREATED) Page addPage(
															@PathVariable(value="publicationId") String publicationId,
															@RequestBody Page page);

	@RequestMapping(value="/publication/{publicationId}/page/{pageId}", method=RequestMethod.GET)
	public @ResponseBody Page getPage(@PathVariable(value="publicationId") String publicationId,
										@PathVariable(value="pageId") String pageId,
										@RequestParam(value="offset", required=false) Integer offset,
										@RequestParam(value="count", required=false) Integer count);

	@RequestMapping(value="/publication/{publicationId}/orderPages", method=RequestMethod.PUT)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void orderPages(@PathVariable(value="publicationId") String publicationId,
			@RequestBody Map<String,List<String>> pageIds);

	@RequestMapping(value="/publication/{publicationId}/page/{pageId}", method=RequestMethod.PUT)
	public @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT) void updatePage(
			@PathVariable(value="publicationId") String publicationId,
			@RequestBody Page page);

	@RequestMapping(value="/publication/{publicationId}/page/{pageId}", method=RequestMethod.DELETE)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void removePage(@PathVariable(value="publicationId") String publicationId,
			@PathVariable(value="pageId") String pageId);

	// ========== STAMP ==========

	@RequestMapping(value="/user/{userId}/stampArticles", method=RequestMethod.GET)
	public @ResponseBody PaginationList<StampArticle> getStampArticles(
															@PathVariable(value="userId") String userId,
															@RequestParam(value="offset", required=false) Integer offset,
															@RequestParam(value="count", required=false) Integer count);

	@RequestMapping(value="/user/{userId}/stampPublications", method=RequestMethod.GET)
	public @ResponseBody PaginationList<StampPublication> getStampPublications(
															@PathVariable(value="userId") String userId,
															@RequestParam(value="offset", required=false) Integer offset,
															@RequestParam(value="count", required=false) Integer count);

	@RequestMapping(value="/user/{userId}/stampArticles/article/{articleId}", method=RequestMethod.GET)
	public @ResponseBody PaginationList<StampArticle> getStampArticlesByArticle(
															@PathVariable(value="userId") String userId,
															@PathVariable(value="articleId") String articleId,
															@RequestParam(value="offset", required=false) Integer offset,
															@RequestParam(value="count", required=false) Integer count);

	@RequestMapping(value="/user/{userId}/stampPublications/publication/{publicationId}", method=RequestMethod.GET)
	public @ResponseBody PaginationList<StampPublication> getStampPublicationsByPublication(
															@PathVariable(value="userId") String userId,
															@PathVariable(value="publicationId") String publicationId,
															@RequestParam(value="offset", required=false) Integer offset,
															@RequestParam(value="count", required=false) Integer count);

	@RequestMapping(value="/user/{userId}/stampArticle", method=RequestMethod.POST)
	public @ResponseBody @ResponseStatus(HttpStatus.CREATED) StampArticle addStampArticle(@RequestBody Map<String, String> newStamp);

	@RequestMapping(value="/user/{userId}/stampPublication", method=RequestMethod.POST)
	public @ResponseBody @ResponseStatus(HttpStatus.CREATED) StampPublication addStampPublication(@RequestBody Map<String, String> newStamp);

	@RequestMapping(value="/user/{userId}/stampArticle/{stampId}", method=RequestMethod.PUT)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void updateStampArticle(@RequestBody StampArticle stamp);

	@RequestMapping(value="/user/{userId}/stampPublication/{stampId}", method=RequestMethod.PUT)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void updateStampPublication(@RequestBody StampPublication stamp);

	@RequestMapping(value="/user/{userId}/stampArticle/{stampId}", method=RequestMethod.DELETE)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void removeStampArticle(@PathVariable("stampId") String stampId);
	
	@RequestMapping(value="/user/{userId}/stampArticle/{stampId}/{articleId}", method=RequestMethod.DELETE)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void removeStampArticle(
																	@PathVariable(value="stampId") String stampId, 
																	@PathVariable(value="articleId") String articleId);

	@RequestMapping(value="/user/{userId}/stampPublication/{stampId}", method=RequestMethod.DELETE)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void removeStampPublication(@PathVariable(value="stampId") String stampId);

	@RequestMapping(value="/user/{userId}/stampPublication/{stampId}/{publicationId}", method=RequestMethod.DELETE)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void removeStampPublication(
																	@PathVariable(value="stampId") String stampId, 
																	@PathVariable(value="publicationId") String publicationId);

	// ========== PAGESECTION ==========

	@RequestMapping(value="/pagesections", method=RequestMethod.GET)
	public @ResponseBody PaginationList<PageExampleSection> getPageExampleSections(
			@RequestParam(value="lang") String lang,
			@RequestParam(value="offset", required=false) Integer offset,
			@RequestParam(value="count", required=false) Integer count);

	// ========== PUBLICATIONSECTION ==========

	@RequestMapping(value="/publicationsections", method=RequestMethod.GET)
	public @ResponseBody PaginationList<PublicationSection> getPublicationSections(
			@RequestParam(value="lang") String lang,
			@RequestParam(value="offset", required=false) Integer offset,
			@RequestParam(value="count", required=false) Integer count);

}
