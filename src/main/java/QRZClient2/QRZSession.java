package QRZClient2;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class QRZSession {
	
	@XmlElement(name="Error")
	private String errorNode;
	
	@XmlElement(name="Key")
    private String keyNode;
	
	@XmlElement(name="Count")
    private Integer countNode;
	
	@XmlElement(name="SubExp")
    private String subExpNode;
	
	@XmlElement(name="GMTime")
    private String gMTimeNode;
	
	@XmlElement(name="Remark")
    private String remarkNode;
	
	public QRZSession() {
		
	}
    
	public String getError() {
		return errorNode;
	}
	public void setError(String error) {
		this.errorNode = error;
	}
	public String getKey() {
		return keyNode;
	}
	public void setKey(String key) {
		keyNode = key;
	}
	public Integer getCount() {
		return countNode;
	}
	public void setCount(Integer count) {
		countNode = count;
	}
	public String getSubExp() {
		return subExpNode;
	}
	public void setSubExp(String subExp) {
		subExpNode = subExp;
	}
	public String getGMTime() {
		return gMTimeNode;
	}
	public void setGMTime(String gMTime) {
		gMTimeNode = gMTime;
	}
	public String getRemark() {
		return remarkNode;
	}
	public void setRemark(String remark) {
		remarkNode = remark;
	}
	@Override
	public String toString() {
		return "QRZSession [Key=" + keyNode + ", Count=" + countNode + ", SubExp=" + subExpNode + ", GMTime=" + gMTimeNode + ", Remark="
				+ remarkNode + "]";
	}
}
