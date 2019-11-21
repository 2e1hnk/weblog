package weblog.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import weblog.model.User;

@Service
public class GalleryService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
    public List<String> findUsersGalleryImages(User user) throws URISyntaxException, IOException {
    	
    	List<String> fileList = new ArrayList<String>();
    	
    	ClassLoader cl = this.getClass().getClassLoader(); 
    	ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    	Resource[] resources = resolver.getResources("classpath*:/upload-dir/" + user.getUsername() + "/gallery/*") ;
    	
    	for (Resource resource: resources){
    	    fileList.add(resource.getFilename());
    	}

    	return fileList;
    }
}
