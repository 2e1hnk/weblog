package weblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import weblog.LocationRepository;
import weblog.model.Location;
import weblog.model.Logbook;

@Controller
@RequestMapping(path="/location")
public class LocationController {
	
	private final LocationRepository locationRepository;

    @Autowired
    public LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
    
    @GetMapping(path="/all")
	public @ResponseBody Iterable<Location> getLocations() {
		return locationRepository.findAll();
	}
    
    /*
     * TODO: add security here
     */
    @GetMapping(path="/from/{id}")
	public @ResponseBody Iterable<Location> getLocationFrom(@PathVariable Long id) {
		return locationRepository.findByIdGreaterThan(id);
	}
    
    @GetMapping(path="/{logbook}/from/{id}")
	public @ResponseBody Iterable<Location> getLocationFrom(@PathVariable Logbook logbook, @PathVariable Long id) {
		return locationRepository.findByIdGreaterThanAndLogbookId(id, logbook.getId());
	}
    
    // Get list of contacts by callsign (used for ajax requests from log page)
    /*
	@GetMapping(path="/search/{callsign}")
	public @ResponseBody Collection<Contact> getContactsByCallsign(@PathVariable String callsign) {
		return locationRepository.findByCallsign(callsign);
	}
	*/

}