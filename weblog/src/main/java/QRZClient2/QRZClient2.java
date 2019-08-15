package QRZClient2;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.message.internal.TracingLogger.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class QRZClient2 {
	
	static Logger logger = LoggerFactory.getLogger("QRZClient2");

	private String sessionKey;
	private Client apiClient;
	private String lookup_url = "https://xmldata.qrz.com/xml/current/?s=%s;callsign=%s";
	private String login_url = "https://xmldata.qrz.com/xml/current/?username=%s&password=%s&agent=2e1hnk_1.0";
	private String qrz_username = System.getenv("QRZ_USERNAME");
	private String qrz_password = System.getenv("QRZ_PASSWORD");

	public QRZClient2() {
		logger.info("Loading " + this.getClass().getName());
		this.apiClient = ClientBuilder.newClient(new ClientConfig().register( 
			new LoggingFeature(new JulFacade())
		));
		
	}

	public QRZLookupResponse lookupCallsign(String callsign) throws QRZCallsignNotFoundException {
		
		logger.info("Looking up " + callsign);
		
		logger.info("Session Key: " + this.sessionKey);
		
		//CallbookEntry qrzData = null;
		
		if (this.sessionKey == null) {
				// get a session key
				this.getSessionKey();
				logger.info("Session Key: " + this.sessionKey);
			}
	
		String lookupUrl = String.format(this.lookup_url, this.sessionKey, callsign);
		logger.info("Lookup URL: " + lookupUrl);
		
		WebTarget lookupWebTarget = apiClient.target(lookupUrl);
		
		Invocation.Builder invocationBuilder = lookupWebTarget.request(MediaType.APPLICATION_XML);
		
		QRZLookupResponse response = invocationBuilder.get(QRZLookupResponse.class);

		return response;
		
	}
/*
	private Map<String, String> qrzDocumentToMap(Document qrzDocument) throws XPathException {
		Map<String, String> map = new HashMap<String, String>();

		map.put("forename", this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:fname/text()"));
		map.put("name", this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:name/text()"));
		map.put("address", this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:addr2/text()"));
		map.put("state", this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:state/text()"));
		map.put("country", this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:country/text()"));

		return map;
	}
*/
	
	private int toInt(String in) {
		try {
			return Integer.parseInt(in);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private Date toDate(String in) {
		try {
			return new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse(in);
		} catch (ParseException e) {
			return null;
		}
	}

	private Map<String, String> qrzDocumentToFullMap(Document qrzDocument) {
		Map<String, String> map = new HashMap<String, String>();
		NodeList flowList = qrzDocument.getElementsByTagNameNS("http://xmldata.qrz.com", "Callsign");
		for (int i = 0; i < flowList.getLength(); i++) {
			NodeList childList = flowList.item(i).getChildNodes();
			for (int j = 0; j < childList.getLength(); j++) {
				Node childNode = childList.item(j);
				// Exclude and fields starting with #
				if ( !childNode.getNodeName().startsWith("#") ) {
					map.put(childNode.getNodeName(), childList.item(j).getTextContent().trim());
				}
			}
		}
		return this.readable(map);
	}

	private void getSessionKey() {
		String loginUrl = String.format(this.login_url, this.qrz_username, this.qrz_password);

		logger.info("QRZ.com login url: " + loginUrl);
		
		WebTarget authenticationWebTarget = apiClient.target(loginUrl);
		
		Invocation.Builder invocationBuilder = authenticationWebTarget.request(MediaType.APPLICATION_XML);
		
		QRZAuthenticationResponse response = invocationBuilder.get(QRZAuthenticationResponse.class);
		
		logger.info(response.toString());

		this.sessionKey = response.getSession().getKey();
	}

	private Document getXmlDocument(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(xml)));
		return document;
	}

	private String getXmlString(Document xml) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(xml);
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}
/*
	@SuppressWarnings("finally")
	private String getXmlValue(Document xmlDocument, String xPathString) {
		String retValue = "";
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			xPath.setNamespaceContext(new QRZNamespaceContext());
			XPathExpression expr = xPath.compile(xPathString);
			Node node = (Node) expr.evaluate(xmlDocument, XPathConstants.NODE);
			retValue = node.getNodeValue();
		} catch (XPathExpressionException e) {
			// throw new XPathExpressionException(e.toString());
		} catch (NullPointerException e) {
			// Node doesn't exist, don't do anything
		} finally {
			return retValue;
		}
	}
*/
	private static class QRZNamespaceContext implements NamespaceContext {

		public String getNamespaceURI(String prefix) {
			if ("ns".equals(prefix)) {
				return "http://xmldata.qrz.com";
			}
			return null;
		}

		public String getPrefix(String namespaceURI) {
			return null;
		}

		public Iterator getPrefixes(String namespaceURI) {
			return null;
		}

	}

	private Map<String, String> readable(Map<String, String> qrzData) {
		Map<String, String> conversions = new HashMap<String, String>();
		Map<String, String> results = new HashMap<String, String>();
		
		conversions.put("call", "Callsign");
		conversions.put("fname", "Forename");
		conversions.put("name", "Surname");
		conversions.put("country", "Country");
		conversions.put("addr2", "Address");

		for ( String key : qrzData.keySet() ) {
			if ( conversions.keySet().contains(key) ) {
				results.put(conversions.get(key), qrzData.get(key));
			} else {
				results.put(key, qrzData.get(key));
			}
		}
		
		return results;
		
	}
	private static class JulFacade extends java.util.logging.Logger {
		  JulFacade() { super("Jersey", null); }
		  @Override public void info(String msg) { logger.info(msg); }
		}
}

