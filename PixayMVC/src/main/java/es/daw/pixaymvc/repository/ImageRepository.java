package es.daw.pixaymvc.repository;

import es.daw.pixaymvc.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByUserId(Long userId);
}
