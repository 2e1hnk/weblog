package weblog.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import weblog.model.BlogPost;
import weblog.model.User;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
	Collection<BlogPost> findByUserOrderByTimestampDesc(User user);
}
