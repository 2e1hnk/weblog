package weblog.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import weblog.model.Contact;
import weblog.model.Entitlement;
import weblog.model.Logbook;
import weblog.model.User;
import weblog.repository.LogbookRepository;

@Service
public class LogbookService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LogbookRepository logbookRepository;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContactService contactService;
	
	@Autowired EntitlementService entitlementService;
	
	public Optional<Logbook> getLogbookById(long id) {
		return logbookRepository.findById(id);
	}
	
	@Transactional
	public void grantEntitlement(Logbook logbook, User user, int entitlementLevel) {
		Entitlement entitlement = new Entitlement();
		entitlement.setEntitlement(entitlementLevel);
		entitlement.setLogbook(logbook);
		entitlement.setUser(user);
		user.addEntitlement(entitlement);
		logbook.addEntitlement(entitlement);
		userService.save(user);
		save(logbook);
		entitlementService.save(entitlement);
	}
	
	public Logbook createLogbook(String logbookName, String locator, User user) {
		Logbook logbook = this.createLogbook(logbookName, locator);
		
		grantEntitlement(logbook, user, Entitlement.FULL );
		
		return logbook;
	}
	
	public Logbook createLogbook(String logbookName, String locator) {
		Logbook logbook = new Logbook();
		logbook.setLat(locationService.extractLatitudeFromLocator(locator));
		logbook.setLng(locationService.extractLongitudeFromLocator(locator));
		logbook.setName(logbookName);
		logbookRepository.save(logbook);
		return logbook;
	}
	
	public List<Logbook> getAllLogbooks() {
		return logbookRepository.findAll();
	}
	
	public void save(Logbook logbook) {
		logbookRepository.save(logbook);
	}
	
	/*
	 * Move all contacts from one logbook to another
	 */
	@Transactional
	public void moveContacts(Logbook fromLogbook, Logbook toLogbook, Boolean deleteAfterMove) {
		for ( Contact contact : fromLogbook.getContacts() ) {
			contact.setLogbook(toLogbook);
			contactService.save(contact);
		}
		if ( deleteAfterMove ) {
			this.delete(fromLogbook);
		}
	}
	
	/*
	 * Delete a logbook AND ALL CONTACTS IN IT!
	 * You should probably be calling moveContactsAndDelete() instead
	 */
	public void delete(Logbook logbook) {
		for (Entitlement entitlement : logbook.getEntitlement() ) {
			entitlementService.deleteAllEntitlementsForLogbookAndUser(entitlement.getLogbook(), entitlement.getUser());
		}
		logbookRepository.delete(logbook);
	}
	
	/*
	 * Returns true if the user is permitted to perform the requested action, otherwise return false
	 */
	public boolean getUserEntitlement(Logbook logbook, User user, int action) {
		for ( Entitlement entitlement : logbook.getEntitlement() ) {
			if ( entitlement.getUser().equals(user) && entitlement.getEntitlement() >= action ) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Get a user's logbook by its name. Note that logbook names are not necessarily unique but should probably
	 * be unique per-user (although this is not enforced). If there are multiple logbooks with the same name
	 * registered to the same user then this will just return the first one it comes to.
	 */
	public Logbook getUserLogbookByName(String username, String logbook_name) {
		User user = userService.getUser(username);
		logger.info("Username: " + username);
		for ( Entitlement entitlement : user.getEntitlement() ) {
			if ( entitlement.getEntitlement() >= Entitlement.VIEW && entitlement.getLogbook().getName().equals(logbook_name) ) {
				return entitlement.getLogbook();
			}
		}
		return null;
	}
}
