package weblog.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weblog.EntitlementRepository;
import weblog.model.Entitlement;
import weblog.model.Logbook;
import weblog.model.User;

@Service
public class EntitlementService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EntitlementRepository entitlementRepository;
	
	public List<Logbook> getLogbooksAtEntitlement(User user, int entitlementLevel) {
		List<Logbook> logbooks = new ArrayList<Logbook>();
		for ( Entitlement entitlement : entitlementRepository.findByUserAndEntitlementLessThanEqual(user, entitlementLevel) ) {
			logbooks.add(entitlement.getLogbook());
		}
		return logbooks;
	}
	
	/*
	 * This returns the MAXIMUM entitlement level for a user/logbook combination
	 */
	public int getUserLogbookEntitlementLevel(User user, Logbook logbook) {
		int level = 0;
		for ( Entitlement entitlement : entitlementRepository.findByUserAndLogbook(user, logbook) ) {
			if ( entitlement.getEntitlement() > level ) {
				level = entitlement.getEntitlement();
			}
		}
		return level;
	}
}
