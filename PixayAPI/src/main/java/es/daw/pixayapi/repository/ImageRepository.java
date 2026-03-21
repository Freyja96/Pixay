package es.daw.pixayapi.repository;

import es.daw.pixayapi.entity.Image;
import es.daw.pixayapi.entity.User;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Slice<Image> findByUser(User user, Pageable pageable);
    Slice<Image> findAllByOrderByIdDesc(Pageable pageable);
    Slice<Image> findByCategory(String category, Pageable pageable);
}
