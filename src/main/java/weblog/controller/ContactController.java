package weblog.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import weblog.ContactRepository;
import weblog.EventStreamMessage;
import weblog.exception.PermissionsException;
import weblog.model.Contact;
import weblog.model.Logbook;
import weblog.model.User;
import weblog.service.ContactService;
import weblog.service.LogbookService;
import weblog.service.SearchService;
import weblog.service.UserService;

@Controller
@RequestMapping(path="/log")
@SessionAttributes("activelogbook")
public class ContactController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	SearchService service;
	
	@Autowired
	private EventStreamController eventStream;
	
	@Autowired UserService userService;
	
	@Autowired LogbookService logbookService;
	
	@Autowired
    public ContactController() {
    	
    }
    
    
    @GetMapping("/add-contact")
    public String showAddContactForm(Contact contact) {
        return "add-contact";
    }
    
    @PostMapping("/addcontact")
    public String addContact(@Valid Contact contact, BindingResult result, Model model, HttpServletResponse response, @ModelAttribute("activelogbook") Logbook activeLogbook, RedirectAttributes attributes) throws IOException {
    	
    	logger.info("Contact logbook: " + contact.getLogbook().getName());
    	
    	activeLogbook = contact.getLogbook();
    	attributes.addFlashAttribute("activelogbook", activeLogbook);
    	
    	User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("user", user);
    	
    	if (result.hasErrors()) {
            return "index";
        }
        
        if ( contact.getTimestamp() == null ) {
        	contact.setTimestamp(new Date());
        }
        
        contact.setCallsign(contact.getCallsign().toUpperCase());
        
        contactService.save(contact);
        
        EventStreamMessage event = new EventStreamMessage("contact", "new", contact.toString());
        try {
			eventStream.sendMessage(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // Set up the next contact. We can use this to populate default fields or to
        // copy over details from the last contact to the next one.
        Contact nextContact = new Contact();
        nextContact.setBand(contact.getBand());
        nextContact.setFrequency(contact.getFrequency());
        nextContact.setMode(contact.getMode());
        nextContact.setOpName(contact.getOpName());
        
        if ( nextContact.getMode().equals("CW") ) {
        	nextContact.setRstr("599");
        	nextContact.setRsts("599");
        } else {
        	nextContact.setRstr("59");
        	nextContact.setRsts("59");
        }
        
        // this.home(model, nextContact, response, Optional.empty(), Optional.empty(), Optional.empty(), activeLogbook, Optional.empty(), Optional.empty());
        
        return "redirect:/logbook";
        // model.addAttribute("contacts", contactRepository.findAll());
        // return "index";
    }
    
    @GetMapping("/edit/{contact}")
    public String showUpdateContactForm(@PathVariable Contact contact, Model model, HttpServletResponse response,
    		@ModelAttribute("activelogbook") Logbook activeLogbook) throws IOException {
    	
    	model.addAttribute("activelogbook", activeLogbook);
    	
        model.addAttribute("contact", contact);
        model.addAttribute("submitUrl", "/log/update/" + contact.getId());
        //return this.home(model, contact, response, Optional.empty(), Optional.empty(), Optional.empty(), activeLogbook, Optional.empty(), Optional.empty());
        return "redirect:/logbook";
    }
    
	@RequestMapping(value = "/{contact}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Contact> getContact(@PathVariable Contact contact) {
		return ResponseEntity.ok(contact);
	}
    
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Contact contact, 
      BindingResult result, Model model) {
        if (result.hasErrors()) {
            contact.setId(id);
            return "index";
        }
             
        contactService.save(contact);
        //model.addAttribute("contacts", contactRepository.findAll());
        return "redirect:/log";
    }
         
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        Contact contact = contactService.getById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
        contactService.delete(contact);
        //model.addAttribute("contacts", contactRepository.findAll());
        return "redirect:/log";
    }
 
    // additional CRUD methods
    
    // Get list of contacts by callsign (used for ajax requests from log page)
	@GetMapping(path="/search/{callsign}")
	public @ResponseBody Collection<Contact> getContactsByCallsign(@PathVariable String callsign) {
		return contactService.getByCallsign(callsign);
	}
	
	// Run full-text search
	@GetMapping(path="/find")
	public String searchContacts(Model model, Contact contact, @RequestParam String q) {
        
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("user", user);
        
		//List<Contact> result = service.fuzzySearch(q);
        List<Contact> result = null;
		try {
			result = service.perUserSearch(q);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		logger.info("Found " + result.size() + " contacts");
		model.addAttribute("contactList", result);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("activeContactList", true);
        model.addAttribute("contact", contact);
        
		return "index";
	}
	
	@ModelAttribute("activelogbook")
	public Logbook activeLogbook() {
	    return new Logbook();
	}

}