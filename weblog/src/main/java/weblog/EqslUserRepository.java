package weblog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EqslUserRepository extends CrudRepository<EqslUser, Long> {
	@Query(value="SELECT * FROM eqsl_user c where c.callsign = :callsign", nativeQuery = true) 
    Collection<EqslUser> findByCallsign(@Param("callsign") String callsign);
}
