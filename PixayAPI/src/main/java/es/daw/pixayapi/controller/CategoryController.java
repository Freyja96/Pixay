package es.daw.pixayapi.controller;

import es.daw.pixayapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<String> getCategorias() {
        return categoryService.getAllCategoryNames();
    }

    @GetMapping("/subcategories")
    public List<String> getSubcategorias() {
        return categoryService.getAllSubcategoryNames();
    }
}