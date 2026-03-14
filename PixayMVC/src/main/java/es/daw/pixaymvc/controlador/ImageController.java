package es.daw.pixaymvc.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class ImageController {
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Por favor, selecciona un archivo.");
                return "redirect:/mis-imagenes";
            }

            // convertir el archivo a byte[]
            byte[] bytes = file.getBytes();

            // TODO: Aquí llamaremos al Service que conecta con la API
            // El service enviará estos bytes a PixayAPI

            redirectAttributes.addFlashAttribute("message", "¡Imagen subida con éxito!");
            return "redirect:/mis-imagenes";

        } catch (IOException e) {
            return "error";
        }
    }
}
