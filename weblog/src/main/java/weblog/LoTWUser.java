package weblog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LoTWUser {
	
	@Id
	private String callsign;
	private Date lastUpload;
	
	public String getCallsign() {
		return callsign;
	}
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	public Date getLastUpload() {
		return lastUpload;
	}
	public void setLastUpload(Date lastUpload) {
		this.lastUpload = lastUpload;
	}
}
