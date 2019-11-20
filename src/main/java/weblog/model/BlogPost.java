package weblog.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.format.annotation.DateTimeFormat;

import com.github.rjeschke.txtmark.Processor;

@Entity
public class BlogPost {
	
	@Id
	private long Id;
	
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @IndexedEmbedded
    private User user;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
    
    private String title;
    
    private String headerImageUrl;
    
    // Contents should be in markdown
    @Lob
    private String postContents;
    
    @ManyToMany
    @JoinTable(
    		  name = "BlogPost_Tag", 
    		  joinColumns = @JoinColumn(name = "blogpost_id"), 
    		  inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
    
    private boolean commentsEnabled;
    private boolean anonymousCommentsEnabled;
    
    @OneToMany(mappedBy = "blogPost")
    private Set<BlogComment> comments;
    
    public BlogPost() {
    	this.tags = new HashSet<Tag>();
    }

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHeaderImageUrl() {
		return headerImageUrl;
	}

	public void setHeaderImageUrl(String headerImageUrl) {
		this.headerImageUrl = headerImageUrl;
	}

	public String getPostContents() {
		return postContents;
	}
	
	public String getPostContentsRendered() {
		return Processor.process(this.getPostContents());
	}

	public void setPostContents(String postContents) {
		this.postContents = postContents;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
    
	public boolean isCommentsEnabled() {
		return commentsEnabled;
	}

	public void setCommentsEnabled(boolean commentsEnabled) {
		this.commentsEnabled = commentsEnabled;
	}
	
	public boolean isAnonymousCommentsEnabled() {
		return anonymousCommentsEnabled;
	}

	public void setAnonymousCommentsEnabled(boolean anonymousCommentsEnabled) {
		this.anonymousCommentsEnabled = anonymousCommentsEnabled;
	}

	public Set<BlogComment> getComments() {
		return comments;
	}

	public void setComments(Set<BlogComment> comments) {
		this.comments = comments;
	}
}
