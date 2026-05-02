package es.daw.pixayapi.controller;

import es.daw.pixayapi.dto.response.ImageResponse;
import es.daw.pixayapi.entity.Image;
import es.daw.pixayapi.entity.User;
import es.daw.pixayapi.service.ImageService;
import es.daw.pixayapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final UserService userService;
    /**
     * Sube una imagen al servidor.
     * @param content
     * @param title
     * @param category_id
     * @param subcategory_id (opcional)
     * @param userDetails
     * @return
     */
    @PostMapping("/subir-imagen") //<-- se une a continuación de RequestMapping
    //TODO para hacer pruebas y luego ya poner el PreAuthorize
    //@PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("content") MultipartFile content,
            @RequestParam("title") String title,
            @RequestParam("category_id") String category_id,
            @RequestParam(value = "subcategory_id", required = false) String subcategory_id,
            @AuthenticationPrincipal UserDetails userDetails // Obtenemos el usuario del token
    ){
        if (userDetails == null) {
            System.out.println("ERROR: El usuario llega como NULL. El Token no se ha validado correctamente.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //pongo traza para ver si el usuario está autenticado
        System.out.println("Usuario que intenta subir: " + userDetails.getUsername());

        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        User user = userService.findByUsername(userDetails.getUsername());

        Image savedImage = imageService.saveImage(content, title, category_id, subcategory_id, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(savedImage));
    }

    /**
     * /**
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
            @RequestParam(defaultValue = "12") int size
            ){ //<-- para las imágenes del usuario actual
        Slice<Image> slice = imageService.getImagesByUser(user, page, size);

        Slice<ImageResponse> responseSlice = slice
                .map(this::convertToResponse);

        return ResponseEntity.ok(responseSlice);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ImageResponse> getImageById(@PathVariable Long id) {
        Image image = imageService.getImageById(id);
        return ResponseEntity.ok(convertToResponse(image));
    }
    /**
     * Muestra las imágenes del usuario por ID
     * @param id
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/usuario/{id}")
    public ResponseEntity<Slice<ImageResponse>> getImagesByUserId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ){
        User user = userService.findById(id);

        Slice<Image> slice = imageService.getImagesByUser(user, page, size);
        Slice<ImageResponse> responseSlice = slice.map(this::convertToResponse);

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
            @RequestParam(defaultValue = "12") int size
    ){
        Slice<Image> slice = imageService.getAllImagesPaged(page, size);
        Slice<ImageResponse> responseSlice = slice
                .map(this::convertToResponse);

        return ResponseEntity.ok(responseSlice);
    }

    @GetMapping("/guardadas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Slice<ImageResponse>> getSavedImages(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        Slice<Image> slice = imageService.getSavedImagesByUser(user, page, size);
        return ResponseEntity.ok(slice.map(this::convertToResponse));
    }

    // En la API
    @GetMapping("/usuario/{id}/guardadas")
    public ResponseEntity<Slice<ImageResponse>> getSavedImagesByUserId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        User user = userService.findById(id);
        Slice<Image> slice = imageService.getSavedImagesByUser(user, page, size);
        return ResponseEntity.ok(slice.map(this::convertToResponse));
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
                entity.getCategory().getName(),
                entity.getSubcategory() != null ? entity.getSubcategory().getName() : "Sin subcategoría" //puede ser nula
        );
    }
}
