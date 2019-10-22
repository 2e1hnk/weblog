package weblog.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import weblog.ContactRepository;
import weblog.model.Contact;
import weblog.service.ContactService;

@Controller
@RequestMapping(path="/search")
public class PublicSearchController {
	
	@Autowired
	ContactService contactService;
	
    // Get list of contacts by callsign (used for ajax requests from log page)
	@GetMapping(path="/{callsign}")
	public @ResponseBody Collection<Contact> getContactsByCallsign(@PathVariable String callsign) {
		return contactService.getByCallsign(callsign);
	}
}
