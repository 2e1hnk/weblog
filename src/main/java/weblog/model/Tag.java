package weblog.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Tag {
	
	@Id
	private long id;
	
	@ManyToMany(mappedBy="tags")
	private Set<BlogPost> blogPosts;
	
	private String tag;
	
	public Tag() {
		this.blogPosts = new HashSet<BlogPost>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<BlogPost> getBlogPosts() {
		return blogPosts;
	}

	public void setBlogPosts(Set<BlogPost> blogPosts) {
		this.blogPosts = blogPosts;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
}
