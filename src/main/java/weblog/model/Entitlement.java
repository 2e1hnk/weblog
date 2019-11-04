package weblog.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import weblog.EntitlementEnum;

@Entity
public class Entitlement {
	
	@Id
	private Long id;
	
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
	@ManyToOne
    @JoinColumn(name = "logbook_id")
    private Logbook logbook;
	
	private EntitlementEnum entitlement;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Logbook getLogbook() {
		return logbook;
	}

	public void setLogbook(Logbook logbook) {
		this.logbook = logbook;
	}

	public EntitlementEnum getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(EntitlementEnum entitlement) {
		this.entitlement = entitlement;
	}
	
	
}
