package es.daw.pixayapi.controller;

import es.daw.pixayapi.dto.response.SubcategoryResponse;
import es.daw.pixayapi.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
@RequiredArgsConstructor
public class SubcategoryController {
    private final SubcategoryRepository subcategoryRepository;
    @GetMapping
    public List<SubcategoryResponse> getAll() {
        return subcategoryRepository.findAll().stream()
                .map(s -> new SubcategoryResponse(
                        s.getId(),
                        s.getName(),
                        s.getCategory().getName()
                ))
                .toList();
    }
}
