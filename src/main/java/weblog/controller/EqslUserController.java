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

import weblog.EqslUserRepository;
import weblog.model.EqslUser;

@RestController
@RequestMapping(path="/eqsl-user")
public class EqslUserController {
	
	private final EqslUserRepository eqslUserRepository;

    @Autowired
    public EqslUserController(EqslUserRepository eqslUserRepository) {
        this.eqslUserRepository = eqslUserRepository;
    }
    
    @GetMapping(produces = "application/json")
    public Iterable<EqslUser> home() {
        return eqslUserRepository.findAll();
    }
    
    @PostMapping("/add")
    public void addEqslUser(@Valid EqslUser eqslUser) {
        eqslUserRepository.save(eqslUser);
    }
        
    @PostMapping("/update/{callsign}")
    public void updateEqslUser(@PathVariable("callsign") String callsign) {
    	// Nothing to do
    }
         
    @GetMapping("/delete/{callsign}")
    public void delete(@PathVariable("callsign") String callsign) {
        Collection<EqslUser> eqslUsers = eqslUserRepository.findByCallsign(callsign);
        Iterator<EqslUser> iterator = eqslUsers.iterator();
        while (iterator.hasNext()) {
        	eqslUserRepository.delete(iterator.next());
        }
    }
 
    // additional CRUD methods
    
    // Get list of contacts by callsign (used for ajax requests from log page)
	@GetMapping(path="/{callsign}", produces = "application/json")
	public @ResponseBody Collection<EqslUser> getContactsByCallsign(@PathVariable String callsign) {
		return eqslUserRepository.findByCallsign(callsign);
	}

}