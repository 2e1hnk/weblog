package weblog.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Indexed
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
     
    @NotBlank(message = "Callsign is mandatory")
    @Field
    private String callsign;
    
    private String rsts;
    
    private String rstr;
    
    @Field
    private String location;
    
    @Field
    private String name;
    
    @Field
    private String opName;
    
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
	
	private String getAdifDate() {
		DateFormat adifDateFormatter = new SimpleDateFormat("yyyyMMdd");
		return adifDateFormatter.format(this.getTimestamp());
	}

	private String getAdifTime() {
		DateFormat adifTimeFormatter = new SimpleDateFormat("HHmm");
		return adifTimeFormatter.format(this.getTimestamp());
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

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("<QSO_DATE:%d>%s", this.getAdifDate().length(), this.getAdifDate()));
		sb.append(String.format("<TIME_ON:%d>%s", this.getAdifTime().length(), this.getAdifTime()));
		sb.append(String.format("<CALL:%d>%s", this.getCallsign().length(), this.getCallsign()));
		if ( this.getBand() != null ) {
			sb.append(String.format("<BAND:%d>%s", this.getBand().length(), this.getBand()));
		}
		if ( this.getMode() != null ) {
			sb.append(String.format("<MODE:%d>%s", this.getMode().length(), this.getMode()));
		}
		sb.append(String.format("<RST_SENT:%d>%s", this.getRsts().length(), this.getRsts()));
		sb.append(String.format("<RST_RCVD:%d>%s", this.getRstr().length(), this.getRstr()));
		//sb.append(String.format("<GRIDSQUARE:%d>%s", this.getGrid().length(), this.getGrid()));
		sb.append("<EOR>\n");
		
		return sb.toString();
	}
}
