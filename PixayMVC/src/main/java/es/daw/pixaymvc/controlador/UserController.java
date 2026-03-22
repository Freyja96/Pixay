package es.daw.pixaymvc.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/perfil")
public class UserController {
    @GetMapping("/{username}")
    public String verPerfil(@PathVariable String username, Model model) {
        // llama a la API con WebClient para pedir los datos de 'username'
        // model.addAttribute("usuario", datosDesdeAPI);
        return "perfil"; // Devuelve perfil.html
    }

    @GetMapping("/{username}/mis-imagenes")
    public String verMisImagenes(@PathVariable String username, Model model) {
        // Llama a la API: /api/imagenes/mis-imagenes
        // model.addAttribute("imagenes", listaDesdeAPI);
        return "mi-perfil/mis-imagenes";
    }
}