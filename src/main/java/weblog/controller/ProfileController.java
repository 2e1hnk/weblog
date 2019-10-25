package weblog.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
        
        
        return "profile";
    }
}
