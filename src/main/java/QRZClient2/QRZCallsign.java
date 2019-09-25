package QRZClient2;

import java.util.Date;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class QRZCallsign {
	private String call;
	private String aliases;
	private int dxcc;
	private String fname;
	private String name;
	private String addr1;
	private String addr2;
	private String zip;
	private String country;
	private double lat;
	private double lon;
	private String grid;
	private int ccode;
	private String land;
	
	@XmlElement(name="class") private String licenseclass;	// called 'class' in XML

	private String codes;
	
	private String email;
	private int u_views;
	private int bio;
	
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date biodate;
	
	private String image;
	private String imageinfo;
	
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date moddate;
	
	private boolean eqsl;
	private boolean mqsl;
	private String iota;
	private boolean lotw;
	private String user;
	private String geoloc;
	private String qslmgr;
	private int ituzone;
	private int gmtoffset;
	private boolean dst;
	private String timezone;
	private int areacode;
	private int born;
	private int cqzone;
	
	public QRZCallsign() {
		
	}
	
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
	public String getQslmgr() {
		return qslmgr;
	}
	public void setQslmgr(String qslmgr) {
		this.qslmgr = qslmgr;
	}
	public String getLicenseclass() {
		return licenseclass;
	}
	public void setLicenseclass(String licenseclass) {
		this.licenseclass = licenseclass;
	}
	public int getItuzone() {
		return ituzone;
	}
	public void setItuzone(int ituzone) {
		this.ituzone = ituzone;
	}
	public int getGmtoffset() {
		return gmtoffset;
	}
	public void setGmtoffset(int gmtoffset) {
		this.gmtoffset = gmtoffset;
	}
	public boolean isDst() {
		return dst;
	}
	public void setDst(boolean dst) {
		this.dst = dst;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public int getAreacode() {
		return areacode;
	}
	public void setAreacode(int areacode) {
		this.areacode = areacode;
	}
	public int getBorn() {
		return born;
	}
	public void setBorn(int born) {
		this.born = born;
	}
	public int getCqzone() {
		return cqzone;
	}
	public void setCqzone(int cqzone) {
		this.cqzone = cqzone;
	}
	@Override
	public String toString() {
		return "QRZCallsign [call=" + call + ", aliases=" + aliases + ", dxcc=" + dxcc + ", fname=" + fname + ", name="
				+ name + ", addr1=" + addr1 + ", addr2=" + addr2 + ", zip=" + zip + ", country=" + country + ", lat="
				+ lat + ", lon=" + lon + ", grid=" + grid + ", ccode=" + ccode + ", land=" + land + ", licenseclass="
				+ licenseclass + ", codes=" + codes + ", email=" + email + ", u_views=" + u_views + ", bio=" + bio
				+ ", biodate=" + biodate + ", image=" + image + ", imageinfo=" + imageinfo + ", moddate=" + moddate
				+ ", eqsl=" + eqsl + ", mqsl=" + mqsl + ", iota=" + iota + ", lotw=" + lotw + ", user=" + user
				+ ", geoloc=" + geoloc + "]";
	}

}
