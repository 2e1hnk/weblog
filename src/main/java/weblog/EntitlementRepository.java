package weblog;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import weblog.model.Entitlement;
import weblog.model.Logbook;
import weblog.model.User;

@Repository
public interface EntitlementRepository extends CrudRepository<Entitlement, Long> {
	Collection<Entitlement> findByUserAndEntitlementLessThanEqual(User user, int entitlement);
	List<Entitlement> findByUserAndLogbook(User user, Logbook logbook);
	long deleteByLogbookAndUser(Logbook logbook, User user);
}
