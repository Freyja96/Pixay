package es.daw.foodexpressapi.controller;

import es.daw.foodexpressapi.dto.RestaurantDTO;
import es.daw.foodexpressapi.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> findAll() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RestaurantDTO> create(@RequestBody RestaurantDTO restaurantDTO){
        Optional<RestaurantDTO> result = restaurantService.create(restaurantDTO);

        //return ResponseEntity.ok(result.get()); //200
        if (result.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.get());
        }

        return ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (restaurantService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> update(@PathVariable Long id,
                                                @RequestBody RestaurantDTO restaurantDTO) {

        return ResponseEntity.ok(restaurantService.update(id, restaurantDTO));
    }
}
