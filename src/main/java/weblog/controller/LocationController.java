package weblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import weblog.LocationRepository;
import weblog.model.Location;

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
    
    // Get list of contacts by callsign (used for ajax requests from log page)
    /*
	@GetMapping(path="/search/{callsign}")
	public @ResponseBody Collection<Contact> getContactsByCallsign(@PathVariable String callsign) {
		return locationRepository.findByCallsign(callsign);
	}
	*/

}