package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.dto.response.CustomSlice;
import es.daw.pixaymvc.dto.response.ImageResponse;
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
        List<String> categories = webClientAPI
                .get()
                .uri("categories")
                .retrieve()
                //.bodyToFlux(String.class)
                //.collectList()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        List<String> subcategories = webClientAPI
                .get()
                .uri("subcategories")
                .retrieve()
//                .bodyToFlux(String.class)
//                .collectList()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        return "pantallas/subir-imagen";
    }
    //TODO MÉTODO PARA SUBIR IMAGEN
    @PostMapping("/subir-imagen") //recibir y procesar el archivo -> Clic en Publicar en el formulario
    public String handleFileUpload(@RequestParam("content") MultipartFile content,
                                   @RequestParam("title") String title,
                                   @RequestParam("category") String category,
                                   @RequestParam("subcategory") String subcategory,
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
        body.add("category", category);
        body.add("subcategory", subcategory);
        try {
            webClientAPI.post()
                    //TODO CRIS mirar a ver que no sea /imagenes/subir-imagen
                    .uri("imagenes/subir-imagen")
                    .header("Authorization", "Bearer " + token)
                    .body(BodyInserters.fromMultipartData(body))
                    .retrieve()
                    .bodyToMono(ImageResponse.class)
                    .block();
            redirectAttributes.addFlashAttribute("message", "¡Imagen subida con éxito!");
            return "redirect:/";// Redirigimos si todo va bien
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al conectar con la API: " + e.getMessage());
            return "redirect:/subir-imagen";
        }
    }
}