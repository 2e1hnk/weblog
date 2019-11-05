package weblog.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Entitlement {
	
	/*
	 * These are the entitlement levels. These are heirarchical so if a user has UPDATE, it is assumed that
	 * they also have VIEW and ADD. 
	 */
	public static final int NONE = 0;
	public static final int VIEW = 1;
	public static final int ADD = 2;
	public static final int UPDATE = 3;
	public static final int DELETE = 4;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
	@ManyToOne
    @JoinColumn(name = "logbook_id")
    private Logbook logbook;
	
	private int entitlement;

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

	public int getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(int entitlement) {
		this.entitlement = entitlement;
	}
	
	
}
