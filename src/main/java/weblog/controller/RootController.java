package weblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import weblog.service.StatsService;


@Controller
@RequestMapping(path="")
public class RootController {
	
	@Autowired
	private StatsService statsService;
	
    @GetMapping("")
    public String home(Model model) {
    	
    	model.addAllAttributes(statsService.getStatsTable());
    	
    	return "home";
    }
    
    @GetMapping("/map")
    public String map(Model model) {
    	return "map";
    }

}
