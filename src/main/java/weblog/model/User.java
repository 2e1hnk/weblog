package weblog.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "users")
public class User {
 
	/*
	 * Standard User Fields
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String username;
 
    private String password;
    
    private Boolean enabled = true;
    
    private boolean admin = false;
    
    private String locator = "XX00xx";
    
    /*
     * User permissions fields
     */
    @ManyToMany
    @JoinTable( 
        name = "users_roles", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Collection<Role> roles;
    
    /*
     * Settings fields
     */
    private String theme = "scout";
    
    /*
     * Logbook-specific fields
     * 
     * THIS WILL BE REPLACED WITH THE ENTITLEMENTS FIELD BELOW
     */
    @ManyToMany
    @JoinTable(
        name = "user_logbooks", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "logbook_id", referencedColumnName = "id"))
    private Collection<Logbook> logbooks = new ArrayList<Logbook>();
    
    @OneToMany(mappedBy = "entitlement")
    Set<Entitlement> entitlement;
    
    public User() {
    	
    }

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

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

	public String getLocator() {
		return locator;
	}

	public void setLocator(String locator) {
		this.locator = locator;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		if ( this.getRoles() == null ) {
			this.setRoles(new ArrayList<Role>());
		}
		if ( !this.getRoles().contains(role) ) {
			this.roles.add(role);
		}
	}
	
	public void removeRole(Role role) {
		this.roles.remove(role);
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

}