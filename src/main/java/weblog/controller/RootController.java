package weblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(path="")
public class RootController {
	
    @GetMapping("")
    public String home(Model model) {
    	return "redirect:/log/";
    }
    
    @GetMapping("/map")
    public String map(Model model) {
    	return "map";
    }

}
