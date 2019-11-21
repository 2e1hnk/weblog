package weblog.controller;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.rjeschke.txtmark.Processor;

import weblog.BandStats;
import weblog.model.BlogComment;
import weblog.model.BlogPost;
import weblog.model.Logbook;
import weblog.model.Tag;
import weblog.model.User;
import weblog.repository.BlogCommentRepository;
import weblog.repository.BlogPostRepository;
import weblog.service.BlogPostService;
import weblog.service.FileSystemStorageService;
import weblog.service.LogbookService;
import weblog.service.StatsService;
import weblog.service.StorageService;
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
	
	@Autowired
	private FileSystemStorageService storageService;
	
	@Autowired
	private BlogPostService blogPostService;
	
	@Autowired
	private BlogCommentRepository blogCommentRepository;
	
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
    	model.addAttribute("posts", blogPostService.findUsersBlogPosts(user));	// This isn't strictly necessary but useful to make the template compatible with filtered lists
    	return "blogthemes/" + user.getBlogTheme() + "/blog";
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
    	
    	model.addAttribute("url_base", "/public/" + user.getUsername());
    	return "blogthemes/" + user.getBlogTheme() + "/blog";
    }

    @GetMapping("/public/{username}/gallery/{filename}")
    public ResponseEntity<Resource> userPublicGalleryImage(@PathVariable String username, @PathVariable String filename, Model model, HttpServletRequest request) {
    	User user = userService.getUser(username);
    	model.addAttribute("user", user);
    	
    	Resource imgFile = storageService.loadAsResource(null, user.getUsername() + "/gallery/" + filename);
    	
    	model.addAttribute("url_base", "/public/" + user.getUsername());
    	return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imgFile);
    }

    @GetMapping("/public/{username}/blog/{blogPost}")
    public String userPublicBlogPage(@PathVariable String username, @PathVariable BlogPost blogPost, Model model, HttpServletRequest request) {
    	User user = userService.getUser(username);
    	if ( blogPost.getUser().equals(user) ) {
    		model.addAttribute("user", user);
    		
    		blogPost.incrementViews();
    		blogPostService.save(blogPost);
    		
    		model.addAttribute("blog", blogPost);
    	}
    	model.addAttribute("url_base", "/public/" + user.getUsername());
    	return "blogthemes/" + user.getBlogTheme() + "/blogpost";
    }
    
    @PostMapping("/public/{username}/blog/{blogPost}/addcomment")
    public String userPublicLogbookPage(@PathVariable String username, @PathVariable BlogPost blogPost, Model model, HttpServletRequest request, @RequestParam String comment) {
    	
    	if ( blogPost.isCommentsEnabled() ) {
    		if ( !blogPost.isAnonymousCommentsEnabled() ) {
    			User user = userService.getThisUser();
    			BlogComment blogComment = new BlogComment();
    			blogComment.setUser(user);
    			blogComment.setTimestamp(new Date());
    			blogComment.setComment(comment);
    			blogComment.setBlogPost(blogPost);
    			blogPost.addComment(blogComment);
    			
    			blogCommentRepository.save(blogComment);
    			blogPostService.save(blogPost);
    		}
    	} else {
    		// Comments not enabled
    		logger.error("Comment attempted by an unauthenticated user! " + request.getRemoteAddr());
    	}
    	
    	return "redirect:/public/" + username + "/blog/" + blogPost.getId();
    	
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
