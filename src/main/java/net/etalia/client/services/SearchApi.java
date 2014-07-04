package net.etalia.client.services;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.Map;

import net.etalia.client.domain.AdvertisedPaginationList;
import net.etalia.client.domain.Article;
import net.etalia.client.domain.PaginationList;
import net.etalia.client.domain.Publication;
import net.etalia.client.domain.SearchCriteria;
import net.etalia.client.domain.StampArticle;
import net.etalia.client.domain.Tag;
import net.etalia.client.domain.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public interface SearchApi {

	@RequestMapping(method=GET, value="/publication/last")
	public @ResponseBody PaginationList<Publication> getLastPublications(
			@RequestParam(value="offset", required=false) Integer start,
			@RequestParam(value="count", required=false) Integer count);

	@RequestMapping(method=POST, value="/publication/images")
	public @ResponseBody Map<String,String> getPublicationsImages(@RequestBody List<String> ids);

	@RequestMapping(value="/suggestions/publication")
	public @ResponseBody PaginationList<Publication> publicationSuggest(
			@RequestParam("q") String q,
			@RequestParam(value="ownerId", required=false) String ownerId,
			@RequestParam(value="offset", required=false) Integer offset,
			@RequestParam(value="count", required=false) Integer count);

	@RequestMapping(value="/search/article", method=POST)
	public @ResponseBody AdvertisedPaginationList<Article> searchArticles(
			@RequestBody SearchCriteria criteria,
			@RequestParam(value="offset", required=false) Integer offset,
			@RequestParam(value="count", required=false) Integer count,
			@RequestParam(value="advertising", required=false) Integer advertising,
			@RequestParam(value="advertisingWidth", required=false) Integer advertisingWidth,
			@RequestHeader(value="x-adu", required=false) String userTgt);

	@RequestMapping(value="/search/inpage/{pid}", method=GET)
	public @ResponseBody AdvertisedPaginationList<Article> searchArticlesByPage(
			@PathVariable("pid") String pageId,
			@RequestParam(value="offset", required=false) Integer offset,
			@RequestParam(value="count", required=false) Integer count,
			@RequestParam(value="advertising", required=false) Integer advertising,
			@RequestParam(value="advertisingWidth", required=false) Integer advertisingWidth,
			@RequestHeader(value="x-adu", required=false) String userTgt);

	@RequestMapping(value="/article/{aid}/similar")
	public @ResponseBody PaginationList<Article> searchSimilarArticles(
			@PathVariable(value="aid") String article,
			@RequestParam(value="count", required=false) Integer count);

	@RequestMapping(value="/suggestions/stamp")
	public @ResponseBody PaginationList<StampArticle> stampSuggest(
			@RequestParam("q") String q,
			@RequestParam(value="offset", required=false) Integer offset,
			@RequestParam(value="count", required=false) Integer count);

	@RequestMapping(value="/suggestions/tags")
	public @ResponseBody PaginationList<Tag> tagSuggest(
			@RequestParam("q") String q,
			@RequestParam(value="offset", required=false) Integer offset, 
			@RequestParam(value="count", required=false) Integer count);

	@RequestMapping(value="/suggestions/user")	
	public @ResponseBody PaginationList<User> userSuggest(
			@RequestParam("q") String q,
			@RequestParam(value="offset", required=false) Integer offset, 
			@RequestParam(value="count", required=false) Integer count);

}
