package es.daw.pixayapi.controller;

import es.daw.pixayapi.dto.response.image.ImageResponse;
import es.daw.pixayapi.entity.Image;
import es.daw.pixayapi.entity.User;
import es.daw.pixayapi.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    //TODO REGLAS DE ACCESO A LOS ENDPOINTS POST api/imagenes/subir-imagen
    //                                           api/imagenes/mis-imagenes
    //                                           api/imagenes/inicio

    /**
     * Sube una imagen al servidor.
     * @param content
     * @param title
     * @param category
     * @param user
     * @return
     */
    @PostMapping("/subir-imagen") //<-- se une a continuación de RequestMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("file") MultipartFile content,
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @AuthenticationPrincipal User user // Obtenemos el usuario del token
    ){
        Image savedImage = imageService.saveImage(content, title, category, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(savedImage));
    }

    /**
     * Muestra las imágenes del usuario autenticado.
     * @param user
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
     * @return
     */
    @GetMapping("/inicio")// <-- ¿la página de inicio queda bien así? /imagenes/inicio
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Slice<ImageResponse>> getAllImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){ //<-- para todas las imágenes de todos los usuarios
        Slice<Image> slice = imageService.getAllImagesPaged(page, size);

        Slice<ImageResponse> responseSlice = slice
                .map(this::convertToResponse);

        return ResponseEntity.ok(responseSlice);
    }
    private ImageResponse convertToResponse(Image entity) {
        return new ImageResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getUser().getId(),
                entity.getCategory()
        );
    }
}
