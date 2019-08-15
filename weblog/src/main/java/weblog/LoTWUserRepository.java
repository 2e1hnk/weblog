package weblog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import weblog.model.LoTWUser;

@Repository
public interface LoTWUserRepository extends CrudRepository<LoTWUser, Long> {
	@Query(value="SELECT * FROM lotwuser c where c.callsign = :callsign", nativeQuery = true) 
    Collection<LoTWUser> findByCallsign(@Param("callsign") String callsign);
}
