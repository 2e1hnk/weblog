package weblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import weblog.model.Logbook;

@Repository
public interface LogbookRepository extends JpaRepository<Logbook, Long> {
	
}
