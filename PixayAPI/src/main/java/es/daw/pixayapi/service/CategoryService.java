package es.daw.pixayapi.service;

import es.daw.pixayapi.entity.Category;
import es.daw.pixayapi.entity.Subcategory;
import es.daw.pixayapi.repository.CategoryRepository;
import es.daw.pixayapi.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    public List<String> getAllCategoryNames() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    public List<String> getAllSubcategoryNames() {
        return subcategoryRepository.findAll().stream()
                .map(Subcategory::getName)
                .collect(Collectors.toList());
    }
}