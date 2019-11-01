package weblog.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import weblog.service.CallbookService;
import weblog.service.LocationService;

@Entity
@Indexed
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    @JoinColumn(name="logbook_id", nullable=false)
    @IndexedEmbedded
    @JsonIgnore
    private Logbook logbook;
    
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
	
	// Extended Fields (mostly from ADIF)
	private int antennaAz;		// Azimuth (0-359)
	private int antennaEl;		// Elevation (0-90)
	private String antennaPath;	// Propagation Path (L [long], S [short], O [other], G [greyline])
	private String email;
	private int power;
	private String rig;			// Rig name/description
	
	// Custom fields - these are for event-specific data
	// Note that a pipe ('|') at the beginning of a regex allows both a blank input
	// and something matching the regex
	
	// Jamboree on the Air JamPuz ID Code
	@Pattern(regexp="|^[1-7]{1}[A-Z]{2}[0-9]{2}[A-Z]{1}$", message="Must be in JID format (#AA##A) or empty")  
	private String jid_code;
	
	// SOTA Summit Code
	@Pattern(regexp="|^[A-Z0-9]{1,2}/[A-Z]{2}-[0-9]{3}$", message="Must be in SOTA summit format (AA/AA-000) or empty")  
	private String sota_code;
	
	// WAB Square
	@Pattern(regexp="|^[A-Z]{2}[0-9]{2}$", message="Must be in WAB format (AA00) or empty")
	private String wab_square;
	
	// NPOTA Code
	@Pattern(regexp="|^[A-Z]{2}[0-9]{2}$", message="Must be in NPOTA format (AA00) or empty")
	private String npota_code;
		
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Logbook getLogbook() {
		return logbook;
	}

	public void setLogbook(Logbook logbook) {
		this.logbook = logbook;
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

	private String getCabrilloDate() {
		DateFormat cabrilloDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		return cabrilloDateFormatter.format(this.getTimestamp());
	}

	private String getAdifTime() {
		DateFormat adifTimeFormatter = new SimpleDateFormat("HHmm");
		return adifTimeFormatter.format(this.getTimestamp());
	}

	private String getCabrilloTime() {
		return this.getAdifTime();
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

	public int getAntennaAz() {
		return antennaAz;
	}

	public void setAntennaAz(int antennaAz) {
		this.antennaAz = antennaAz;
	}

	public int getAntennaEl() {
		return antennaEl;
	}

	public void setAntennaEl(int antennaEl) {
		this.antennaEl = antennaEl;
	}

	public String getAntennaPath() {
		return antennaPath;
	}

	public void setAntennaPath(String antennaPath) {
		this.antennaPath = antennaPath;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public String getRig() {
		return rig;
	}

	public void setRig(String rig) {
		this.rig = rig;
	}

	public String getJid_code() {
		return jid_code;
	}

	public void setJid_code(String jid_code) {
		this.jid_code = jid_code;
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
		if ( this.getRsts() != null ) {
			sb.append(String.format("<RST_SENT:%d>%s", this.getRsts().length(), this.getRsts()));
		}
		if ( this.getRstr() != null ) {
			sb.append(String.format("<RST_RCVD:%d>%s", this.getRstr().length(), this.getRstr()));
		}
		//sb.append(String.format("<GRIDSQUARE:%d>%s", this.getGrid().length(), this.getGrid()));
		sb.append("<EOR>\n");
		
		return sb.toString();
	}
	
	public String toADIF() {
		return this.toString();
	}
	
	public String toCabrillo() {
		return String.format("QSO: %d %s %s %s %s %s 001 %s %s 001", this.getFrequency(), this.getCabrilloMode(), this.getCabrilloDate(), this.getCabrilloTime(), "MYCALL", this.getRsts(), this.getCallsign(), this.getRstr());
	}
	
	private String getCabrilloMode() {
		switch ( this.getMode() ) {
		case "SSB":
		case "USB":
		case "LSB":
		case "AM":
			return "PH";
		case "CW":
		case "CWR":
			return "CW";
		case "FM":
			return "FM";
		case "RTTY":
			return "RY";
		default:
			// Treat data as the default type because there are so many possibilities
			return "DG";
		}
	}
	
	/*
	 * Calculated fields
	 */

	
	/*
	 * Order of preference for bearing is:
	 * 	-	Antenna Azimuth (note that az=0 is taken to mean that no azimuth was recorded)
	 * 	-	Direction calculated from logbook locator to locator recorded in `location` field
	 * 	-	Direction calculated from logbook locator to lat/long recorded in callbook data
	 */
	@Transient
	public int getBearing() {
		LocationService locationService = new LocationService();

		if ( this.getAntennaAz() > 0 ) {
			return this.getAntennaAz();
		}

		double srcLat = this.getLogbook().getLat();
		double srcLng = this.getLogbook().getLng();
		
		if ( this.getLocation() != null ) {
			if ( this.getLocation().matches("[A-Z]{2}[0-9]{2}[a-zA-Z]{0,2}")) {
				double dstLat = locationService.extractLatitudeFromLocator(this.getLocation());
				double dstLng = locationService.extractLongitudeFromLocator(this.getLocation());
				
				return (int) Math.round(locationService.getBearingDegrees(srcLat, srcLng, dstLat, dstLng));
			}
		}
		/*
		else {
			CallbookService callbookService = new CallbookService();
			if ( callbookService.getCallbookEntryByCallsign(this.getCallsign()).size() == 1 ) {
				CallbookEntry entry = callbookService.getCallbookEntryByCallsign(this.getCallsign()).iterator().next();
				double dstLat = entry.getLat();
				double dstLng = entry.getLon();
				
				return (int) Math.round(locationService.getBearingDegrees(srcLat, srcLng, dstLat, dstLng));
			}
		}
		*/

		return 0;
	}
	
}
