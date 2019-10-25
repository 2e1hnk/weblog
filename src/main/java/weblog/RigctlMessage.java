package weblog;

import weblog.model.Logbook;

public class RigctlMessage {
	private Long logbookId;
	private float frequency;
	private String mode;
	
	public Long getLogbookId() {
		return logbookId;
	}
	public void setLogbookId(Long logbookId) {
		this.logbookId = logbookId;
	}
	public float getFrequency() {
		return frequency;
	}
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public void setValueByAttributeName(String attribute, Object value) throws Exception {
		switch (attribute) {
		case "frequency":
			this.setFrequency(Float.parseFloat(value.toString()));
			break;
		case "mode":
			this.setMode(value.toString());
			break;
		default:
			throw new Exception("Invalid attribute: " + attribute);
		}
	}
	@Override
	public String toString() {
		return "RigctlMessage [logbookId=" + logbookId + ", frequency=" + frequency + ", mode=" + mode + "]";
	}
	
}
