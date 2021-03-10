package group2.wed.repository;

import group2.wed.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("from User u where u.username = :username and u.password = :password")
    Optional<User> findByUserNameAndPassword(@Param("username") String username,
                                                         @Param("password") String password);
    @Query("from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username")String username);

    @Query("FROM User u WHERE u.username LIKE CONCAT('%',:username,'%')")
    List<User> searchByUsername(@Param("username")String username);
}
