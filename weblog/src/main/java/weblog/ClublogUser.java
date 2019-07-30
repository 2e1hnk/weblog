package weblog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class ClublogUser {
	
	@Id
	private String callsign;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") private Date firstqso;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") private Date lastqso;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") private Date lastupload;
	private String locator;
	private boolean oqrs;
	
	public String getCallsign() {
		return callsign;
	}
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	public Date getFirstqso() {
		return firstqso;
	}
	public void setFirstqso(Date firstqso) {
		this.firstqso = firstqso;
	}
	public Date getLastqso() {
		return lastqso;
	}
	public void setLastqso(Date lastqso) {
		this.lastqso = lastqso;
	}
	public Date getLastupload() {
		return lastupload;
	}
	public void setLastupload(Date lastupload) {
		this.lastupload = lastupload;
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
