package weblog;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
	@Query(value="SELECT * FROM contact c where c.callsign = :callsign", nativeQuery = true) 
    Collection<Contact> findByCallsign(@Param("callsign") String callsign);
	
	Page<Contact> findAll(Pageable pageable);
}