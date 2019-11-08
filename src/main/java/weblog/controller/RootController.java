package weblog.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import weblog.BandStats;
import weblog.model.Logbook;
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
    
    @GetMapping("/public/{username}/**")
    public String userPublicLogbookPage(@PathVariable String username, Model model, HttpServletRequest request) {
    	
    	String[] path = request.getRequestURI().split("/", 4);
    	
    	Logbook logbook = logbookService.getUserLogbookByName(username, path[3]);
    	
    	model.addAttribute("logbook", logbook);
    	model.addAttribute("map_url", "/" + logbook.getId());
    	
    	return "logbook_public";
    }

}
