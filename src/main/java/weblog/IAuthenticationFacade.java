package weblog;

import org.springframework.security.core.Authentication;

import weblog.model.User;

public interface IAuthenticationFacade {
	Authentication getAuthentication();
	User getUser();
}
