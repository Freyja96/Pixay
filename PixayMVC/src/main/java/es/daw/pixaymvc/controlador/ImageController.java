package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.dto.response.CustomSlice;
import es.daw.pixaymvc.dto.response.ImageResponse;
import es.daw.pixaymvc.service.ImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

        model.addAttribute("imagenes", slice.getContent());
        model.addAttribute("hasNext", slice.isHasNext());

        return "pantallas/inicio";
    }
    //TODO MÉTODOS PARA SUBIR IMAGENES
    @GetMapping("/subir-imagen")//mostrar el formulario
    public String showUploadForm(Model model) {
        List<String> categories = webClientAPI
                .get()
                .uri("categories")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        List<String> subcategories = webClientAPI
                .get()
                .uri("subcategories")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        return "pantallas/subir-imagen";
    }
    @PostMapping("/subir-imagen") //recibir y procesar el archivo -> Click en Publicar en el formulario
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("title") String title,
                                   @RequestParam("category") String category,
                                   @RequestParam("subcategory") String subcategory,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Por favor, selecciona un archivo.");

                return "redirect:/mi-perfil/mis-imagenes";
            }
            imageService.saveImage(file, title, category, subcategory);
            // convertir el archivo a byte[]
            byte[] bytes = file.getBytes();

            // TODO: llamar al Service que conecta con la API
            // El service enviará estos bytes a PixayAPI

            redirectAttributes.addFlashAttribute("message", "¡Imagen subida con éxito!");
            return "redirect:/mi-perfil/mis-imagenes";

        } catch (IOException e) {
            return "error";
        }
    }
}