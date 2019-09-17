package weblog.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EqslUser {
	
	@Id
	private String callsign;
	
	public String getCallsign() {
		return callsign;
	}
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
}
