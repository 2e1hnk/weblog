package weblog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ADIFStorageService implements StorageService {
	
	Pattern regex_date = Pattern.compile("<QSO_DATE:[0-9]*>([0-9]{8})");
	Pattern regex_time = Pattern.compile("<TIME_ON:[0-9]*>([0-9]{4})");
	Pattern regex_band = Pattern.compile("<BAND:[0-9]*>([0-9]{0,4}[a-zA-Z]{0,2})");
	Pattern regex_mode = Pattern.compile("<MODE:[0-9]*>([a-zA-Z0-9]{0,8})");
	Pattern regex_callsign = Pattern.compile("<CALL:[0-9]*>([a-zA-Z0-9/]{0,10})");
	Pattern regex_rsts = Pattern.compile("<RST_SENT:[0-9]*>([0-9]{0,3})");
	Pattern regex_rstr = Pattern.compile("<RST_RCVD:[0-9]*>([0-9]{0,3})");
	Pattern regex_grid = Pattern.compile("<GRIDSQUARE:[0-9]*>([a-zA-Z0-9]{0,6})");
	
	SimpleDateFormat adifDateFormatter = new SimpleDateFormat("yyyyMMddHHmm");
	
	@Autowired ContactRepository contactRepository;

    @Autowired
    public ADIFStorageService() {
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
            	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            	String line;
            	while ((line = reader.readLine()) != null) {
            		Matcher dateMatcher = regex_date.matcher(line);
            		Matcher timeMatcher = regex_time.matcher(line);
            		Matcher bandMatcher = regex_band.matcher(line);
            		Matcher modeMatcher = regex_mode.matcher(line);
            		Matcher callsignMatcher = regex_callsign.matcher(line);
            		Matcher rstsMatcher = regex_rsts.matcher(line);
            		Matcher rstrMatcher = regex_rstr.matcher(line);
            		Matcher gridMatcher = regex_grid.matcher(line);
            		
            	    Contact contact = new Contact();
            	    try {
						contact.setTimestamp(adifDateFormatter.parse("" + dateMatcher.group(1) + timeMatcher.group(2)));
						contact.setBand(bandMatcher.group(1));
						contact.setCallsign(callsignMatcher.group(1));
						contact.setRsts(rstsMatcher.group(1));
						contact.setRstr(rstrMatcher.group(1));
						
						// TODO: Others here
						
						contactRepository.save(contact);
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	    
            		// here you can have your logic of comparison.
            	    if(line.toString().equals(".")) {
            	        // do something
            	    }
            	}
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
		return null;
        
    }

    @Override
    public Path load(String filename) {
		return null;
        
    }

    @Override
    public Resource loadAsResource(String filename) {
		return null;
        
    }

    @Override
    public void deleteAll() {
        
    }

    @Override
    public void init() {
        // Nothing to do
    }
}