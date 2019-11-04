package weblog.model;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Logbook {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Field
	private String name;
	
	private Double lat;
	private Double lng;
	
	// This will be replaced by the Entitlements below
    @ManyToMany(mappedBy = "logbooks")
    @ContainedIn
    @JsonIgnore
    private Collection<User> users = new ArrayList<User>();
    
    @OneToMany(mappedBy = "entitlement")
    Set<Entitlement> entitlement;
    
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

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
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
	
	public Collection<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Collection<Contact> contacts) {
		this.contacts = contacts;
	}
	
	public int size() {
		return this.getContacts().size();
	}
}
