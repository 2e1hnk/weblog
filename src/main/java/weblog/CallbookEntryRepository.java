package weblog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import weblog.model.CallbookEntry;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface CallbookEntryRepository extends CrudRepository<CallbookEntry, Integer> {
	@Query(value="SELECT * FROM callbook_entry c where c.callsign = :callsign", nativeQuery = true) 
    Collection<CallbookEntry> findByCallsign(@Param("callsign") String callsign);
}