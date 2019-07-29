package weblog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClublogUser {
	
	@Id
	private String callsign;
	private Date firstQso;
	private Date lastQso;
	private String locator;
	private boolean oqrs;
	
	public String getCallsign() {
		return callsign;
	}
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	public Date getFirstQso() {
		return firstQso;
	}
	public void setFirstQso(Date firstQso) {
		this.firstQso = firstQso;
	}
	public Date getLastQso() {
		return lastQso;
	}
	public void setLastQso(Date lastQso) {
		this.lastQso = lastQso;
	}
	public String getLocator() {
		return locator;
	}
	public void setLocator(String locator) {
		this.locator = locator;
	}
	public boolean isOqrs() {
		return oqrs;
	}
	public void setOqrs(boolean oqrs) {
		this.oqrs = oqrs;
	}
}
