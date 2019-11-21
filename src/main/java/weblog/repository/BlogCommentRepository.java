package weblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import weblog.model.BlogComment;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {

}
