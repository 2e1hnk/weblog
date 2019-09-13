package weblog.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import weblog.ContactRepository;
import weblog.EventStreamMessage;
import weblog.model.Contact;
import weblog.service.ContactService;
import weblog.service.SearchService;

@Controller
@RequestMapping(path="/log")
public class ContactController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final ContactRepository contactRepository;
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	SearchService service;
	
	@Autowired
	private EventStreamController eventStream;

    @Autowired
    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
    
    @GetMapping("")
    public String home(Model model, Contact contact, @RequestParam("page") Optional<Integer> page, 
    	      @RequestParam("size") Optional<Integer> size, @RequestParam("edit") Optional<Long> editId) {
    	
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        
        PageRequest pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Contact> contactPage = contactService.getPaginatedArticles(pageable);
        
        int totalPages = contactPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("activeContactList", true);
        model.addAttribute("contactList", contactPage.getContent());
        
        if ( editId.isPresent() ) {
        	contact = contactRepository.findById(editId.get())
                .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + editId.get()));
        }
        
        model.addAttribute("contact", contact);
              
        return "index";
    }
    
    @GetMapping("/add-contact")
    public String showAddContactForm(Contact contact) {
        return "add-contact";
    }
    
    @PostMapping("/addcontact")
    public String addContact(@Valid Contact contact, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "index";
        }
        
        if ( contact.getTimestamp() == null ) {
        	contact.setTimestamp(new Date());
        }
        
        contact.setCallsign(contact.getCallsign().toUpperCase());
        
        contactRepository.save(contact);
        
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
        
        return this.home(model, nextContact, Optional.empty(), Optional.empty(), Optional.empty());
        
        //return "redirect:/log";
        // model.addAttribute("contacts", contactRepository.findAll());
        // return "index";
    }
    
    @GetMapping("/edit/{id}")
    public String showUpdateContactForm(@PathVariable("id") long id, Model model) {
        Contact contact = contactRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
         
        model.addAttribute("contact", contact);
        model.addAttribute("submitUrl", "/log/update/" + id);
        return this.home(model, contact, Optional.empty(), Optional.empty(), Optional.empty());
    }
    
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Contact contact, 
      BindingResult result, Model model) {
        if (result.hasErrors()) {
            contact.setId(id);
            return "index";
        }
             
        contactRepository.save(contact);
        //model.addAttribute("contacts", contactRepository.findAll());
        return "redirect:/log";
    }
         
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        Contact contact = contactRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
        contactRepository.delete(contact);
        //model.addAttribute("contacts", contactRepository.findAll());
        return "redirect:/log";
    }
 
    // additional CRUD methods
    
    // Get list of contacts by callsign (used for ajax requests from log page)
	@GetMapping(path="/search/{callsign}")
	public @ResponseBody Collection<Contact> getContactsByCallsign(@PathVariable String callsign) {
		return contactRepository.findByCallsign(callsign);
	}
	
	// Run full-text search
	@GetMapping(path="/find")
	public String searchContacts(Model model, Contact contact, @RequestParam String q) {
		List<Contact> result = service.fuzzySearch(q);
		logger.info("Found " + result.size() + " contacts");
		model.addAttribute("contactList", result);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("activeContactList", true);
        model.addAttribute("contact", contact);
		return "index";
	}

}