package weblog.service;

import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import weblog.UserRepository;
import weblog.exception.UsernameAlreadyExistsException;
import weblog.model.Logbook;
import weblog.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LogbookService logbookService;
    
    public Page<User> getPaginatedArticles(Pageable pageable) {
        return userRepository.findAllByOrderByUsernameAsc(pageable);
    }
    
    public User getUser(String username) {
    	return userRepository.findByUsername(username);
    }
    
    public User getThisUser() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return this.getUser(authentication.getName());
    }
    
    public Optional<User> getById(long id) {
    	return userRepository.findById(id);
    }
    
    public void enable(User user) {
    	user.setEnabled(true);
    	save(user);
    }
    
    public void disable(User user) {
    	user.setEnabled(false);
    	save(user);
    }
    
    public String resetPassword(User user) {
    	String password = this.generatePassword(5);
    	user.setPassword(this.encodePassword(password));
    	save(user);
    	return password;
    }
    
    public void save(User user) {
    	userRepository.save(user);
    }
    
    public void delete(User user) {
    	userRepository.delete(user);
    }
    
    public void associateUserWithLogbook(User user, Logbook logbook) {
    	user.associateWithLogbook(logbook);
    	save(user);
    }
    
    public String addUser(User user) throws UsernameAlreadyExistsException {
    	
    	// Check if the username already exists
    	if ( getUser(user.getUsername()) != null ) {
    		throw new UsernameAlreadyExistsException();
    	}
    	
    	// Generate a password and save the user using this password
    	String generatedPassword = this.generatePassword(5);
    	user.setPassword(this.encodePassword(generatedPassword));
    	this.save(user);
    	
    	// Create a default logbook for the user
    	Logbook logbook = logbookService.createLogbook(user.getUsername());
    	logbookService.associateLogbookWithUser(logbook, user);
    	this.associateUserWithLogbook(user, logbook);
    	
    	// Return the generated (not-encoded) password for info
    	return generatedPassword;
    }
    
    public void makeAdmin(User user) {
    	user.setAdmin(true);
    	save(user);
    }
    
    public void makeNonAdmin(User user) {
    	user.setAdmin(false);
    	save(user);
    }
    
    public String generatePassword(int length) {
    	String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    	SecureRandom random = new SecureRandom();
    	
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

			int rndCharAt = random.nextInt(validChars.length());
            char rndChar = validChars.charAt(rndCharAt);

            sb.append(rndChar);
        }

        return sb.toString();
    }
    
    public String encodePassword(String password) {
    	PasswordEncoder encoder = new BCryptPasswordEncoder(11);
    	return encoder.encode(password);
    }
}
