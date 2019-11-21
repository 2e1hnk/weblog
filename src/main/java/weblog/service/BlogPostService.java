package weblog.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weblog.repository.BlogPostRepository;
import weblog.model.BlogPost;
import weblog.model.User;

@Service
public class BlogPostService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired BlogPostRepository blogPostRepository;
	
	public Collection<BlogPost> findUsersBlogPosts(User user) {
		return blogPostRepository.findByUserOrderByTimestampDesc(user);
	}
	
	public void save(BlogPost blogPost) {
		blogPostRepository.save(blogPost);
	}
}
