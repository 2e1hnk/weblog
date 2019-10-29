package weblog.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import weblog.model.Contact;
import weblog.model.Logbook;
import weblog.model.User;
import weblog.service.UserService;

@Controller
@RequestMapping(path="/profile")
public class ProfileController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private UserService userService;
	
    @GetMapping("")
    public String home(Model model, Principal principal, @ModelAttribute("activelogbook") Logbook activeLogbook) {
    	
    	User user = userService.getThisUser();
        model.addAttribute("user", user);
        model.addAttribute("themes", userService.listThemeNames());
        
        
        return "profile";
    }
    
    @PostMapping("")
    public String update(@Valid User user, Principal principal, BindingResult result, Model model, HttpServletResponse response, RedirectAttributes attributes, @ModelAttribute("activelogbook") Logbook activeLogbook) {
    	
    	logger.info(user.toString());
    	
    	User myUser = userService.getThisUser();
    	myUser.setTheme(user.getTheme());
    	
    	userService.save(myUser);
    	
    	return home(model, principal, activeLogbook);
    }
}
