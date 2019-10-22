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

@Service
public class ContactService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
    @Autowired
    private ContactRepository contactRepository;
    
    public Page<Contact> getPaginatedLogbookEntries(Pageable pageable) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	logger.info("Username: " + authentication.getName());
    	return contactRepository.findByLogbookOrderByTimestampDesc(authentication.getName(), pageable);
    }
    
    public Collection<Contact> findAll() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return contactRepository.findByLogbook(authentication.getName());
    }
    
    public Optional<Contact> getById(Long id) {
    	return contactRepository.findById(id);
    }
    
    public Collection<Contact> getByCallsign(String callsign) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return contactRepository.findByLogbookInAndCallsignIn(authentication.getName(), callsign);
    }
    
    public void save(Contact contact) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	contact.setLogbook(authentication.getName());
    	contactRepository.save(contact);
    }
    
    public void delete(Contact contact) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if ( contact.getLogbook().equals(authentication.getName())) {
    		contactRepository.delete(contact);
    	}
    }
    
}
