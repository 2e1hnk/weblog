package weblog.service;

import org.springframework.stereotype.Service;

@Service
public class LocationService {
	
	public Double getBearingDegrees(String sourceLocator, String destinationLocator) {
		return getBearingDegrees(this.extractLatitudeFromLocator(sourceLocator), this.extractLongitudeFromLocator(sourceLocator), this.extractLatitudeFromLocator(destinationLocator), this.extractLongitudeFromLocator(destinationLocator));
	}
	
	public Double getBearingDegrees(Double srcLatitude, Double srcLongitude, Double dstLatitude, Double dstLongitude) {
		return (Math.toDegrees(getBearingRadians(srcLatitude, srcLongitude, dstLatitude, dstLongitude)) + 360 ) % 360;
	}
	
	public Double getBearingRadians(Double srcLatitude, Double srcLongitude, Double dstLatitude, Double dstLongitude) {
	    double srcLat = Math.toRadians(srcLatitude);
	    double dstLat = Math.toRadians(dstLatitude);
	    double dLng = Math.toRadians(dstLongitude - srcLongitude);

	    return Math.atan2(Math.sin(dLng) * Math.cos(dstLat),
	            Math.cos(srcLat) * Math.sin(dstLat) - 
	              Math.sin(srcLat) * Math.cos(dstLat) * Math.cos(dLng));
	}
	
	public String getLocator(double lat, double lng) {
		double longitude = lng + 180;
		longitude /= 2;
		char lonFirst = (char) ('A' + (longitude / 10));
		char lonSecond = (char) ('0' + longitude % 10);
		char lonThird = (char) ('A' + (longitude % 1) * 24);

		double latitude = lat + 90;
		char latFirst = (char) ('A' + (latitude / 10));
		char latSecond = (char) ('0' + latitude % 10);
		char latThird = (char) ('A' + (latitude % 1) * 24);

		StringBuilder sb = new StringBuilder();
		sb.append(lonFirst);
		sb.append(latFirst);
		sb.append(lonSecond);
		sb.append(latSecond);
		sb.append(("" + lonThird).toLowerCase());
		sb.append(("" + latThird).toLowerCase());

		return sb.toString();
	}

	public double extractLatitudeFromLocator(String locator) {
		String maidenhead = locator.toUpperCase();
		double latitude = -90 + 10 * (maidenhead.charAt(1) - 'A') + (maidenhead.charAt(3) - '0')
				+ 2.5 / 60 * (maidenhead.charAt(5) - 'A') + 2.5 / 60 / 2;
		return latitude;
	}

	public double extractLongitudeFromLocator(String locator) {
		String maidenhead = locator.toUpperCase();
		double longitude = -180 + 20 * (maidenhead.charAt(0) - 'A') + 2 * (maidenhead.charAt(2) - '0')
				+ 5.0 / 60 * (maidenhead.charAt(4) - 'A') + 5.0 / 60 / 2;
		return longitude;
	}

}
