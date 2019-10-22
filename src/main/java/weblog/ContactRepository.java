package weblog;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import weblog.model.Contact;
import weblog.model.Logbook;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
	@Query(value="SELECT * FROM contact c where c.callsign = :callsign AND c.logbook = :logbook", nativeQuery = true) 
    Collection<Contact> findByLogbookInAndCallsignIn(@Param("logbook") String logbook, @Param("callsign") String callsign);
	
	Page<Contact> findByLogbookOrderByTimestampDesc(Logbook logbook, Pageable pageable);
	Collection<Contact> findByLogbook(String logbook);
	Page<Contact> findAll(Pageable pageable);
	Contact findTopByOrderByIdDesc();
	Contact findTopByOrderByIdAsc();
	
    @Query("SELECT new weblog.BandStats(v.band, COUNT(v)) FROM Contact v GROUP BY v.band")
    List<BandStats> findBandStats();
}