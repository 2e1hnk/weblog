package weblog;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QRZLookupResponse {
	private String call;
	private String aliases;
	private int dxcc;
	private String fname;
	private String name;
	private String addr1;
	private String addr2;
	private String zip;
	private String country;
	private Long lat;
	private Long lon;
	private String grid;
	private int ccode;
	private String land;
	private String licenseclass;	// called 'class' in XML
	private String codes;
	private String email;
	private int u_views;
	private int bio;
	private Date biodate;
	private String image;
	private String imageinfo;
	private Date moddate;
	private boolean eqsl;
	private boolean mqsl;
	private String iota;
	private boolean lotw;
	private String user;
	private String geoloc;
	public String getCall() {
		return call;
	}
	public void setCall(String call) {
		this.call = call;
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
	public Long getLat() {
		return lat;
	}
	public void setLat(Long lat) {
		this.lat = lat;
	}
	public Long getLon() {
		return lon;
	}
	public void setLon(Long lon) {
		this.lon = lon;
	}
	public String getGrid() {
		return grid;
	}
	public void setGrid(String grid) {
		this.grid = grid;
	}
	public int getCcode() {
		return ccode;
	}
	public void setCcode(int ccode) {
		this.ccode = ccode;
	}
	public String getLand() {
		return land;
	}
	public void setLand(String land) {
		this.land = land;
	}
	public String getLicenseclass() {
		return licenseclass;
	}
	public void setLicenseclass(String licenseclass) {
		this.licenseclass = licenseclass;
	}
	public String getCodes() {
		return codes;
	}
	public void setCodes(String codes) {
		this.codes = codes;
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
	public String getIota() {
		return iota;
	}
	public void setIota(String iota) {
		this.iota = iota;
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
	public CallbookEntry getAsCallbookEntry() {
		CallbookEntry callbookEntry = new CallbookEntry();
		
		callbookEntry.setAddr1(this.getAddr1());
		callbookEntry.setAddr2(this.getAddr2());
		callbookEntry.setAliases(this.getAliases());
		callbookEntry.setBio(this.getBio());
		callbookEntry.setBiodate(this.getBiodate());
		callbookEntry.setCallsign(this.getCall());
		callbookEntry.setCcode(getCcode());
		callbookEntry.setCodes(this.getCodes());
		callbookEntry.setCountry(this.getCountry());
		callbookEntry.setDxcc(this.getDxcc());
		callbookEntry.setEmail(this.getEmail());
		callbookEntry.setEqsl(isEqsl());
		callbookEntry.setFname(this.getFname());
		callbookEntry.setGeoloc(this.getGeoloc());
		callbookEntry.setGrid(this.getGrid());
		callbookEntry.setImage(this.getImage());
		callbookEntry.setImageinfo(this.getImageinfo());
		callbookEntry.setLat(this.getLat());
		callbookEntry.setLon(this.getLon());
		callbookEntry.setLicense_class(this.getLicenseclass());
		callbookEntry.setLotw(this.isLotw());
		callbookEntry.setModdate(this.getModdate());
		callbookEntry.setMqsl(this.isMqsl());
		callbookEntry.setName(this.getName());
		callbookEntry.setU_views(this.getU_views());
		callbookEntry.setUser(this.getUser());
		callbookEntry.setZip(this.getZip());
		
		return callbookEntry;
	}
}
