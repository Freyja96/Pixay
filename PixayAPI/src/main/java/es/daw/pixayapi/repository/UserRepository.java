package es.daw.pixayapi.repository;

import es.daw.pixayapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    //    Page<User> findByUsername(String username, Pageable pageable);
}