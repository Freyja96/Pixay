package es.daw.pixayapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
//TODO en lugar de List.of, categoryService que lo saque de la bdd
    @GetMapping("/categories")
    public List<String> getCategorias() {
        return List.of("Naturaleza", "Arquitectura", "Personas", "Tecnología");
    }

    @GetMapping("/subcategories")
    public List<String> getSubcategorias() {
        return List.of("Bosques", "Rascacielos", "Retratos", "IA");
    }
}