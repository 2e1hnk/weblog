package weblog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import weblog.model.ClublogUser;

@Repository
public interface ClublogUserRepository extends CrudRepository<ClublogUser, Long> {
	@Query(value="SELECT * FROM clublog_user c where c.callsign = :callsign", nativeQuery = true) 
    Collection<ClublogUser> findByCallsign(@Param("callsign") String callsign);
}
