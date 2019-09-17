package weblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import weblog.ContactRepository;
import weblog.model.Contact;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;
    
    public Page<Contact> getPaginatedArticles(Pageable pageable) {
        return contactRepository.findAllByOrderByTimestampDesc(pageable);
    }
}
