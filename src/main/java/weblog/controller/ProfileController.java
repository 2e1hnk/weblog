package weblog.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import weblog.model.BlogPost;
import weblog.model.Contact;
import weblog.model.Entitlement;
import weblog.model.Logbook;
import weblog.model.User;
import weblog.repository.BlogPostRepository;
import weblog.service.EntitlementService;
import weblog.service.UserService;

@Controller
@RequestMapping(path="/profile")
public class ProfileController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private UserService userService;
	@Autowired private EntitlementService entitlementService;
	@Autowired private BlogPostRepository blogPostRepository;
	
    @GetMapping("")
    public String home(Model model, Principal principal, @ModelAttribute("activelogbook") Logbook activeLogbook) {
    	User user = userService.getThisUser();
        model.addAttribute("user", user);
        model.addAttribute("themes", userService.listThemeNames());
        logger.info("Fetching blog themes");
        model.addAttribute("blog_themes", userService.listBlogThemeNames()); //TODO: This doesn't seem to be working
        return "profile";
    }
    
    @PostMapping("")
    public String update(@Valid User user, Principal principal, BindingResult result, Model model, HttpServletResponse response, RedirectAttributes attributes, @ModelAttribute("activelogbook") Logbook activeLogbook) {
    	
    	
    	logger.info(user.toString());
    	
    	
    	User myUser = userService.getThisUser();
    	myUser.setTheme(user.getTheme());
    	myUser.setBlogTheme(user.getBlogTheme());
    	userService.save(myUser);
    	
    	return home(model, principal, activeLogbook);
    }
    
    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String password1, @RequestParam String password2, Model model, Principal principal, HttpServletResponse response, RedirectAttributes attributes, @ModelAttribute("activelogbook") Logbook activeLogbook) {
    	
    	User user = userService.getThisUser();
    	List<String> messages = new ArrayList<String>();
        
    	if ( password1.equals(password2) ) {
    		user.setPassword(userService.encodePassword(password1));
    		userService.save(user);
    		
    		messages.add("Password updated!");
    	}
        
        attributes.addFlashAttribute("messages", messages);
        
        return "redirect:/profile";
    }
    
    @PostMapping("/blog/add")
    public String addBlogPost(@RequestParam String title, @RequestParam String contents, @RequestParam(required = false) String imageUrl, @RequestParam(required = false) boolean allowComments, @RequestParam(required = false) boolean allowAnonymousComments) {
    	BlogPost blogPost = new BlogPost();
    	User user = userService.getThisUser();
    	
    	blogPost.setUser(user);
    	blogPost.setAnonymousCommentsEnabled(allowAnonymousComments);
    	blogPost.setCommentsEnabled(allowComments);
    	blogPost.setTimestamp(new Date());
    	if ( !imageUrl.equals("") ) {
    		blogPost.setHeaderImageUrl(imageUrl);
    	}
    	blogPost.setPostContents(contents);
    	blogPost.setTitle(title);
    	
    	blogPostRepository.save(blogPost);
    	
    	return "redirect:/profile";
    }
    
    @GetMapping("/blog/delete/{blogPost}")
    public String deleteBlogPost(@PathVariable BlogPost blogPost, Model model, RedirectAttributes attributes) {
    	List<String> messages = new ArrayList<String>();
    	if ( blogPost.getUser().equals(userService.getThisUser()) ) {
    		blogPostRepository.delete(blogPost);
    		messages.add("Blog post deleted");
    	} else {
    		messages.add("You are not the owner of this blog post!");
    	}
    	attributes.addFlashAttribute("messages", messages);
    	return "redirect:/profile";
    }
}
