package weblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import weblog.model.User;
import weblog.service.UserService;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {
	
	@Autowired
	private UserService userService;
 
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    public User getUser() {
    	Authentication authentication = getAuthentication();
    	User user = userService.getUser(authentication.getName());
    	return user;
    }
}