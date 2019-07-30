package weblog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ClublogDownloader extends ScheduledTasks {
	
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private ClublogUserRepository clublogUserRepository;
	
	// Weekly, delay of 10 seconds
	// Useful for testing but disable in prod
	@Scheduled(fixedRate = 604800,initialDelay = 10000)
	public void scheduleTaskWithInitialDelay() {
		this.populateClublogUsersTable();
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
			ZipInputStream zipStream = new ZipInputStream(clublogUrl.openStream());
			
			// Assuming we got this far, delete existing entries
			clublogUserRepository.deleteAll();
			
			ZipEntry zipEntry;
			byte[] buffer = new byte[1024];
			
            while ((zipEntry = zipStream.getNextEntry()) != null) {
            	if ( !zipEntry.isDirectory() ) {
            		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            		int len;
            		while ((len = zipStream.read(buffer)) > 0) {
            			outStream.write(buffer, 0, len);
            		}
            		outStream.close();
            		
            		String strJson = new String(outStream.toByteArray());
	                logger.info(strJson);
	                
	                // Convert JSON string to List<ClublogUser>
	                ObjectMapper mapper = new ObjectMapper();
	                mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	                Map<String, ClublogUser> jsonMap = mapper.readValue(strJson,
	                	    new TypeReference<Map<String,ClublogUser>>(){});
	                
	                for ( String callsign : jsonMap.keySet() ) {
	                	jsonMap.get(callsign).setCallsign(callsign);
	                	clublogUserRepository.save(jsonMap.get(callsign));
	                }
            	}
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
