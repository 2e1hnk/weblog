package weblog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weblog.BandStats;
import weblog.ContactRepository;
import weblog.model.Contact;

@Service
public class StatsService {
    
	@Autowired
    private ContactRepository contactRepository;
    
    public long getTotalContacts() {
    	return contactRepository.count();
    }
    
    public Contact getLatestContact() {
    	return contactRepository.findTopByOrderByIdDesc();
    }
    
    public Map<String, String> getStatsTable() {
    	
    	PrettyTime p = new PrettyTime();
    	
    	Map<String, String> stats = new HashMap<String, String>();
    	
    	stats.put("TotalContacts", Long.toString(this.getTotalContacts()));
    	
    	if ( contactRepository.count() > 0) {
	    	Contact firstContact = contactRepository.findTopByOrderByIdAsc();
	    	Contact latestContact = contactRepository.findTopByOrderByIdDesc();
	    	
	    	stats.put("firstContactTimeAgo", p.format(firstContact.getTimestamp()));
	    	stats.put("latestContactCallsign", latestContact.getCallsign());
	    	stats.put("latestContactName", latestContact.getName());
	    	stats.put("latestContactTime", latestContact.getTimestamp().toString());
	    	stats.put("latestContactTimeAgo", p.format(latestContact.getTimestamp()));
    	}
    	
    	return stats;
    }
    
    public List<BandStats> getBandStatsList() {
    	return contactRepository.findBandStats();
    }
}
