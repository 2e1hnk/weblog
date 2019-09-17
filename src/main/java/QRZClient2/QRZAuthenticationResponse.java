package QRZClient2;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="QRZDatabase")
public class QRZAuthenticationResponse {
	
	@XmlElement(name="Session")
	private QRZSession sessionElement;

	public QRZSession getSession() {
		return sessionElement;
	}

	public void setSession(QRZSession session) {
		this.sessionElement = session;
	}

	@Override
	public String toString() {
		return "QRZAuthenticationResponse [session=" + sessionElement + "]";
	}
}