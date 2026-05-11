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

    //para buscar imágenes cuyo título contenga la cadena de texto dada ignorando mayúsculas/minúsculas
    Slice<Image> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    //para buscar imágenes cuyo título contenga la cadena de texto dada ignorando mayúsculas/minúsculas, filtrando por categoría
    Slice<Image> findByTitleContainingIgnoreCaseAndCategoryId(String title, Long categoryId, Pageable pageable);
    Slice<Image> findByTitleContainingIgnoreCaseAndSubcategoryId(String title, Long subcategoryId, Pageable pageable);

    Slice<Image> findByCategoryId(Long categoryId, Pageable pageable);
    Slice<Image> findBySubcategoryId(Long subcategoryId, Pageable pageable);
}
