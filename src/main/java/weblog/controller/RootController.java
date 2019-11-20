package weblog.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rjeschke.txtmark.Processor;

import weblog.BandStats;
import weblog.model.BlogPost;
import weblog.model.Logbook;
import weblog.model.Tag;
import weblog.model.User;
import weblog.service.LogbookService;
import weblog.service.StatsService;
import weblog.service.UserService;


@Controller
@RequestMapping(path="")
public class RootController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StatsService statsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LogbookService logbookService;
	
    @GetMapping("")
    public String home(Model model) {
    	
    	model.addAllAttributes(statsService.getStatsTable());
    	
    	for ( BandStats bandStats : statsService.getBandStatsList() ) {
    		model.addAttribute("band_" + bandStats.getBand(), bandStats.getCount());
    	}
    	
    	model.addAttribute("user", userService.getThisUser());
    	
    	return "home";
    }
    
    @GetMapping("/map")
    public String map(Model model) {
    	return "map";
    }
    
    @GetMapping("/public/{username}/blog")
    public String userPublicBlog(@PathVariable String username, Model model, HttpServletRequest request) {
    	User user = userService.getUser(username);
    	model.addAttribute("user", user);
    	model.addAttribute("posts", user.getBlogPosts());	// This isn't strictly necessary but useful to make the template compatible with filtered lists
    	return "blog";
    }
    
    @GetMapping("/public/{username}/blog/tag/{tag}")
    public String userPublicTaggedBlogPage(@PathVariable String username, @PathVariable Tag tag, Model model, HttpServletRequest request) {
    	User user = userService.getUser(username);
    	model.addAttribute("user", user);
    	
    	Set<BlogPost> taggedBlogPosts = new HashSet<BlogPost>();
    	for ( BlogPost blogPost : tag.getBlogPosts() ) {
    		if ( blogPost.getUser().equals(user) ) {
    			taggedBlogPosts.add(blogPost);
    		}
    	}
    	model.addAttribute("posts", taggedBlogPosts);
    	
    	return "blog";
    }

    @GetMapping("/public/{username}/blog/{blogPost}")
    public String userPublicBlogPage(@PathVariable String username, @PathVariable BlogPost blogPost, Model model, HttpServletRequest request) {
    	User user = userService.getUser(username);
    	if ( blogPost.getUser().equals(user) ) {
    		model.addAttribute("user", user);
    		model.addAttribute("blog", blogPost);
    	}
    	return "blogpost";
    }
    
    @GetMapping("/public/{username}/**")
    public String userPublicLogbookPage(@PathVariable String username, Model model, HttpServletRequest request) {
    	
    	String[] path = request.getRequestURI().split("/", 4);
    	
    	Logbook logbook = logbookService.getUserLogbookByName(username, path[3]);
    	
    	model.addAttribute("logbook", logbook);
    	model.addAttribute("map_url", "/" + logbook.getId());
    	
    	return "logbook_public";
    }

}
