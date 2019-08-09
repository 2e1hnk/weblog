package weblog;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
	//List<User> findByEmail(String email);

    @Query("UPDATE User u SET u.lastLogin=:lastLogin WHERE u.username = ?#{ principal?.username }")
    @Modifying
    @Transactional
    public void updateLastLogin(@Param("lastLogin") Date lastLogin);
}