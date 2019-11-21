package weblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import weblog.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
