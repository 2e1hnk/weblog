package weblog;

import org.springframework.data.jpa.repository.JpaRepository;

import weblog.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
