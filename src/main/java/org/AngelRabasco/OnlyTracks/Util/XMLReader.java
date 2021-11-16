package org.AngelRabasco.OnlyTracks.Util;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.AngelRabasco.OnlyTracks.App;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

public class XMLReader {
	public static String getConectionInfo(String data) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		String url = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(App.class.getResourceAsStream("db.xml"));
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("/connection/" + data + "/text()");
			url = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return url;
	}
}
