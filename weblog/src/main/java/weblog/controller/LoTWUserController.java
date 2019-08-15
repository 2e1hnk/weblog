package weblog.controller;

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

import weblog.LoTWUserRepository;
import weblog.model.LoTWUser;

@RestController
@RequestMapping(path="/lotw-user")
public class LoTWUserController {
	
	private final LoTWUserRepository lotwUserRepository;

    @Autowired
    public LoTWUserController(LoTWUserRepository lotwUserRepository) {
        this.lotwUserRepository = lotwUserRepository;
    }
    
    @GetMapping(produces = "application/json")
    public Iterable<LoTWUser> home() {
        return lotwUserRepository.findAll();
    }
    
    @PostMapping("/add")
    public void addLotwUser(@Valid LoTWUser lotwUser) {
        lotwUserRepository.save(lotwUser);
    }
        
    @PostMapping("/update/{callsign}")
    public void updateLotwUser(@PathVariable("callsign") String callsign) {
    	// Nothing to do
    }
         
    @GetMapping("/delete/{callsign}")
    public void delete(@PathVariable("callsign") String callsign) {
        Collection<LoTWUser> lotwUsers = lotwUserRepository.findByCallsign(callsign);
        Iterator<LoTWUser> iterator = lotwUsers.iterator();
        while (iterator.hasNext()) {
        	lotwUserRepository.delete(iterator.next());
        }
    }
 
    // additional CRUD methods
    
    // Get list of contacts by callsign (used for ajax requests from log page)
	@GetMapping(path="/{callsign}", produces = "application/json")
	public @ResponseBody Collection<LoTWUser> getContactsByCallsign(@PathVariable String callsign) {
		return lotwUserRepository.findByCallsign(callsign);
	}

}