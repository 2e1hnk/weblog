package weblog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class User {
     
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    // email is used as the user identifier
    @NotBlank(message = "Username is mandatory")
    @Column(unique=true)
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;

    private Date lastLogin;
    
    private boolean enabled = true;
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
 
    // standard constructors / setters / getters / toString
}