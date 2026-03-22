package es.daw.pixaymvc.controlador;

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

    public ImageController(WebClient webClientAPI) {
        this.webClientAPI = webClientAPI;
    }

    @GetMapping("/subir-imagen")
    public String showUploadForm(Model model) {
        List<String> categorias = webClientAPI
                .get()
                .uri("/categorias")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        List<String> subcategorias = webClientAPI
                .get()
                .uri("/subcategorias")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        model.addAttribute("categorias", categorias);
        model.addAttribute("subcategorias", subcategorias);
        return "subir-imagen";
    }
    @PostMapping("/subir-imagen")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Por favor, selecciona un archivo.");

                return "redirect:/mis-imagenes";
            }

            // convertir el archivo a byte[]
            byte[] bytes = file.getBytes();

            // TODO: llamar al Service que conecta con la API
            // El service enviará estos bytes a PixayAPI

            redirectAttributes.addFlashAttribute("message", "¡Imagen subida con éxito!");
            return "redirect:/mis-imagenes";

        } catch (IOException e) {
            return "error";
        }
    }
}
