package weblog.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class BlogComment {
	
	@Id
	private long id;
	
	@ManyToOne(optional = true)
	private User user;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date timestamp;
	
	@ManyToOne
	private BlogPost blogPost;
	
	private String comment;

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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
