package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.service.ApiAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perfil")
public class UserController {
    private final ApiAuthService apiAuthService;

    public UserController(ApiAuthService apiAuthService) {
        this.apiAuthService = apiAuthService;
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        try {
            String token = apiAuthService.login(username, password);
            session.setAttribute("token", token);
            session.setAttribute("username", username);

            return "redirect:/pantallas/inicio";

        } catch (Exception e) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }
//    @GetMapping("/{username}")
//    public String verPerfil(@PathVariable String username, Model model) {
//        // llama a la API con WebClient para pedir los datos de 'username'
//        // model.addAttribute("usuario", datosDesdeAPI);
//        return "perfil"; // Devuelve perfil.html
//    }

    @GetMapping("/{username}/mis-imagenes")
    public String verMisImagenes(@PathVariable String username, Model model) {
        // Llama a la API: /api/imagenes/mis-imagenes
        // model.addAttribute("imagenes", listaDesdeAPI);
        return "mi-perfil/mis-imagenes";
    }
}