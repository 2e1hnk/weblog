package weblog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private Date timestamp;
     
    @NotBlank(message = "Callsign is mandatory")
    private String callsign;
    
    private String rsts;
    
    private String rstr;
    
    private String location;
    
    private String name;
    
    private double frequency;
    
    private String band;
    
    private String mode;

	private boolean qsl_req;
	private boolean qsl_sent;
	private boolean qsl_rcvd;
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public String getRsts() {
		return rsts;
	}

	public void setRsts(String rsts) {
		this.rsts = rsts;
	}

	public String getRstr() {
		return rstr;
	}

	public void setRstr(String rstr) {
		this.rstr = rstr;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean isQsl_req() {
		return qsl_req;
	}

	public void setQsl_req(boolean qsl_req) {
		this.qsl_req = qsl_req;
	}

	public boolean isQsl_sent() {
		return qsl_sent;
	}

	public void setQsl_sent(boolean qsl_sent) {
		this.qsl_sent = qsl_sent;
	}

	public boolean isQsl_rcvd() {
		return qsl_rcvd;
	}

	public void setQsl_rcvd(boolean qsl_rcvd) {
		this.qsl_rcvd = qsl_rcvd;
	}

}
