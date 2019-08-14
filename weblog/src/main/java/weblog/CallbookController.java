package weblog;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

@RestController
@RequestMapping(path="/callbook")
public class CallbookController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private QRZClient2 qrzClient = new QRZClient2();
	
	@Autowired
	private CallbookEntryRepository callbookEntryRepository;
	
	public void addNewContact (CallbookEntry callbookEntry) {
		callbookEntryRepository.save(callbookEntry);
		logger.info("Callbook entry saved");
	}
	
	@GetMapping(path="/")
	public @ResponseBody Iterable<CallbookEntry> getAllCallbookEntries() {
		// This returns a JSON or XML with the users
		Iterable<CallbookEntry> list = callbookEntryRepository.findAll();
		logger.info("" + ((Collection<?>) list).size() + " entries in callbook");
		return list;
	}
	
	@GetMapping(path="/{callsign}")
	public @ResponseBody Collection<CallbookEntry> getCallbookEntryByCallsign(@PathVariable String callsign) {
		if ( callbookEntryRepository.findByCallsign(callsign).isEmpty() ) {
			try {
				callbookEntryRepository.save(qrzClient.lookupCallsign(callsign));
			} catch ( QRZCallsignNotFoundException e ) {
				// Callsign not found, nothing to do
				logger.error("QRZ Lookup for " + callsign + " returned no results.");
			}
		}
		return callbookEntryRepository.findByCallsign(callsign);
	}
}