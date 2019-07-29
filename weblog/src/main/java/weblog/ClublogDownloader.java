package weblog;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClublogDownloader extends ScheduledTasks {
	
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private EqslUserRepository eqslUserRepository;
	
	// Weekly, delay of 10 seconds
	// Useful for testing but disable in prod
	@Scheduled(fixedRate = 604800,initialDelay = 10000)
	public void scheduleTaskWithInitialDelay() {
		// this.populateClublogUsersTable();
	}
	
	// Every Saturday at 3am
	@Scheduled(cron = "0 3 * * 6 ?")
	public void scheduleTaskWithCronExpression() {
		this.populateClublogUsersTable();
	}
	
	private void populateClublogUsersTable() {
		logger.info("Updating clublog User Data");
		BufferedInputStream reader;
		try {
			URL clublogUrl = new URL("https://secure.clublog.org/clublog-users.json.zip");
			InputStream inputStream = clublogUrl.openStream();
			reader = new BufferedInputStream(new InputStream(clublogUrl.openStream()));
			
			// Assuming we got this far, delete existing entries
			eqslUserRepository.deleteAll();
			
			String line;
		    while ((line = reader.readLine()) != null) {
		        EqslUser eqslUser = new EqslUser();
		        eqslUser.setCallsign(line);
		        
		        eqslUserRepository.save(eqslUser);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
