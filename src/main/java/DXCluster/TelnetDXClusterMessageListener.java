package DXCluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class TelnetDXClusterMessageListener implements ApplicationListener<TelnetDXClusterMessageEvent> {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SimpMessagingTemplate template;
	
    @Override
    public void onApplicationEvent(TelnetDXClusterMessageEvent event) {
        logger.info("Received spring custom event - " + event.getSpot());
        
		try {
			template.convertAndSend("/topic/dx-spots", event.getSpot());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}