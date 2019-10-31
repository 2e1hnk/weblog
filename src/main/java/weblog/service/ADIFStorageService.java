package weblog.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import QRZClient2.QRZClient2;
import weblog.AuthenticationFacade;
import weblog.CallbookEntryRepository;
import weblog.ContactRepository;
import weblog.StorageException;
import weblog.StorageFileNotFoundException;
import weblog.model.CallbookEntry;
import weblog.model.Contact;
import weblog.model.Logbook;

@Service
public class ADIFStorageService implements StorageService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	Pattern regex_date = Pattern.compile("<QSO_DATE:[0-9]*>([0-9]{8})", Pattern.CASE_INSENSITIVE);
	Pattern regex_time = Pattern.compile("<TIME_ON:[0-9]*>([0-9]{4})", Pattern.CASE_INSENSITIVE);
	Pattern regex_band = Pattern.compile("<BAND:[0-9]*>([0-9]{0,4}[a-zA-Z]{0,2})", Pattern.CASE_INSENSITIVE);
	Pattern regex_mode = Pattern.compile("<MODE:[0-9]*>([a-zA-Z0-9]{0,8})", Pattern.CASE_INSENSITIVE);
	Pattern regex_callsign = Pattern.compile("<CALL:[0-9]*>([a-zA-Z0-9\\/]{0,10})", Pattern.CASE_INSENSITIVE);
	Pattern regex_rsts = Pattern.compile("<RST_SENT:[0-9]*>([0-9]{0,3})", Pattern.CASE_INSENSITIVE);
	Pattern regex_rstr = Pattern.compile("<RST_RCVD:[0-9]*>([0-9]{0,3})", Pattern.CASE_INSENSITIVE);
	Pattern regex_grid = Pattern.compile("<GRIDSQUARE:[0-9]*>([a-zA-Z0-9]{0,6})", Pattern.CASE_INSENSITIVE);
	
	SimpleDateFormat adifDateFormatter = new SimpleDateFormat("yyyyMMddHHmm");
	
	@Autowired ContactService contactService;
	
	@Autowired CallbookEntryRepository callbookEntryRepository;
	
	@Autowired LogbookService logbookService;
	
	@Autowired AuthenticationFacade authentication;
	
	private QRZClient2 qrzClient = new QRZClient2();

    @Autowired
    public ADIFStorageService() {
    }

    @Override
    public void store(MultipartFile file) {
    	
    	String newLogName = StringUtils.cleanPath(file.getOriginalFilename());
    	
    	if ( newLogName.equals("") ) {
    		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
    		Date date = new Date();
    		newLogName = authentication.getUser().getUsername() + "-" + dateFormat.format(date);
    	}
    	
    	logger.info("Importing " + newLogName + " with size " + file.getSize());
    	
    	// TODO: use correct locator here
    	Logbook logbook = logbookService.createLogbook(newLogName, authentication.getUser().getLocator(), authentication.getUser());
        
    	int linesImported = 0, linesSkipped = 0;
    	
        try {
        	/*
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + newLogName);
            }
            */
            if (newLogName.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + newLogName);
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
                		// Handle mandatory matches
            	    	dateMatcher.find();
            	    	timeMatcher.find();
            	    	contact.setTimestamp(adifDateFormatter.parse("" + dateMatcher.group(1) + timeMatcher.group(1)));
						
            	    	contact.setLogbook(logbook);
            	    	
            	    	callsignMatcher.find();
            	    	contact.setCallsign(callsignMatcher.group(1));
						
            	    	// Handle non-mandatory matches
            	    	if ( bandMatcher.find() ) {
            	    		contact.setBand(bandMatcher.group(1));
            	    	}
            	    	if ( modeMatcher.find() ) {
            	    		contact.setMode(modeMatcher.group(1));
            	    	}
            	    	if ( rstsMatcher.find() ) {
            	    		contact.setRsts(rstsMatcher.group(1));
            	    	}
            	    	if ( rstrMatcher.find() ) {
            	    		contact.setRstr(rstrMatcher.group(1));
            	    	}
            	    	if ( gridMatcher.find() ) {
            	    		contact.setLocation(gridMatcher.group(1));
            	    	}
            	    	
						// TODO: Others here
						
						contactService.save(contact);
						linesImported++;
						
						// Populate Callbook entry
						if ( callbookEntryRepository.findByCallsign(callsignMatcher.group(1)).isEmpty() ) {
							try {
								callbookEntryRepository.save(new CallbookEntry(qrzClient.lookupCallsign(callsignMatcher.group(1))));
							} catch (Exception e) {
								// Callsign not found in QRZ
							}
						}
						
					} catch (ParseException | IllegalStateException e) {
						// Ignore this line
						logger.error("Skipped importing line: " + line);
						linesSkipped++;
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
            throw new StorageException("Failed to store file " + newLogName, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
		return null;
        
    }

    // TODO: Make this logbook-aware
    @Override
    public Path load(String filename) {
    	// Create a temporary file
    	File tmpFile = null;
    	try {
			tmpFile = File.createTempFile("log", ".adi");
			FileWriter writer = new FileWriter(tmpFile);
			for ( Contact contact : contactService.findAll() ) {
				writer.write(contact.toString());
			}
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	
		return tmpFile.toPath();
        
    }

    @Override
    public Resource loadAsResource(Long logbookId, String filename) {
    	try { 
	    	// Create a temporary file
	    	File tmpFile = null;
	    	try {
				tmpFile = File.createTempFile("log", ".adi");
				FileWriter writer = new FileWriter(tmpFile);
				writer.write("Exported from weblog online logging software\n");
				writer.write(tmpFile.getName() + "\n");
				writer.write("\n<EOH>\n");
				for ( Contact contact : contactService.findAll() ) {
					writer.write(contact.toString());
				}
			    writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
	    	
			Path tmpFilePath = tmpFile.toPath();
			Resource resource = new UrlResource(tmpFilePath.toUri());
			return resource;
	    }
	    catch (MalformedURLException e) {
	        throw new StorageFileNotFoundException("Could not read file: " + filename, e);
	    }
    }

    @Override
    public void deleteAll() {
        
    }

    @Override
    public void init() {
        // Nothing to do
    }
}