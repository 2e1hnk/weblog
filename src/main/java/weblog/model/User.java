package weblog.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String username;
 
    private String password;
    
    private Boolean enabled = true;
    
    @ManyToMany
    @JoinTable(
        name = "user_logbooks", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "logbook_id", referencedColumnName = "id"))
    private Collection<Logbook> logbooks = new ArrayList<Logbook>();
    
    public Collection<Logbook> getLogbooks() {
		return logbooks;
	}

	public void setLogbooks(Collection<Logbook> logbooks) {
		this.logbooks = logbooks;
	}

	public void associateWithLogbook(Logbook logbook) {
		if ( !this.logbooks.contains(logbook) ) {
			this.logbooks.add(logbook);
		}
	}
	
	public void dissociateFromLogbook(Logbook logbook) {
		if ( this.logbooks.contains(logbook) ) {
			this.logbooks.remove(logbook);
		}
	}
	
	public Logbook getAnyLogbook() {
		return (Logbook) logbooks.toArray()[0];
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

}