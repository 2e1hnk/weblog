package weblog.service;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import weblog.ContactRepository;
import weblog.model.Contact;
import weblog.model.Logbook;
import weblog.model.User;

@Service
public class ContactService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private ContactRepository contactRepository;
    
    public Page<Contact> getPaginatedLogbookEntries(Pageable pageable, Logbook logbook) {
    	return contactRepository.findByLogbookOrderByTimestampDesc(logbook, pageable);
    }
    
    public Collection<Contact> findAll() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return contactRepository.findByLogbook(authentication.getName());
    }
    
    public Collection<Contact> findByLogbookId(Long logbookId) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return contactRepository.findByLogbookId(logbookId);
    }
    
    public Optional<Contact> getById(Long id) {
    	return contactRepository.findById(id);
    }
    
    public Collection<Contact> getByCallsign(String callsign) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return contactRepository.findByLogbookInAndCallsignIn(authentication.getName(), callsign);
    }
    
    public void save(Contact contact) {
    	//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	//User user = userService.getUser(authentication.getName());
    	//contact.setLogbook(user.getAnyLogbook());
    	contactRepository.save(contact);
    }
    
    public void delete(Contact contact) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if ( contact.getLogbook().equals(authentication.getName())) {
    		contactRepository.delete(contact);
    	}
    }
    
}
