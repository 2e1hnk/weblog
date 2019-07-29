package weblog;

import java.util.Collection;
import java.util.Iterator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/clublog-user")
public class ClublogUserController {
	
	private final ClublogUserRepository clublogUserRepository;

    @Autowired
    public ClublogUserController(ClublogUserRepository clublogUserRepository) {
        this.clublogUserRepository = clublogUserRepository;
    }
    
    @GetMapping(produces = "application/json")
    public Iterable<ClublogUser> home() {
        return clublogUserRepository.findAll();
    }
    
    @PostMapping("/add")
    public void addClublogUser(@Valid ClublogUser clublogUser) {
        clublogUserRepository.save(clublogUser);
    }
        
    @PostMapping("/update/{callsign}")
    public void updateClublogUser(@PathVariable("callsign") String callsign) {
    	// Nothing to do
    }
         
    @GetMapping("/delete/{callsign}")
    public void delete(@PathVariable("callsign") String callsign) {
        Collection<ClublogUser> clublogUsers = clublogUserRepository.findByCallsign(callsign);
        Iterator<ClublogUser> iterator = clublogUsers.iterator();
        while (iterator.hasNext()) {
        	clublogUserRepository.delete(iterator.next());
        }
    }
 
    // additional CRUD methods
    
    // Get list of contacts by callsign (used for ajax requests from log page)
	@GetMapping(path="/{callsign}", produces = "application/json")
	public @ResponseBody Collection<ClublogUser> getContactsByCallsign(@PathVariable String callsign) {
		return clublogUserRepository.findByCallsign(callsign);
	}

}