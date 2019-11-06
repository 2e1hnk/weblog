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
	 * These are the entitlement levels. These are hierarchical so if a user has UPDATE, it is assumed that
	 * they also have VIEW and ADD.
	 */
	public static final int NONE = 0;
	public static final int VIEW = 1;
	public static final int ADD = 2;
	public static final int UPDATE = 3;
	public static final int FULL = 4;		// FULL include permission to delete, permission to alter other user's permissions etc.
	
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

	public String getEntitlementAsText() {
		switch ( this.getEntitlement() ) {
		case 0:
			return "No Access";
			
		case 1:
			return "View-only";
			
		case 2:
			return "View/Add";
			
		case 3:
			return "View/Add/Update";
			
		case 4:
			return "Full Access";
			
		default:
			return "Error";
		}
	}

	public void setEntitlement(int entitlement) {
		this.entitlement = entitlement;
	}
	
	
}
