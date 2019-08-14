package weblog;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QRZAuthenticationResponse {
    private String Key;
    private Integer Count;
    private Date SubExp;
    private Date GMTime;
    private String Remark;
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public Integer getCount() {
		return Count;
	}
	public void setCount(Integer count) {
		Count = count;
	}
	public Date getSubExp() {
		return SubExp;
	}
	public void setSubExp(Date subExp) {
		SubExp = subExp;
	}
	public Date getGMTime() {
		return GMTime;
	}
	public void setGMTime(Date gMTime) {
		GMTime = gMTime;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
}