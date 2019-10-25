package weblog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This class intercepts requests and adds the username to the model to make it available
 * if necessary, and if the user is logged in.
 *
 */

public class UserInterceptor extends HandlerInterceptorAdapter {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
	    if (isUserLogged()) {
	        addToModelUserDetails(request.getSession());
	    }
	    return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object o, ModelAndView model) throws Exception {
	    if (model != null && !isRedirectView(model)) {
	        if (isUserLogged()) {
		        addToModelUserDetails(model);
		    }
	    }
	}
	
	public static boolean isUserLogged() {
	    try {
	        return !SecurityContextHolder.getContext().getAuthentication()
	          .getName().equals("anonymousUser");
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	private void addToModelUserDetails(HttpSession session) {
	    String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
	    session.setAttribute("username", loggedUsername);
	}
	
	private void addToModelUserDetails(ModelAndView model) {
	    String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
	    model.addObject("loggedUsername", loggedUsername);
	}
	
	public static boolean isRedirectView(ModelAndView mv) {
	    String viewName = mv.getViewName();
	    if (viewName.startsWith("redirect:/")) {
	        return true;
	    }
	    View view = mv.getView();
	    return (view != null && view instanceof SmartView
	      && ((SmartView) view).isRedirectView());
	}

}
