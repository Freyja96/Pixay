package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.dto.response.CustomSlice;
import es.daw.pixaymvc.dto.response.ImageResponse;
import es.daw.pixaymvc.dto.response.SubcategoryResponse;
import es.daw.pixaymvc.service.ImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        List<String> categories = webClientAPI.get()
                .uri("categories")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();

        List<SubcategoryResponse> subcategories = webClientAPI.get()
                .uri("subcategories")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SubcategoryResponse>>() {})
                .block();

        model.addAttribute("categories", categories);
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
        if (token == null) {
            return "redirect:/login"; // Si no hay token, no puede subir nada
        }
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("content", content.getResource());
        body.add("title", title);
        body.add("category_id", category_id);
        body.add("subcategory_id", subcategory_id);
        try {
            webClientAPI.post()
                    .uri("imagenes/subir-imagen")
                    .header("Authorization", "Bearer " + token)
                    .body(BodyInserters.fromMultipartData(body))
                    .retrieve()
                    .bodyToMono(ImageResponse.class)
                    .block();
            redirectAttributes.addFlashAttribute("message", "¡Imagen subida con éxito!");
            return "redirect:/";// Redirigimos si va bien
        } catch (Exception e) {
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
}