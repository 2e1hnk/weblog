package weblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import weblog.model.BlogComment;

public interface BlogCommentsRepository extends JpaRepository<BlogComment, Long> {

}
