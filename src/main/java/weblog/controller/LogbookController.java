package weblog.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import weblog.model.Logbook;
import weblog.service.LogbookService;

@Controller
@RequestMapping(path="/logbook")
public class LogbookController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LogbookService logbookService;
	
    @GetMapping("/delete/{logbook}")
    public String delete(@PathVariable Logbook logbook, Model model, HttpServletResponse response) throws IOException {
        
        logbookService.delete(logbook);
        
        return "redirect:/profile";
    }
	
    @GetMapping("/move/{fromLogbook}")
    public String move(@PathVariable Logbook fromLogbook, @RequestParam Logbook toLogbook, @RequestParam Boolean deleteAfterMove, Model model, HttpServletResponse response) throws IOException {
    	
    	logbookService.moveContacts(fromLogbook, toLogbook, deleteAfterMove);
    	
        return "redirect:/profile";
    }
	
}
