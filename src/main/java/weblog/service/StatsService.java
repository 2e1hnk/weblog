package weblog.service;

import java.util.HashMap;
import java.util.Map;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    	Contact firstContact = contactRepository.findTopByOrderByIdAsc();
    	Contact latestContact = contactRepository.findTopByOrderByIdDesc();
    	
    	Map<String, String> stats = new HashMap<String, String>();
    	
    	stats.put("TotalContacts", Long.toString(this.getTotalContacts()));
    	stats.put("firstContactTimeAgo", p.format(firstContact.getTimestamp()));
    	stats.put("latestContactCallsign", latestContact.getCallsign());
    	stats.put("latestContactTime", latestContact.getTimestamp().toString());
    	stats.put("latestContactTimeAgo", p.format(latestContact.getTimestamp()));
    	
    	return stats;
    }
}
