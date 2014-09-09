package net.etalia.nitf;

import static net.etalia.cron.ScheduledImport.PROP_TIMEZONE;
import static net.etalia.cron.ScheduledImport.getProperty;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import net.etalia.client.domain.Article;
import net.etalia.client.domain.Media;
import net.etalia.client.domain.Media.MediaType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NITFArticleReader {

	public Article read(InputStream is) throws IOException {
		try {
			return parse(is);
		} catch (Exception e) {
			throw new IOException("Cannot parse this stream", e);
		}
	}

	private Article parse(InputStream is) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(is);
		Article article = new Article();
		article.setTitle(getValue(doc, "/nitf/body/body.head/hedline/hl1"));
		article.setSubtitle(getValue(doc, "/nitf/body/body.head/byline"));
		article.setLang(getValue(doc, "/nitf/body/body.content/lang/@lang"));
		NodeList nl = getNodeList(doc, "/nitf/body/body.content/block/img");
		for (int i=0; i<nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			Media media = new Media();
			media.setType(MediaType._image);
			media.setUrl(e.getAttribute("source"));
			article.addMedia(media);
		}
		article.setBody(getValue(doc, "/nitf/body/body.content/block/p"));
		String dateString = getValue(doc, "/nitf/head/docdata/date.release/@norm");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone(getProperty(PROP_TIMEZONE)));
		Long date = sdf.parse(dateString).getTime();
		article.setUpdated(date);
		return article;
	}

	private String getValue(Document doc, String xpath) throws Exception {
		XPath xp = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xp.compile(xpath);
		String result = (String) expr.evaluate(doc, XPathConstants.STRING);
		return result.trim();
	}

	private Node getNode(Document doc, String xpath) throws Exception {
		XPath xp = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xp.compile(xpath);
		Node result = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return result;
	}

	private NodeList getNodeList(Document doc, String xpath) throws Exception {
		XPath xp = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xp.compile(xpath);
		NodeList result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		return result;
	}

}
