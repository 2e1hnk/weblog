package weblog.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import QRZClient2.QRZCallsignNotFoundException;
import QRZClient2.QRZClient2;
import QRZClient2.QRZLookupResponse;
import weblog.CallbookEntryRepository;
import weblog.model.CallbookEntry;

@Service
public class CallbookService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CallbookEntryRepository callbookEntryRepository;
	
	private QRZClient2 qrzClient = new QRZClient2();
	
	public void addNewContact (CallbookEntry callbookEntry) {
		callbookEntryRepository.save(callbookEntry);
		logger.info("Callbook entry saved");
	}
	
	public Iterable<CallbookEntry> getAllCallbookEntries() {
		Iterable<CallbookEntry> list = callbookEntryRepository.findAll();
		logger.info("" + ((Collection<?>) list).size() + " entries in callbook");
		return list;
	}
	
	public Collection<CallbookEntry> getCallbookEntryByCallsign(String callsign) {
		if ( callbookEntryRepository.findByCallsign(callsign).size() < 1 ) {
			try {
				QRZLookupResponse qrzLookupResponse = qrzClient.lookupCallsign(callsign);
				CallbookEntry callbookEntry = new CallbookEntry(qrzLookupResponse);
				callbookEntryRepository.save(callbookEntry);
			} catch ( QRZCallsignNotFoundException e ) {
				// Callsign not found, nothing to do
				logger.error("QRZ Lookup for " + callsign + " returned no results.");
			}
		}
		return callbookEntryRepository.findByCallsign(callsign);
	}
	
}
