package weblog;

public class BandStats {
	private String band;
	private long count;
	
	public BandStats(String band, long count) {
		this.setBand(band);
		this.setCount(count);
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
	
}
