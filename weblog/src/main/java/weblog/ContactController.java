package weblog;

import java.util.Collection;

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

@Controller
//@RequestMapping(path="/contact")
public class ContactController {
	
	private final ContactRepository contactRepository;

    @Autowired
    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
    
    @GetMapping("")
    public String home(Model model, Contact contact) {
        model.addAttribute("contacts", contactRepository.findAll());
        return "index";
    }
    
    @GetMapping("/add-contact")
    public String showAddContactForm(Contact contact) {
        return "add-contact";
    }
    
    @PostMapping("/addcontact")
    public String addContact(@Valid Contact contact, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-contact";
        }
         
        contactRepository.save(contact);
        model.addAttribute("contacts", contactRepository.findAll());
        return "index";
    }
    
    @GetMapping("/edit/{id}")
    public String showUpdateContactForm(@PathVariable("id") long id, Model model) {
        Contact contact = contactRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
         
        model.addAttribute("contact", contact);
        return "update-contact";
    }
    
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Contact contact, 
      BindingResult result, Model model) {
        if (result.hasErrors()) {
            contact.setId(id);
            return "update-contact";
        }
             
        contactRepository.save(contact);
        model.addAttribute("contacts", contactRepository.findAll());
        return "index";
    }
         
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        Contact contact = contactRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
        contactRepository.delete(contact);
        model.addAttribute("contacts", contactRepository.findAll());
        return "index";
    }
 
    // additional CRUD methods
    
    // Get list of contacts by callsign (used for ajax requests from log page)
	@GetMapping(path="/search/{callsign}")
	public @ResponseBody Collection<Contact> getContactsByCallsign(@PathVariable String callsign) {
		return contactRepository.findByCallsign(callsign);
	}

}