package weblog.service.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import weblog.model.security.Authority;
import weblog.model.security.User;
import weblog.repository.security.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
	
    @Override
	    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
			
	    // Find the user with the repository and if there is no launching a exception
	    User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("no user"));
			
	    // Map our Authority list with the spring security 
	    List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
	    for (Authority authority : user.getAuthority()) {
	        // ROLE_USER, ROLE_ADMIN,..
	        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
	        grantList.add(grantedAuthority);
	    }
			
	    // Create the UserDetails object that is going to be in session and return it.
	    UserDetails userDetails = (UserDetails) new User (user.getUsername(), user.getPassword(), grantList);
        
	    return userDetails;
    }
}