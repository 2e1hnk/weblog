package weblog.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import weblog.ContactRepository;
import weblog.StorageFileNotFoundException;
import weblog.model.Contact;

public class CabrilloStorageService implements StorageService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired ContactRepository contactRepository;
	
    @Autowired
    public CabrilloStorageService() {
    }

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(MultipartFile file) {
		// TODO Auto-generated method stub

	}

	@Override
	public Stream<Path> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path load(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource loadAsResource(String filename) {
    	try { 
	    	// Create a temporary file
	    	File tmpFile = null;
	    	try {
				tmpFile = File.createTempFile("log", ".cbi");
				FileWriter writer = new FileWriter(tmpFile);
				writer.write("START-OF-LOG: 3.0\n");
				writer.write("X-FILENAME: " + tmpFile.getName() + "\n");
				for ( Contact contact : contactRepository.findAll() ) {
					writer.write(contact.toCabrillo());
				}
				writer.write("END-OF-LOG: ");
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
		// TODO Auto-generated method stub

	}

}
