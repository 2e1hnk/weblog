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

import weblog.EntitlementEnum;
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
	
	public Optional<Logbook> getLogbookById(long id) {
		return logbookRepository.findById(id);
	}
	
	public void grantEntitlement(Logbook logbook, User user, List<EntitlementEnum> entitlements) {
		for ( EntitlementEnum entitlementEnum : entitlements ) {
			Entitlement entitlement = new Entitlement();
			entitlement.setEntitlement(entitlementEnum);
			user.addEntitlement(entitlement);
			logbook.addEntitlement(entitlement);
			userService.save(user);
			save(logbook);
		}
	}
	
	public Logbook createLogbook(String logbookName, String locator, User user) {
		Logbook logbook = this.createLogbook(logbookName, locator);
		
		grantEntitlement(logbook, user, Arrays.asList(EntitlementEnum.VIEW, EntitlementEnum.ADD, EntitlementEnum.UPDATE, EntitlementEnum.DELETE) );
		
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
	 * Delete a logbook AND AND CONTACTS IN IT!
	 * You should probably be calling moveContactsAndDelete() instead
	 */
	public void delete(Logbook logbook) {
		for (User user : logbook.getUsers() ) {
			user.dissociateFromLogbook(logbook);
		}
		logbookRepository.delete(logbook);
	}
}
