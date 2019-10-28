package weblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import weblog.IAuthenticationFacade;
import weblog.RigctlMessage;
import weblog.model.Logbook;
import weblog.model.User;
import weblog.service.LogbookService;

@RestController
@RequestMapping(path="/rigctl")
public class RigctlController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Autowired
	private SimpMessagingTemplate template;
	
    @Autowired
    private IAuthenticationFacade authenticationFacade;
    
    @Autowired
    private LogbookService logbookService;
	
	@GetMapping(path="")
	public @ResponseBody String dummy() {
		return "OK";
	}

	@RequestMapping(
			value="/update/{logbookId}/{attribute}/{value}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> httpUpdate(@PathVariable Long logbookId, @PathVariable String attribute, @PathVariable String value) {
		
		Logbook logbook = logbookService.getLogbookById(logbookId).get();
		
		// Check user has access to the logbook
		User user = authenticationFacade.getUser();
		if ( !user.getLogbooks().contains(logbook) ) {
			return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
		RigctlMessage message = new RigctlMessage();
		
		message.setLogbookId(logbook.getId());
		
		try {
			message.setValueByAttributeName(attribute, value);
			template.convertAndSend("/topic/rigctl/" + message.getLogbookId(), message);
			return new ResponseEntity<>("OK", HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>("Error setting attributes or sending message", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(
			value="/update/{logbookId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> httpUpdateWithParams(@PathVariable Long logbookId, @RequestParam(required = false) String frequency, @RequestParam(required = false) String mode) {
		
		Logbook logbook = logbookService.getLogbookById(logbookId).get();
		
		// Check user has access to the logbook
		User user = authenticationFacade.getUser();
		if ( !user.getLogbooks().contains(logbook) ) {
			return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
		RigctlMessage message = new RigctlMessage();
		
		message.setLogbookId(logbook.getId());
		
		try {
			if ( frequency != null )
				message.setValueByAttributeName("frequency", frequency);
			if ( mode != null )
				message.setMode(mode);
			template.convertAndSend("/topic/rigctl/" + message.getLogbookId(), message);
			return new ResponseEntity<>("OK", HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>("Error setting attributes or sending message", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@MessageMapping("/update/{logbookId}")
    @SendTo("/topic/rigctl/{logbookId}")
    public RigctlMessage wsUpdate(@DestinationVariable Long logbookId, RigctlMessage message) throws Exception {
		logger.info(message.toString());
        Thread.sleep(1000); // simulated delay
        return message;
    }
	
}
