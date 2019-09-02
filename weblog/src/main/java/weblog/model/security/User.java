package weblog.model.security;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class User {
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String username;
	
	@Column
	private String password;
	
	@ManyToMany (fetch = FetchType.EAGER)
	@JoinTable (name = "Authorities_users",
		joinColumns = { @JoinColumn(name = "usuario_id") },
		inverseJoinColumns = { @JoinColumn (name = "authority_id") }
			)
	private Set<Authority> authority;
	
	public User(String username, String password, List<GrantedAuthority> grantedAuthorities) {
		this.setUsername(username);
		this.setPassword(password);
		for ( GrantedAuthority grantedAuthority : grantedAuthorities ) {
			//this.authority.add(grantedAuthority.getAuthority());
		}
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
	public Set<Authority> getAuthority() {
		return authority;
	}
	public void setAuthority(Set<Authority> authority) {
		this.authority = authority;
	}
	
}
