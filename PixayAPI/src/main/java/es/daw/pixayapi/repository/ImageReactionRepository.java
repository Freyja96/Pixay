package es.daw.pixayapi.repository;

import es.daw.pixayapi.entity.Image;
import es.daw.pixayapi.entity.ImageReaction;
import es.daw.pixayapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageReactionRepository extends JpaRepository<ImageReaction, Long> {
    Optional<ImageReaction> findByUserAndImage(User user, Image image);
}