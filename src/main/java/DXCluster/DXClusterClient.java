package DXCluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.telnet.TelnetClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class DXClusterClient implements ApplicationListener<ApplicationReadyEvent> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${dxcluster.host}")
	private String host = "127.0.0.1";
	@Value("${dxcluster.port}")
	private String port = "7300";
	@Value("${dxcluster.user}")
	private String user = "2e1hnk";
	@Value("${dxcluster.login_prompt}")
	private String login_prompt = "login: ";

	@Autowired TelnetDXClusterMessagePublisher telnetDXClusterMessagePublisher;

	private static final Pattern dxRegex = Pattern.compile(
			"^DX\\sde\\s([a-zA-Z0-9\\/\\-#]+):\\s+([0-9\\.]+)\\s+([a-zA-Z0-9\\/]+)\\s+(.*)\\s([0-9]{4})Z\\s?([a-zA-Z0-9]{4})?");
	private static final Pattern wxRegex = Pattern.compile(
			"^WCY\\sde\\s([a-zA-Z0-9\\/\\-]+)\\s<([0-9]+)>\\s+:\\s+K=([0-9]+)\\s+expK=([0-9]+)\\s+A=([0-9]+)\\s+R=([0-9]+)\\s+SFI=([0-9]+)\\s+SA=([a-z]+)\\s+GMF=([a-z]+)\\s+Au=([a-z]+)$");

	@Async
//	@EventListener
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		boolean running = true;
	
		while (running) {
			try {
				TelnetClient telnetClient = new TelnetClient();
				
				logger.info(String.format("Connecting to DX Cluster %s:%s as %s", host, port, user));
	
				telnetClient.connect(host, Integer.parseInt(port));
				
				InputStream inStream = telnetClient.getInputStream();
				BufferedReader r = new BufferedReader(new InputStreamReader(inStream));
	
				OutputStream outStream = telnetClient.getOutputStream();
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outStream));
	
				while (running) {
	
					boolean lineComplete = false;
					String line = "";
					
					while ( !lineComplete ) {
						
						String nextChar = Character.toString((char) r.read());
						
						if ( nextChar.equals("\r") || nextChar.equals("\n") ) {
							try {
								if ( !line.equals("") ) {
									logger.info(line);
									
									Matcher dxMatch = dxRegex.matcher(line);
									Matcher wxMatch = wxRegex.matcher(line);
									
									if (dxMatch.find()) {
	
										Spot spot = new Spot(dxMatch.group(1), Double.valueOf(dxMatch.group(2)), dxMatch.group(3),
										dxMatch.group(4), Integer.parseInt(dxMatch.group(5)));
	
										if (dxMatch.group(6) != null) {
											spot.setGridsquare(dxMatch.group(6));
										}
	
										logger.info("Received spot: " + spot.toString());
										
										telnetDXClusterMessagePublisher.publishEvent(spot);
	
									} else if (wxMatch.find()) {
										// TODO: something
									} else {
										// Unrecognised line, log it for now
										logger.error("UNRECOGNISED DX CLUSTER MESSAGE: " + line);
									}
									lineComplete = true;
									
								}
							} catch (Exception e) {
								logger.error("Oops, exception thrown!", e);
								e.printStackTrace();
							}
							line = "";
							continue;
						}
						
						line += nextChar;
						
						if ( line.equals(login_prompt) ) {
							logger.info("Logging in to telnet server " + host + ":" + port);
							w.write(user + "\n");
							w.flush();
							line = "";
							lineComplete = true;
							continue;
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// Couldn't connect, wait 10 secs then retry
				try {
					Thread.sleep(10000);
					logger.info("Attempting reconnection to DXCluster " + host + ":" + port);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
