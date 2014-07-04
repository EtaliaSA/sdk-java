package net.etalia.client.services;

import java.util.Map;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Journalia;
import net.etalia.client.domain.Publication;
import net.etalia.client.domain.StampArticle;
import net.etalia.client.domain.StampPublication;
import net.etalia.client.domain.User;
import net.etalia.client.domain.PaginationList;
import net.etalia.client.domain.PublicationSection;

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
	public @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT) void removeArticle(@PathVariable String articleId);

	// ========== PUBLICATION ==========

	@RequestMapping(value="/journalia", method=RequestMethod.GET)
	public @ResponseBody Journalia journalia();

	@RequestMapping(value="/publication", method=RequestMethod.POST)
	public @ResponseBody @ResponseStatus(HttpStatus.CREATED) Publication addPublication(@RequestBody Publication publication);

	@RequestMapping(value="/publication/{id}", method=RequestMethod.GET)
	public @ResponseBody Publication getPublication(@PathVariable("id") String publicationId);

	@RequestMapping(value="/publication/{id}", method=RequestMethod.PUT)
	public @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT) void updatePublication(@RequestBody Publication publication);

	@RequestMapping(value="/publication/{id}", method=RequestMethod.DELETE)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void removePublication(@PathVariable("id") String publicationId);

	// ========== STAMP ==========

	@RequestMapping(value="/user/{userId}/stampArticle", method=RequestMethod.POST)
	public @ResponseBody @ResponseStatus(HttpStatus.CREATED) StampArticle addStampArticle(@RequestBody Map<String, String> newStamp);

	@RequestMapping(value="/user/{userId}/stampPublication", method=RequestMethod.POST)
	public @ResponseBody @ResponseStatus(HttpStatus.CREATED) StampPublication addStampPublication(@RequestBody Map<String, String> newStamp);

	@RequestMapping(value="/user/{userId}/stampPublication/{stampId}", method=RequestMethod.DELETE)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void removeStampPublication(@PathVariable("stampId") String stampId);

	@RequestMapping(value="/user/{userId}/stampPublication/{stampId}/{publicationId}", method=RequestMethod.DELETE)
	public @ResponseStatus(HttpStatus.NO_CONTENT) void removeStampPublication(
																	@PathVariable("stampId") String stampId, 
																	@PathVariable("publicationId") String publicationId);

	// ========== PUBLICATIONSECTION ==========

	@RequestMapping(value="/publicationsections", method=RequestMethod.GET)
	public @ResponseBody PaginationList<PublicationSection> getPublicationSections(
			@RequestParam(value="lang") String lang,
			@RequestParam(value="offset", required=false) Integer offset,
			@RequestParam(value="count", required=false) Integer count);

}
