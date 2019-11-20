package weblog.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
     * Logbooks are mapped via an entitlement object - this object also specifies the permissions level
     */
    @OneToMany(mappedBy = "user")
    Set<Entitlement> entitlement;
    
    @OneToMany(mappedBy = "user")
    Set<BlogPost> blogPosts;
    
    public User() {
    	
    }

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/*
	 * @return A list of all logbooks where this user has at least the permission level
	 * specified in @Param entitlementLevel
	 */
	public List<Logbook> getLogbooks(int entitlementLevel) {
		List<Logbook> logbooks = new ArrayList<Logbook>();
		for (Entitlement entitlement : this.getEntitlement() ) {
			if ( entitlement.getEntitlement() >= entitlementLevel ) {
				logbooks.add(entitlement.getLogbook());
			}
		}
		return logbooks;
	}
	
	
	public Logbook getAnyLogbook() {
		return this.getLogbooks(Entitlement.VIEW).get(0);
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

	public Set<Entitlement> getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(Set<Entitlement> entitlement) {
		this.entitlement = entitlement;
	}
	
	public void addEntitlement(Entitlement entitlement) {
		this.entitlement.add(entitlement);
	}

	public Set<BlogPost> getBlogPosts() {
		return blogPosts;
	}

	public void setBlogPosts(Set<BlogPost> blogPosts) {
		this.blogPosts = blogPosts;
	}
	
	public Set<Tag> getAllBlogTags() {
		Set<Tag> tags = new HashSet<Tag>();
		for ( BlogPost blog : this.getBlogPosts() ) {
			tags.addAll(blog.getTags());
		}
		return tags;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", enabled=" + enabled
				+ ", admin=" + admin + ", locator=" + locator + ", roles=" + roles + ", theme=" + theme
				+ ", entitlement=" + entitlement + "]";
	}

}