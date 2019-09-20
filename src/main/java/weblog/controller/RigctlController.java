package weblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import weblog.RigctlMessage;

@RestController
@RequestMapping(path="/rigctl")
public class RigctlController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Autowired
	private SimpMessagingTemplate template;
	
	@GetMapping(path="")
	public @ResponseBody String dummy() {
		return "OK";
	}

	@GetMapping(path="/update/{rigId}/{attribute}/{value}")
	public @ResponseBody String httpUpdate(@PathVariable int rigId, @PathVariable String attribute, @PathVariable String value) {
		
		RigctlMessage message = new RigctlMessage();
		message.setRigId(rigId);
		
		try {
			message.setValueByAttributeName(attribute, value);
			template.convertAndSend("/topic/rigctl/" + message.getRigId(), message);
			return "OK";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
	}

	@MessageMapping("/update/{rigId}")
    @SendTo("/topic/rigctl/{rigId}")
    public RigctlMessage wsUpdate(@DestinationVariable int rigId, RigctlMessage message) throws Exception {
		logger.info(message.toString());
        Thread.sleep(1000); // simulated delay
        return message;
    }
	
}
