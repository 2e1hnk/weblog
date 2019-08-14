package weblog;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class QRZClient2 {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private String sessionKey;
	private Client apiClient;
	private String lookup_url = "https://xmldata.qrz.com/xml/current/?s=%s;callsign=%s";
	private String login_url = "https://xmldata.qrz.com/xml/current/?username=%s&password=%s&agent=2e1hnk_1.0";
	private String qrz_username = System.getenv("QRZ_USERNAME");
	private String qrz_password = System.getenv("QRZ_PASSWORD");

	public QRZClient2() {
		logger.info("QRZClient loading");
		this.apiClient = ClientBuilder.newClient();
	}

	protected CallbookEntry lookupCallsign(String callsign) throws QRZCallsignNotFoundException {
		
		logger.info("Looking up " + callsign);
		
		logger.info("Session Key: " + this.sessionKey);
		
		CallbookEntry qrzData = null;
		
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

		return response.getAsCallbookEntry();
		
	}

	private Map<String, String> qrzDocumentToMap(Document qrzDocument) throws XPathException {
		Map<String, String> map = new HashMap<String, String>();

		map.put("forename", this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:fname/text()"));
		map.put("name", this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:name/text()"));
		map.put("address", this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:addr2/text()"));
		map.put("state", this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:state/text()"));
		map.put("country", this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:country/text()"));

		return map;
	}

	private CallbookEntry qrzDocumentToCallbookEntry(Document qrzDocument) throws XPathException {
		CallbookEntry callbookEntry = new CallbookEntry();
		
		callbookEntry.setAddr1(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:addr1/text()"));
		callbookEntry.setAddr2(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:addr2/text()"));
		callbookEntry.setAliases(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:aliases/text()"));
		callbookEntry.setAreacode(toInt(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:areacode/text()")));
		callbookEntry.setBio(toInt(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:bio/text()")));
		callbookEntry.setBiodate(toDate(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:biodate/text()")));
		callbookEntry.setBorn(toInt(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:born/text()")));
		callbookEntry.setCallsign(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:call/text()"));
		callbookEntry.setCcode(toInt(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:ccode/text()")));
		callbookEntry.setCodes(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:codes/text()"));
		callbookEntry.setCountry(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:country/text()"));
		callbookEntry.setCounty(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:county/text()"));
		callbookEntry.setCqzone(toInt(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:cqzone/text()")));
		callbookEntry.setDST(Boolean.parseBoolean(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:DST/text()")));
		callbookEntry.setDxcc(toInt(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:dxcc/text()")));
		callbookEntry.setEfdate(toDate(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:efdate/text()")));
		callbookEntry.setEmail(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:email/text()"));
		callbookEntry.setEqsl(Boolean.parseBoolean(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:eqsl/text()")));
		callbookEntry.setExpdate(toDate(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:expdate/text()")));
		callbookEntry.setFips(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:fips/text()"));
		callbookEntry.setFname(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:fname/text()"));
		callbookEntry.setGeoloc(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:geoloc/text()"));
		callbookEntry.setGMTOffset(toInt(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:GMTOffset/text()")));
		callbookEntry.setGrid(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:grid/text()"));
		callbookEntry.setImage(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:image/text()"));
		callbookEntry.setImageinfo(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:imageinfo/text()"));
		callbookEntry.setItuzone(toInt(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:ituzone/text()")));
		callbookEntry.setLat(Double.parseDouble(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:lat/text()")));
		callbookEntry.setLicense_class(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:class/text()"));
		callbookEntry.setLon(Double.parseDouble(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:lon/text()")));
		callbookEntry.setLotw(Boolean.parseBoolean(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:lotw/text()")));
		callbookEntry.setModdate(toDate(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:moddate/text()")));
		callbookEntry.setMqsl(Boolean.parseBoolean(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:mqsl/text()")));
		callbookEntry.setMSA(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:MSA/text()"));
		callbookEntry.setName(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:name/text()"));
		callbookEntry.setQslmgr(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:qslmgr/text()"));
		callbookEntry.setState(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:state/text()"));
		callbookEntry.setTimeZone(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:timezone/text()"));
		callbookEntry.setU_views(toInt(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:u_views/text()")));
		callbookEntry.setUser(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:user/text()"));
		callbookEntry.setZip(this.getXmlValue(qrzDocument, "/ns:QRZDatabase/ns:Callsign/ns:zip/text()"));
		
		return callbookEntry;
	}
	
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

		this.sessionKey = response.getKey();
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
}
