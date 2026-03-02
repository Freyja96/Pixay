package es.daw.foodexpressapi.repository;

import es.daw.foodexpressapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    //    Page<User> findByUsername(String username, Pageable pageable);
}