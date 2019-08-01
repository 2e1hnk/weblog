package weblog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/rigctl")
public class RigctlController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@MessageMapping("/update")
    @SendTo("/topic/rigctl")
    public RigctlMessage update(RigctlMessage message) throws Exception {
		logger.info(message.toString());
        Thread.sleep(1000); // simulated delay
        return message;
    }
}
