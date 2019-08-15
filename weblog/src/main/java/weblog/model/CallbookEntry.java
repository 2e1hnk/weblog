package weblog.model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import QRZClient2.QRZLookupResponse;

@Entity // This tells Hibernate to make a table out of this class
public class CallbookEntry {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	
	private String callsign;			// call
	private String aliases;
	private int dxcc;
	private String fname;
	private String name;
	private String addr1;
	private String addr2;
	private String state;
	private String zip;
	private String country;
	private double lat;
	private double lon;
	private String grid;
	private String county;
	private int ccode;
	private String fips;
	private Date efdate;
	private Date expdate;
	private String license_class;		// class
	private String codes;
	private String qslmgr;
	private String email;
	private int u_views;
	private int bio;
	private Date biodate;
	private String image;
	private String imageinfo;
	private Date moddate;
	private String MSA;
	private int areacode;
	private String TimeZone;
	private int GMTOffset;
	private boolean DST;
	private boolean eqsl;
	private boolean mqsl;
	private int cqzone;
	private int ituzone;
	private int born;
	private boolean lotw;
	private String user;
	private String geoloc;
	
	public CallbookEntry() {
		
	}
	
	public CallbookEntry(QRZLookupResponse qrz) {
		this.setAddr1(qrz.getCallsign().getAddr1());
		this.setAddr2(qrz.getCallsign().getAddr2());
		this.setAliases(qrz.getCallsign().getAliases());
		this.setBio(qrz.getCallsign().getBio());
		this.setBiodate(qrz.getCallsign().getBiodate());
		this.setCallsign(qrz.getCallsign().getCall());
		this.setCcode(qrz.getCallsign().getCcode());
		this.setDxcc(qrz.getCallsign().getDxcc());
		this.setCountry(qrz.getCallsign().getCountry());
		this.setEmail(qrz.getCallsign().getEmail());
		this.setEqsl(qrz.getCallsign().isEqsl());
		this.setFname(qrz.getCallsign().getFname());
		this.setGeoloc(qrz.getCallsign().getGeoloc());
		this.setGrid(qrz.getCallsign().getGrid());
		this.setImage(qrz.getCallsign().getImage());
		this.setImageinfo(qrz.getCallsign().getImageinfo());
		this.setLat(qrz.getCallsign().getLat());
		this.setLon(qrz.getCallsign().getLon());
		this.setLotw(qrz.getCallsign().isLotw());
		this.setModdate(qrz.getCallsign().getModdate());
		this.setMqsl(qrz.getCallsign().isMqsl());
		this.setName(qrz.getCallsign().getName());
		this.setU_views(qrz.getCallsign().getU_views());
		this.setUser(qrz.getCallsign().getUser());
		this.setZip(qrz.getCallsign().getZip());
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCallsign() {
		return callsign;
	}
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	public String getAliases() {
		return aliases;
	}
	public void setAliases(String aliases) {
		this.aliases = aliases;
	}
	public int getDxcc() {
		return dxcc;
	}
	public void setDxcc(int dxcc) {
		this.dxcc = dxcc;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getGrid() {
		return grid;
	}
	public void setGrid(String grid) {
		this.grid = grid;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public int getCcode() {
		return ccode;
	}
	public void setCcode(int ccode) {
		this.ccode = ccode;
	}
	public String getFips() {
		return fips;
	}
	public void setFips(String fips) {
		this.fips = fips;
	}
	public Date getEfdate() {
		return efdate;
	}
	public void setEfdate(Date efdate) {
		this.efdate = efdate;
	}
	public Date getExpdate() {
		return expdate;
	}
	public void setExpdate(Date expdate) {
		this.expdate = expdate;
	}
	public String getLicense_class() {
		return license_class;
	}
	public void setLicense_class(String license_class) {
		this.license_class = license_class;
	}
	public String getCodes() {
		return codes;
	}
	public void setCodes(String codes) {
		this.codes = codes;
	}
	public String getQslmgr() {
		return qslmgr;
	}
	public void setQslmgr(String qslmgr) {
		this.qslmgr = qslmgr;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getU_views() {
		return u_views;
	}
	public void setU_views(int u_views) {
		this.u_views = u_views;
	}
	public int getBio() {
		return bio;
	}
	public void setBio(int bio) {
		this.bio = bio;
	}
	public Date getBiodate() {
		return biodate;
	}
	public void setBiodate(Date biodate) {
		this.biodate = biodate;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getImageinfo() {
		return imageinfo;
	}
	public void setImageinfo(String imageinfo) {
		this.imageinfo = imageinfo;
	}
	public Date getModdate() {
		return moddate;
	}
	public void setModdate(Date moddate) {
		this.moddate = moddate;
	}
	public String getMSA() {
		return MSA;
	}
	public void setMSA(String mSA) {
		MSA = mSA;
	}
	public int getAreacode() {
		return areacode;
	}
	public void setAreacode(int areacode) {
		this.areacode = areacode;
	}
	public String getTimeZone() {
		return TimeZone;
	}
	public void setTimeZone(String timeZone) {
		TimeZone = timeZone;
	}
	public int getGMTOffset() {
		return GMTOffset;
	}
	public void setGMTOffset(int gMTOffset) {
		GMTOffset = gMTOffset;
	}
	public boolean isDST() {
		return DST;
	}
	public void setDST(boolean dST) {
		DST = dST;
	}
	public boolean isEqsl() {
		return eqsl;
	}
	public void setEqsl(boolean eqsl) {
		this.eqsl = eqsl;
	}
	public boolean isMqsl() {
		return mqsl;
	}
	public void setMqsl(boolean mqsl) {
		this.mqsl = mqsl;
	}
	public int getCqzone() {
		return cqzone;
	}
	public void setCqzone(int cqzone) {
		this.cqzone = cqzone;
	}
	public int getItuzone() {
		return ituzone;
	}
	public void setItuzone(int ituzone) {
		this.ituzone = ituzone;
	}
	public int getBorn() {
		return born;
	}
	public void setBorn(int born) {
		this.born = born;
	}
	public boolean isLotw() {
		return lotw;
	}
	public void setLotw(boolean lotw) {
		this.lotw = lotw;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getGeoloc() {
		return geoloc;
	}
	public void setGeoloc(String geoloc) {
		this.geoloc = geoloc;
	}
	
	
}