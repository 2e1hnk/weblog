package weblog;

public class RigctlMessage {
	private int rigId;
	private float frequency;
	private String mode;
	public int getRigId() {
		return rigId;
	}
	public void setRigId(int rigId) {
		this.rigId = rigId;
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
		return "RigctlMessage [rigId=" + rigId + ", frequency=" + frequency + ", mode=" + mode + "]";
	}
	
}
