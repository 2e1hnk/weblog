package QRZClient2;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="QRZDatabase")
public class QRZLookupResponse {
	
	@XmlElement(name="Callsign")
	private QRZCallsign Callsign;
	
	@XmlElement(name="Session")
	private QRZSession Session;
	
	public QRZLookupResponse() {
		
	}
	
	public QRZCallsign getCallsign() {
		return Callsign;
	}
	public void setCallsign(QRZCallsign callsign) {
		Callsign = callsign;
	}
	public QRZSession getSession() {
		return Session;
	}
	public void setSession(QRZSession session) {
		Session = session;
	}
	@Override
	public String toString() {
		return "QRZLookupResponse [Callsign=" + Callsign + ", Session=" + Session + "]";
	}
	
}
