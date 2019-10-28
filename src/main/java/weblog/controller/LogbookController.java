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

import weblog.model.Logbook;
import weblog.service.LogbookService;

@Controller
@RequestMapping(path="/logbook")
public class LogbookController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LogbookService logbookService;
	
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model, HttpServletResponse response) throws IOException {
        Logbook logbook = logbookService.getLogbookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid logbook id:" + id));
        logbookService.delete(logbook);
        return "redirect:/profile";
    }
	
}
