package weblog.model;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Logbook {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String name;
	
    @ManyToMany(mappedBy = "logbooks")
    private Collection<User> users = new ArrayList<User>();
    
    @OneToMany(mappedBy="logbook")
    private Collection<Contact> contacts = new ArrayList<Contact>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}
	
	public void associateUserWithLogbook(User user) {
		if ( !this.users.contains(user) ) {
			this.users.add(user);
		}
	}
	
	public Collection<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Collection<Contact> contacts) {
		this.contacts = contacts;
	}

	public void dissociateUserFromLogbook(User user) {
		if ( this.users.contains(user) ) {
			this.users.remove(user);
		}
	}
	
	
}
