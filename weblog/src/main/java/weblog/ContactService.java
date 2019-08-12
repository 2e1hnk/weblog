package weblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;
    
    public Page<Contact> getPaginatedArticles(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }
}
