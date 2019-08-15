package weblog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import weblog.model.LoTWUser;

@Component
public class LotwCsvDownloader extends ScheduledTasks {
	
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private LoTWUserRepository lotwUserRepository;
	
	// Weekly, delay of 10 seconds
	// Useful for testing but disable in prod
	@Scheduled(fixedRate = 604800,initialDelay = 10000)
	public void scheduleTaskWithInitialDelay() {
		//this.populateLotwUsersTable();
	}
	
	// Every Saturday at 3am
	@Scheduled(cron = "0 3 * * 6 ?")
	public void scheduleTaskWithCronExpression() {
		this.populateLotwUsersTable();
	}
	
	private void populateLotwUsersTable() {
		logger.info("Updating LoTW User Data");
		BufferedReader reader;
		try {
			URL lotwUrl = new URL("https://lotw.arrl.org/lotw-user-activity.csv");
			reader = new BufferedReader(new InputStreamReader(lotwUrl.openStream()));
			
			// Assuming we got this far, delete existing entries
			lotwUserRepository.deleteAll();
			
			String line;
		    while ((line = reader.readLine()) != null) {
		        String[] values = line.split(",");
		        LoTWUser lotwUser = new LoTWUser();
		        lotwUser.setCallsign(values[0]);
		        lotwUser.setLastUpload(dateTimeFormat.parse(values[1] + " " + values[2]));
		        
		        lotwUserRepository.save(lotwUser);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// Skip anything that we can't parse the date for
			logger.error("Couldn't parse date!");
		}
	}
}
