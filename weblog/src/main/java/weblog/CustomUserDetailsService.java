package weblog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private WebApplicationContext applicationContext;
    
    @Autowired
    private UserRepository userRepository;

    public CustomUserDetailsService() {
        super();
    }

    @PostConstruct
    public void completeSetup() {
        userRepository = applicationContext.getBean(UserRepository.class);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws  UsernameNotFoundExcaption {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException(
                  "No user found with username: " + email);
            }
             
            return new org.springframework.security.core.userdetails.User(
              user.getEmail(), 
              user.getPassword().toLowerCase(), 
              user.isEnabled(), 
              accountNonExpired, 
              credentialsNonExpired, 
              accountNonLocked, 
              getAuthorities(user.getRole()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}