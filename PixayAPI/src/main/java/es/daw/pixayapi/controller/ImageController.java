package es.daw.pixayapi.controller;

import es.daw.pixayapi.dto.response.image.ImageResponse;
import es.daw.pixayapi.entity.Image;
import es.daw.pixayapi.entity.User;
import es.daw.pixayapi.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    /**
     * Sube una imagen al servidor.
     * @param content
     * @param title
     * @param category
     * @param subcategory (opcional)
     * @param user
     * @return
     */
    @PostMapping("/subir-imagen") //<-- se une a continuación de RequestMapping
    //TODO para hacer pruebas y luego ya poner el PreAuthorize
    //@PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("file") MultipartFile content,
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("subcategory") String subcategory,
            @AuthenticationPrincipal User user // Obtenemos el usuario del token
    ){
        Image savedImage = imageService.saveImage(content, title, category, subcategory, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(savedImage));
    }

    /**
     * Muestra las imágenes del usuario actual.
     * @param user
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/mis-imagenes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Slice<ImageResponse>> getMyImages(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){ //<-- para las imágenes del usuario actual
        Slice<Image> slice = imageService.getImagesByUser(user, page, size);

        Slice<ImageResponse> responseSlice = slice
                .map(this::convertToResponse);

        return ResponseEntity.ok(responseSlice);
    }

    /**
     * Muestra las imágenes de todos los usuarios.
     * @param page
     * @param size
     * @return
     */
    @GetMapping// <-- página de INICIO
    public ResponseEntity<Slice<ImageResponse>> getAllImages(//<-- para todas las imágenes de todos los usuarios
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Slice<Image> slice = imageService.getAllImagesPaged(page, size);
        Slice<ImageResponse> responseSlice = slice
                .map(this::convertToResponse);

        return ResponseEntity.ok(responseSlice);
    }

    /**
     * Convierte una imagen en una respuesta.
     * @param entity
     * @return
     */
    private ImageResponse convertToResponse(Image entity) {
        return new ImageResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getUser().getId(),
                entity.getCategory(),
                entity.getSubcategory()
        );
    }
}
