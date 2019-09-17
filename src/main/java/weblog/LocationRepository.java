package weblog;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import weblog.model.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
}