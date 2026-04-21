package es.daw.pixayapi.repository;

import es.daw.pixayapi.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    // por si queremos filtrar subcategorías por una categoría específica
    List<Subcategory> findByCategory(String category);
}