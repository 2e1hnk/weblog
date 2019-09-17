package weblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import weblog.EventStreamMessage;

@RestController
@RequestMapping(path="/event-stream")
public class EventStreamController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Autowired
	private SimpMessagingTemplate template;
	
	@GetMapping(path="")
	public @ResponseBody String dummy() {
		return "OK";
	}

	//@MessageMapping("/event-stream")
    @SendTo("/topic/event-stream")
    public EventStreamMessage sendMessage(EventStreamMessage message) throws Exception {
		logger.info(message.toString());
        Thread.sleep(1000); // simulated delay
        return message;
    }
}
