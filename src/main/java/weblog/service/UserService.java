package weblog.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
