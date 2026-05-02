package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.dto.response.*;
import es.daw.pixaymvc.service.ImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

        return "pantallas/inicio";
    }

    @GetMapping("/subir-imagen")//mostrar el formulario
    public String showUploadForm(Model model) {
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

        return "pantallas/subir-imagen";
    }

    @PostMapping("/subir-imagen") //Clic en Publicar en el formulario, subir imagen y redirigir a inicio
    public String handleFileUpload(@RequestParam("content") MultipartFile content,
                                   @RequestParam("title") String title,
                                   @RequestParam("category_id") String category_id,
                                   @RequestParam(value = "subcategory_id", required = false) String subcategory_id,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session
                                   //Model model
    ) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login"; // Si no hay token, no puede subir nada

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("content", content.getResource());
        builder.part("title", title);
        builder.part("category_id", category_id);
        if (subcategory_id != null) builder.part("subcategory_id", subcategory_id);

        //MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("content", content.getResource());
//        body.add("title", title);
//        body.add("category_id", category_id);
//        body.add("subcategory_id", subcategory_id);
        try {
            webClientAPI.post()
                    .uri("imagenes/subir-imagen")
                    .header("Authorization", "Bearer " + token)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(ImageResponse.class)
                    .block();
            redirectAttributes.addFlashAttribute("message", "¡Imagen subida con éxito!");
            return "redirect:/";// Redirigimos si va bien
        } catch (Exception e) {
            System.out.println("Error subiendo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al conectar con la API: " + e.getMessage());
            return "redirect:/subir-imagen";
        }
    }

    @GetMapping("/imagenes")
    @ResponseBody // <-- devuelve datos en formato JSON
    public CustomSlice<ImageResponse> getImagenesScroll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            HttpSession session) {

        String token = (String) session.getAttribute("token");

        return imageService.getAllImages(page, size, token);
    }

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

    // Este es el endpoint que usará el FETCH del JavaScript
    @GetMapping("/mi-perfil/mis-imagenes/scroll")
    @ResponseBody
    public CustomSlice<ImageResponse> getMisImagenesScroll(
            @RequestParam Long userId, // Le pasamos el ID del usuario por parámetro
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
}