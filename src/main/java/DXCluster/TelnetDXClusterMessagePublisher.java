package DXCluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TelnetDXClusterMessagePublisher {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
 
    public void publishEvent(final Spot spot) {
        logger.info("Publishing spot: " + spot.toString());
        TelnetDXClusterMessageEvent customSpringEvent = new TelnetDXClusterMessageEvent(this, spot);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }
}
