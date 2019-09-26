package weblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import weblog.UserRepository;
import weblog.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public Page<User> getPaginatedArticles(Pageable pageable) {
        return userRepository.findAllByOrderByUsernameAsc(pageable);
    }
}
