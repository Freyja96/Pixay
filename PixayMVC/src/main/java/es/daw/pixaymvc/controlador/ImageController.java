package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.dto.response.*;
import es.daw.pixaymvc.service.ImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ImageController {
    private final WebClient webClientAPI;
    private final ImageService imageService;

    public ImageController(WebClient webClientAPI, ImageService imageService) {
        this.webClientAPI = webClientAPI;
        this.imageService = imageService;
    }

    /**
     * Mostrar inicio
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/")
    public String inicio(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        CustomSlice<ImageResponse> slice = imageService.getAllImages(0, 12, token);

        System.out.println("Imágenes recibidas de la API: " + slice.getContent().size());
        if(!slice.getContent().isEmpty()){
            System.out.println("Título de la primera: " + slice.getContent().get(0).title());
        }

        model.addAttribute("imagenes", slice.getContent());
        model.addAttribute("hasNext", slice.isHasNext());

        model.addAttribute("query", "");
        model.addAttribute("selectedCategoryId", null);
        model.addAttribute("selectedSubcategoryId", null);
        model.addAttribute("selectedCategoryName", "Todas las categorías");

        return "pantallas/inicio";
    }

    /**
     * mostrar el formulario de subida de imagen
     * @param model
     * @return
     */
    @GetMapping("/subir-imagen")
    public String showUploadForm(Model model) {
        cargarSidebar(model);
        return "pantallas/subir-imagen";
    }

    /**
     * Clic en Publicar en el formulario, subir imagen y redirigir a inicio. Si no hay token, no puede subir nada.
     * @param content
     * @param title
     * @param category_id
     * @param subcategory_id
     * @param redirectAttributes
     * @param session
     * @return
     */
    @PostMapping("/subir-imagen")
    public String handleFileUpload(@RequestParam("content") MultipartFile content,
                                   @RequestParam("title") String title,
                                   @RequestParam("category_id") String category_id,
                                   @RequestParam(value = "subcategory_id", required = false) String subcategory_id,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session
                                   //Model model
    ) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("content", content.getResource());
        builder.part("title", title);
        builder.part("category_id", category_id);
        if (subcategory_id != null) builder.part("subcategory_id", subcategory_id);

        try {
            webClientAPI.post()
                    .uri("imagenes/subir-imagen")
                    .header("Authorization", "Bearer " + token)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(ImageResponse.class)
                    .block();
            redirectAttributes.addFlashAttribute("message", "¡Imagen subida con éxito!");
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("Error subiendo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al conectar con la API: " + e.getMessage());
            return "redirect:/subir-imagen";
        }
    }

    /**
     * Devuelve datos en formato JSON de las imágenes que se han subido.
     * @param page
     * @param size
     * @param session
     * @return
     */
    @GetMapping("/imagenes")
    @ResponseBody
    public CustomSlice<ImageResponse> getImagenesScroll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            HttpSession session) {

        String token = (String) session.getAttribute("token");

        return imageService.getAllImages(page, size, token);
    }

    /**
     * Muestra las imágenes del perfil del usuario actual. Si no hay token, redirige a login.
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/mi-perfil/mis-imagenes")
    public String misImagenes(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        UserProfileResponse profile = webClientAPI.get()
                .uri("usuarios/me")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(UserProfileResponse.class)
                .block();

        CustomSlice<ImageResponse> slice = webClientAPI.get()
                .uri(uriBuilder -> uriBuilder
                        .path("imagenes/usuario/" + profile.id())
                        .queryParam("page", 0)
                        .queryParam("size", 12)
                        .build())
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomSlice<ImageResponse>>() {})
                .block();

        model.addAttribute("usuario", profile);
        model.addAttribute("imagenes", slice.getContent());
        model.addAttribute("hasNext", slice.isHasNext());

        return "pantallas/mi-perfil/mis-imagenes";
    }

    /**
     * Scroll de las imágenes del perfil del usuario actual. Es el endpoint que usará el FETCH del JavaScript
     * @param userId
     * @param page
     * @param size
     * @param session
     * @return
     */
    @GetMapping("/mi-perfil/mis-imagenes/scroll")
    @ResponseBody
    public CustomSlice<ImageResponse> getMisImagenesScroll(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            HttpSession session) {

        String token = (String) session.getAttribute("token");

        return webClientAPI.get()
                .uri(uriBuilder -> uriBuilder
                        .path("imagenes/usuario/" + userId)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomSlice<ImageResponse>>() {})
                .block();
    }

    /**
     * Para mostrar las imágenes guardadas del usuario actual. Si no hay token, redirige a login.
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/mi-perfil/guardadas")
    public String misImagenesGuardadas(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        UserProfileResponse profile = webClientAPI.get()
                .uri("usuarios/me").headers(h -> h.setBearerAuth(token))
                .retrieve().bodyToMono(UserProfileResponse.class).block();

        CustomSlice<ImageResponse> slice = webClientAPI.get()
                .uri(uriBuilder -> uriBuilder.path("imagenes/guardadas")
                        .queryParam("page", 0).queryParam("size", 12).build())
                .headers(h -> h.setBearerAuth(token))
                .retrieve().bodyToMono(new ParameterizedTypeReference<CustomSlice<ImageResponse>>() {})
                .block();

        model.addAttribute("usuario", profile);
        model.addAttribute("imagenes", slice.getContent());
        model.addAttribute("hasNext", slice.isHasNext());
        model.addAttribute("seccion", "guardadas");

        return "pantallas/mi-perfil/mis-imagenes";
    }

    /**
     * Endpoint para el scroll de guardadas
     * @param page
     * @param session
     * @return
     */
    @GetMapping("/mi-perfil/guardadas/scroll")
    @ResponseBody
    public CustomSlice<ImageResponse> getSavedScroll(
            @RequestParam int page, HttpSession session) {
        String token = (String) session.getAttribute("token");
        return webClientAPI.get()
                .uri(uriBuilder -> uriBuilder.path("imagenes/guardadas")
                        .queryParam("page", page).queryParam("size", 12).build())
                .headers(h -> h.setBearerAuth(token))
                .retrieve().bodyToMono(new ParameterizedTypeReference<CustomSlice<ImageResponse>>() {})
                .block();
    }

    /**
     * Mostrar el perfil de otro usuario por ID, con sus imágenes.
     * Si el perfil es público, se muestra aunque no haya token.
     * Si no es público, se muestra solo si hay token.
     * @param id
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/usuario/{id}")
    public String verPerfilAjeno(@PathVariable Long id, HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        // token opcional, por si queremos que los perfiles sean públicos

        UserProfileResponse profile = webClientAPI.get()
                .uri("usuarios/" + id)
                .headers(h -> { if(token != null) h.setBearerAuth(token); })
                .retrieve()
                .bodyToMono(UserProfileResponse.class)
                .block();

        CustomSlice<ImageResponse> slice = webClientAPI.get()
                .uri(uriBuilder -> uriBuilder
                        .path("imagenes/usuario/" + id)
                        .queryParam("page", 0)
                        .queryParam("size", 12)
                        .build())
                .headers(h -> { if(token != null) h.setBearerAuth(token); })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomSlice<ImageResponse>>() {})
                .block();

        model.addAttribute("usuario", profile);
        model.addAttribute("imagenes", slice.getContent());
        model.addAttribute("hasNext", slice.isHasNext());

        return "pantallas/mi-perfil/mis-imagenes";
    }

    /**
     * Mostrar las imágenes guardadas de otro usuario por ID.
     * @param id
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/usuario/{id}/guardadas")
    public String verGuardadasAjeno(@PathVariable Long id, HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");

        UserProfileResponse profile = webClientAPI.get()
                .uri("usuarios/" + id).headers(h -> { if(token!=null) h.setBearerAuth(token); })
                .retrieve().bodyToMono(UserProfileResponse.class).block();

        CustomSlice<ImageResponse> slice = webClientAPI.get()
                .uri(uriBuilder -> uriBuilder.path("imagenes/usuario/" + id + "/guardadas").build())
                .headers(h -> { if(token!=null) h.setBearerAuth(token); })
                .retrieve().bodyToMono(new ParameterizedTypeReference<CustomSlice<ImageResponse>>() {})
                .block();

        model.addAttribute("usuario", profile);
        model.addAttribute("imagenes", slice.getContent());
        model.addAttribute("hasNext", slice.isHasNext());
        model.addAttribute("seccion", "guardadas"); // Para que el botón se vea azul

        return "pantallas/mi-perfil/mis-imagenes";
    }

    /**
     * Mostrar el detalle de una imagen por ID. Incluye chat.
     * @param id
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/imagen/{id}")
    public String verDetalleImagen(@PathVariable Long id, HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");

        ImageResponse imagen = imageService.getImageById(id, token);

        UserProfileResponse autor = webClientAPI.get()
                .uri("usuarios/" + imagen.userId())
                .retrieve()
                .bodyToMono(UserProfileResponse.class)
                .block();

        model.addAttribute("imagen", imagen);
        model.addAttribute("autor", autor);

        return "pantallas/detalle";
    }

    /**
     * Mostrar los resultados de la búsqueda por título. Si no hay query, muestra todas las imágenes.
     * @param query
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/busqueda")
    public String mostrarBusqueda(@RequestParam(required = false) String query,
                                  @RequestParam(required = false) Long categoryId,
                                  @RequestParam(required = false) Long subcategoryId,
                                  Model model,
                                  HttpSession session
    ) {
        String token = (String) session.getAttribute("token");

        CustomSlice<ImageResponse> slice = webClientAPI.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("imagenes/buscar")
                            .queryParam("page", 0)
                            .queryParam("size", 12);
                    if (query != null && !query.isEmpty()) uriBuilder.queryParam("query", query);
                    if (categoryId != null) uriBuilder.queryParam("categoryId", categoryId);
                    if (subcategoryId != null) uriBuilder.queryParam("subcategoryId", subcategoryId);
                    return uriBuilder.build();
                })
                .headers(h -> { if (token != null) h.setBearerAuth(token); })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomSlice<ImageResponse>>() {})
                .block();

//        if (query != null && !query.isEmpty()) {
//            slice = webClientAPI.get()
//                    .uri(uriBuilder -> {
//                        uriBuilder.path("imagenes/buscar")
//                                .queryParam("page", 0)
//                                .queryParam("size", 12)
//                                .queryParam("query", query);
//                        if (categoryId != null) uriBuilder.queryParam("categoryId", categoryId);
//                        return uriBuilder.build();
//                    })
//                    .headers(h -> { if(token != null) h.setBearerAuth(token); })
//                    .retrieve()
//                    .bodyToMono(new ParameterizedTypeReference<CustomSlice<ImageResponse>>() {})
//                    .block();
//        } else if (categoryId != null) {
//            // Búsqueda solo por categoría
//            slice = webClientAPI.get()
//                    .uri(uriBuilder -> uriBuilder.path("imagenes/buscar")
//                            .queryParam("categoryId", categoryId)
//                            .queryParam("page", 0).queryParam("size", 12).build())
//                    .headers(h -> { if(token != null) h.setBearerAuth(token); })
//                    .retrieve()
//                    .bodyToMono(new ParameterizedTypeReference<CustomSlice<ImageResponse>>() {})
//                    .block();
//        } else {
//            slice = imageService.getAllImages(0, 12, token);
//        }


        model.addAttribute("query", query);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedSubcategoryId", subcategoryId);
        model.addAttribute("imagenes", slice.getContent());
        model.addAttribute("hasNext", slice.isHasNext());

        //Categorías y subcategorías en el sidebar:
        cargarSidebar(model);
        return "pantallas/busqueda";
    }

    /**
     * Scroll de la pantalla de búsqueda. Es el endpoint que usará el FETCH del JavaScript
     * @param query
     * @param page
     * @param session
     * @return
     */
    @GetMapping("/busqueda/scroll")
    @ResponseBody
    public CustomSlice<ImageResponse> getBusquedaScroll(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam int page, HttpSession session) {
        String token = (String) session.getAttribute("token");

        String path = (query != null && !query.isEmpty()) ? "imagenes/buscar" : "imagenes";

        return webClientAPI.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path).queryParam("page", page).queryParam("size", 12);
                    if(query != null) uriBuilder.queryParam("query", query);
                    if(categoryId != null) uriBuilder.queryParam("categoryId", categoryId);
                    if(subcategoryId != null) uriBuilder.queryParam("subcategoryId", subcategoryId);
                    return uriBuilder.build();
                })
                .headers(h -> { if(token!=null) h.setBearerAuth(token); })
                .retrieve().bodyToMono(new ParameterizedTypeReference<CustomSlice<ImageResponse>>() {}).block();
    }
    private void cargarSidebar(Model model) {
        List<CategoryResponse> categories = webClientAPI.get()
                .uri("categories")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CategoryResponse>>() {})
                .block();

        List<SubcategoryResponse> subcategories = webClientAPI.get()
                .uri("subcategories")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SubcategoryResponse>>() {})
                .block();

        model.addAttribute("categoriesObjects", categories);
        model.addAttribute("subcategoriesObjects", subcategories);
    }
}