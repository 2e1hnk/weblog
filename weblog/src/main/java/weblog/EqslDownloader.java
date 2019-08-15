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

import weblog.model.EqslUser;

@Component
public class EqslDownloader extends ScheduledTasks {
	
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private EqslUserRepository eqslUserRepository;
	
	// Weekly, delay of 10 seconds
	// Useful for testing but disable in prod
	@Scheduled(fixedRate = 604800,initialDelay = 10000)
	public void scheduleTaskWithInitialDelay() {
		// this.populateEqslUsersTable();
	}
	
	// Every Saturday at 3am
	@Scheduled(cron = "0 3 * * 6 ?")
	public void scheduleTaskWithCronExpression() {
		this.populateEqslUsersTable();
	}
	
	private void populateEqslUsersTable() {
		logger.info("Updating Eqsl User Data");
		BufferedReader reader;
		try {
			URL lotwUrl = new URL("https://www.eqsl.cc/qslcard/DownloadedFiles/AGMemberList.txt");
			reader = new BufferedReader(new InputStreamReader(lotwUrl.openStream()));
			
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
